package com.rickymorty.customer.view;

import com.rickymorty.customer.models.RickyMortyCharacter;
import com.rickymorty.customer.models.RickyMortyEpisode;
import com.rickymorty.customer.models.RickyMortyLocation;
import com.rickymorty.customer.services.ConsumerApi;
import com.rickymorty.customer.services.TranslateData;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            });
            residents.add(character);
        });

        residents.forEach(System.out::println);
        episodes.forEach(System.out::println);
    }
}
