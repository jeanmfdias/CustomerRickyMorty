package com.rickymorty.customer.view;

import com.rickymorty.customer.models.RickyMortyLocation;
import com.rickymorty.customer.models.RickyMortyCharacterRecord;
import com.rickymorty.customer.models.RickyMortyEpisodeRecord;
import com.rickymorty.customer.models.RickyMortyLocationRecord;
import com.rickymorty.customer.repositories.ILocationRepository;
import com.rickymorty.customer.services.ConsumerApi;
import com.rickymorty.customer.services.TranslateData;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private final Scanner scanner = new Scanner(System.in);

    private final String ADDRESS = "https://rickandmortyapi.com/api/location/";

    private final ConsumerApi consumerApi = new ConsumerApi();

    private final TranslateData translateData = new TranslateData();

    private final ILocationRepository locationRepository;

    public Main(ILocationRepository iLocationRepository) {
        this.locationRepository = iLocationRepository;
    }

    public void showMenu() {
        scanner.reset();
        System.out.print("Type location ID: ");
        int locationId = scanner.nextInt();
        String fullAddress = ADDRESS + locationId;

        var json = consumerApi.getData(fullAddress);

        RickyMortyLocationRecord rmLocation = translateData.getData(json, RickyMortyLocationRecord.class);
        RickyMortyLocation location = new RickyMortyLocation(rmLocation);
        this.locationRepository.save(location);

        List<RickyMortyCharacterRecord> residents = new ArrayList<>();
        List<RickyMortyEpisodeRecord> episodes = new ArrayList<>();

        rmLocation.residents().forEach(c -> {
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

        topFiveCharacter(residents);
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
