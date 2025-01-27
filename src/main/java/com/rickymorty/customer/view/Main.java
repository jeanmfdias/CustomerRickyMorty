package com.rickymorty.customer.view;

import com.rickymorty.customer.models.*;
import com.rickymorty.customer.repositories.ICharacterRepository;
import com.rickymorty.customer.repositories.IEpisodeRepository;
import com.rickymorty.customer.repositories.ILocationRepository;
import com.rickymorty.customer.services.ConsumerApi;
import com.rickymorty.customer.services.TranslateData;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final Scanner scanner = new Scanner(System.in);

    private final String ADDRESS_LOCATION = "https://rickandmortyapi.com/api/location/";

    private final String ADDRESS_CHARACTER = "https://rickandmortyapi.com/api/character/";

    private final ConsumerApi consumerApi = new ConsumerApi();

    private final TranslateData translateData = new TranslateData();

    private final ILocationRepository locationRepository;

    private final ICharacterRepository characterRepository;

    private final IEpisodeRepository episodeRepository;

    public Main(ILocationRepository iLocationRepository,
                ICharacterRepository iCharacterRepository,
                IEpisodeRepository iEpisodeRepository) {
        this.locationRepository = iLocationRepository;
        this.characterRepository = iCharacterRepository;
        this.episodeRepository = iEpisodeRepository;
    }

    public void showMenu() {
        scanner.reset();
        int option = -1;
        while (option != 0) {
            System.out.println("Choose a option: ");
            System.out.println("1 - Search Location");
            System.out.println("2 - List All Locations");
            System.out.println("3 - Search All Character by Location");
            System.out.println("4 - Search All Episodes by Character");
            System.out.println("5 - List All Episodes");
            System.out.println("6 - Search Character by name");
            System.out.println("0 - Exit");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    this.searchLocation();
                    break;
                case 2:
                    this.listAllLocations();
                    break;
                case 3:
                    this.searchCharacterByLocation();
                    break;
                case 4:
                    this.searchEpisodesByCharacter();
                    break;
                case 5:
                    this.listAllEpisodes();
                    break;
                case 6:
                    this.findCharacterByName();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void searchLocation() {
        scanner.reset();
        System.out.print("Type location ID: ");
        int locationId = scanner.nextInt();
        RickyMortyLocation location = this.getOneLocation(locationId);
        this.locationRepository.save(location);
    }

    private RickyMortyLocation getOneLocation(int locationId) {
        String fullAddress = ADDRESS_LOCATION + locationId;

        var json = consumerApi.getData(fullAddress);

        RickyMortyLocationRecord rmLocation = translateData.getData(json, RickyMortyLocationRecord.class);
        return new RickyMortyLocation(rmLocation);
    }

    private void listAllLocations() {
        List<RickyMortyLocation> locations = this.locationRepository.findAll();
        locations.stream().forEach(System.out::println);
    }

    private void listAllEpisodes() {
        List<RickyMortyEpisode> episodes = this.episodeRepository.findAll();
        episodes.stream()
                .sorted(Comparator.comparing(RickyMortyEpisode::getAirDate))
                .forEach(System.out::println);
    }

    private void searchCharacterByLocation() {
        List<RickyMortyLocation> locations = this.locationRepository.findAll();
        locations.forEach(System.out::println);

        System.out.print("Choice a location ID: ");
        scanner.reset();
        Long locationId = scanner.nextLong();

        Optional<RickyMortyLocation> location = locations.stream()
                .filter(l -> l.getId().equals(locationId))
                .findFirst();

        if (location.isPresent()) {
            RickyMortyLocation locationFinded = location.get();
            String url = ADDRESS_LOCATION + "?name=" + locationFinded.getName().toLowerCase().replace(" ", "+");
            String json = this.consumerApi.getData(url);
            RickyMortyLocationListRecord locationRecords = this.translateData.getData(json, RickyMortyLocationListRecord.class);
            List<RickyMortyCharacter> characters = new ArrayList<>();
            for (RickyMortyLocationRecord record : locationRecords.locations()) {
                for (String urlCharacter : record.residents()) {
                    json = this.consumerApi.getData(urlCharacter);
                    RickyMortyCharacterRecord characterRecord = this.translateData.getData(json, RickyMortyCharacterRecord.class);
                    RickyMortyCharacter character = new RickyMortyCharacter(characterRecord);
                    characters.add(character);
                    showProgress();
                }
            }
            locationFinded.setResidents(characters);
            this.locationRepository.save(locationFinded);
        } else {
            System.out.println("Location not found!");
        }
    }

    private void searchEpisodesByCharacter() {
        List<RickyMortyLocation> locations = this.locationRepository.findAll();
        locations.forEach(System.out::println);

        System.out.print("Choice a character ID: ");
        scanner.reset();
        Long characterId = scanner.nextLong();

        Optional<RickyMortyCharacter> character = locations.stream()
                .flatMap(l -> l.getResidents().stream())
                .filter(c -> c.getId().equals(characterId))
                .findFirst();

        if (character.isPresent()) {
            RickyMortyCharacter characterFinded = character.get();
            String url = ADDRESS_CHARACTER + "?name=" + characterFinded.getName().toLowerCase().replace(" ", "+");
            String json = this.consumerApi.getData(url);
            RickyMortyCharacterListRecord list = this.translateData.getData(json, RickyMortyCharacterListRecord.class);
            List<RickyMortyEpisode> episodes = new ArrayList<>();
            for (RickyMortyCharacterRecord record : list.characters()) {
                for (String urlEpisode : record.episodes()) {
                    json = this.consumerApi.getData(urlEpisode);
                    RickyMortyEpisodeRecord episodeRecord = this.translateData.getData(json, RickyMortyEpisodeRecord.class);
                    RickyMortyEpisode episode = new RickyMortyEpisode(episodeRecord);
                    this.episodeRepository.save(episode);
                    episodes.add(episode);
                }
            }
            characterFinded.setEpisodes(episodes);
            this.characterRepository.save(characterFinded);
        } else {
            System.out.println("Character not found");
        }
    }

    private void showEpisodesAndCharacters(RickyMortyLocationRecord rickyMortyLocationRecord) {
        List<RickyMortyCharacterRecord> residents = new ArrayList<>();
        List<RickyMortyEpisodeRecord> episodes = new ArrayList<>();

        rickyMortyLocationRecord.residents().forEach(c -> {
            var temp = consumerApi.getData(c);
            RickyMortyCharacterRecord character = translateData.getData(temp, RickyMortyCharacterRecord.class);
            character.episodes().forEach(e -> {
                var tempEpisode = consumerApi.getData(e);
                RickyMortyEpisodeRecord episode = translateData.getData(tempEpisode, RickyMortyEpisodeRecord.class);
                episodes.add(episode);
                showProgress();
            });
            residents.add(character);
            showProgress();
        });

        residents.forEach(System.out::println);
        episodes.forEach(System.out::println);

        List<String> urlEpisodes = residents.stream()
                .flatMap(c -> c.episodes().stream())
                .collect(Collectors.toList());

        System.out.println(urlEpisodes);
    }

    private void showProgress() {
        System.out.print(".");
    }

    private void topFiveCharacter(List<RickyMortyCharacterRecord> residents) {
        System.out.println("Top 5 character with more episodes...");

        residents.stream()
                .map(c -> Map.of(c.name(), c.episodes().size()))
                .sorted(Comparator.comparing(c -> c.values().iterator().next(), Comparator.reverseOrder()))
                .limit(5)
                .forEach(System.out::println);
    }

    private void findCharacterByName() {
        System.out.println("Type a name: ");
        this.scanner.reset();
        this.scanner.nextLine();
        String name = this.scanner.nextLine();

        Optional<RickyMortyCharacter> character = this.characterRepository.findByNameContainingIgnoreCase(name);

        if (character.isPresent()) {
            System.out.println(character.get().toStringFull());
        } else {
            System.out.println("Character not found!");
        }
    }
}
