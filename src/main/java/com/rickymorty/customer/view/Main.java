package com.rickymorty.customer.view;

import com.rickymorty.customer.models.*;
import com.rickymorty.customer.repositories.ICharacterRepository;
import com.rickymorty.customer.repositories.IEpisodeRepository;
import com.rickymorty.customer.repositories.ILocationRepository;
import com.rickymorty.customer.services.ConsumerApi;
import com.rickymorty.customer.services.TranslateData;

import java.util.*;

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
            System.out.println("0 - Exit");
            option = scanner.nextInt();

            switch (option) {
                case 0:
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

}
