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
            System.out.println("4 - Search All Episodes by Character");
            System.out.println("5 - List All Episodes");
            System.out.println("6 - Search Character by name");
            System.out.println("7 - Search Character by Location name");
            System.out.println("0 - Exit");
            option = scanner.nextInt();

            switch (option) {
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

    private void listAllEpisodes() {
        List<RickAndMortyEpisode> episodes = this.episodeRepository.findAll();
        episodes.stream()
                .sorted(Comparator.comparing(RickAndMortyEpisode::getAirDate))
                .forEach(System.out::println);
    }

    private void searchEpisodesByCharacter() {
        List<RickAndMortyLocation> locations = this.locationRepository.findAll();
        locations.forEach(System.out::println);

        System.out.print("Choice a character ID: ");
        scanner.reset();
        Long characterId = scanner.nextLong();

        Optional<RickAndMortyCharacter> character = locations.stream()
                .flatMap(l -> l.getResidents().stream())
                .filter(c -> c.getId().equals(characterId))
                .findFirst();

        if (character.isPresent()) {
            RickAndMortyCharacter characterFinded = character.get();
            String url = ADDRESS_CHARACTER + "?name=" + characterFinded.getName().toLowerCase().replace(" ", "+");
            String json = this.consumerApi.getData(url);
            RickAndMortyCharacterListRecord list = this.translateData.getData(json, RickAndMortyCharacterListRecord.class);
            List<RickAndMortyEpisode> episodes = new ArrayList<>();
            for (RickAndMortyCharacterRecord record : list.characters()) {
                for (String urlEpisode : record.episodes()) {
                    json = this.consumerApi.getData(urlEpisode);
                    RickAndMortyEpisodeRecord episodeRecord = this.translateData.getData(json, RickAndMortyEpisodeRecord.class);
                    RickAndMortyEpisode episode = new RickAndMortyEpisode(episodeRecord);
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

    private void showEpisodesAndCharacters(RickAndMortyLocationRecord rickAndMortyLocationRecord) {
        List<RickAndMortyCharacterRecord> residents = new ArrayList<>();
        List<RickAndMortyEpisodeRecord> episodes = new ArrayList<>();

        rickAndMortyLocationRecord.residents().forEach(c -> {
            var temp = consumerApi.getData(c);
            RickAndMortyCharacterRecord character = translateData.getData(temp, RickAndMortyCharacterRecord.class);
            character.episodes().forEach(e -> {
                var tempEpisode = consumerApi.getData(e);
                RickAndMortyEpisodeRecord episode = translateData.getData(tempEpisode, RickAndMortyEpisodeRecord.class);
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

    private void topFiveCharacter(List<RickAndMortyCharacterRecord> residents) {
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

        Optional<RickAndMortyCharacter> character = this.characterRepository.findByNameContainingIgnoreCase(name);

        if (character.isPresent()) {
            System.out.println(character.get().toStringFull());
        } else {
            System.out.println("Character not found!");
        }
    }
}
