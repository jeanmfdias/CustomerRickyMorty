package com.rickymorty.customer.view;

import com.rickymorty.customer.models.RickyMortyCharacter;
import com.rickymorty.customer.models.RickyMortyEpisode;
import com.rickymorty.customer.models.RickyMortyLocation;
import com.rickymorty.customer.services.ConsumerApi;
import com.rickymorty.customer.services.TranslateData;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private Scanner scanner = new Scanner(System.in);

    private final String ADDRESS = "https://rickandmortyapi.com/api/location/";

    private ConsumerApi consumerApi = new ConsumerApi();

    TranslateData translateData = new TranslateData();

    public void showMenu() {
        scanner.reset();
        System.out.print("Type location ID: ");
        int locationId = scanner.nextInt();
        String fullAddress = ADDRESS + locationId;

        var json = consumerApi.getData(fullAddress);

        RickyMortyLocation location = translateData.getData(json, RickyMortyLocation.class);
        System.out.println(location);

        List<RickyMortyCharacter> residents = new ArrayList<>();
        List<RickyMortyEpisode> episodes = new ArrayList<>();

        location.residents().forEach(c -> {
            var temp = consumerApi.getData(c);
            RickyMortyCharacter character = translateData.getData(temp, RickyMortyCharacter.class);
            character.episodes().forEach(e -> {
                var tempEpisode = consumerApi.getData(e);
                RickyMortyEpisode episode = translateData.getData(tempEpisode, RickyMortyEpisode.class);
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

    private void topFiveCharacter(List<RickyMortyCharacter> residents) {
        System.out.println("Top 5 character with more episodes...");

        residents.stream()
                .map(c -> Map.of(c.name(), c.episodes().size()))
                .sorted(Comparator.comparing(c -> c.values().iterator().next(), Comparator.reverseOrder()))
                .limit(5)
                .forEach(System.out::println);
    }
}
