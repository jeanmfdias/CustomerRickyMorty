package com.rickymorty.customer;

import com.rickymorty.customer.models.RickyMortyCharacter;
import com.rickymorty.customer.models.RickyMortyEpisode;
import com.rickymorty.customer.models.RickyMortyLocation;
import com.rickymorty.customer.services.ConsumerApi;
import com.rickymorty.customer.services.TranslateData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CustomerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumerApi consumerApi = new ConsumerApi();
		TranslateData translateData = new TranslateData();

		var json = consumerApi.getData("https://rickandmortyapi.com/api/location/3");
		RickyMortyLocation location = translateData.getData(json, RickyMortyLocation.class);
		System.out.println(location);

		List<RickyMortyCharacter> residents = new ArrayList<>();
		List<RickyMortyEpisode> episodes = new ArrayList<>();
		for (int i = 0; i < location.residents().size(); i++) {
			json = consumerApi.getData(location.residents().get(i));
			RickyMortyCharacter character = translateData.getData(json, RickyMortyCharacter.class);
			System.out.println(character);
			for (int j = 0; j < character.episodes().size(); j++) {
				json = consumerApi.getData(character.episodes().get(j));
				RickyMortyEpisode episode = translateData.getData(json, RickyMortyEpisode.class);
				System.out.println(episode);
				episodes.add(episode);
			}
			residents.add(character);
		}

		System.out.println(residents);
		System.out.println(episodes);
	}
}
