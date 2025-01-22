package com.rickymorty.customer.view;

import com.rickymorty.customer.models.*;
import com.rickymorty.customer.repositories.ILocationRepository;
import com.rickymorty.customer.services.ConsumerApi;
import com.rickymorty.customer.services.TranslateData;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final Scanner scanner = new Scanner(System.in);

    private final String ADDRESS_LOCATION = "https://rickandmortyapi.com/api/location/";

    private final ConsumerApi consumerApi = new ConsumerApi();

    private final TranslateData translateData = new TranslateData();

    private final ILocationRepository locationRepository;

    public Main(ILocationRepository iLocationRepository) {
        this.locationRepository = iLocationRepository;
    }

    public void showMenu() {
        scanner.reset();
        int option = -1;
        while (option != 0) {
            System.out.println("Choose a option: ");
            System.out.println("1 - Search Location");
            System.out.println("2 - List All Locations");
            System.out.println("3 - Search All Character by Location");
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

    private void searchCharacterByLocation() {
        List<RickyMortyLocation> locations = this.locationRepository.findAll();
        locations.forEach(System.out::println);

        System.out.print("Choice a name location: ");
        scanner.nextLine();
        scanner.reset();
        String locationName = scanner.nextLine();

        Optional<RickyMortyLocation> location = locations.stream()
                .filter(l -> l.getName().toLowerCase().contains(locationName.toLowerCase()))
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
}
