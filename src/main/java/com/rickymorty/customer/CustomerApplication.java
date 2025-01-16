package com.rickymorty.customer;

import com.rickymorty.customer.models.RickyMortyCharacter;
import com.rickymorty.customer.models.RickyMortyEpisode;
import com.rickymorty.customer.models.RickyMortyLocation;
import com.rickymorty.customer.services.ConsumerApi;
import com.rickymorty.customer.services.TranslateData;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CustomerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumerApi consumerApi = new ConsumerApi();
		TranslateData translateData = new TranslateData();

		var json = consumerApi.getData("https://rickandmortyapi.com/api/character/2");
		RickyMortyCharacter character = translateData.getData(json, RickyMortyCharacter.class);
		System.out.println(character);

		json = consumerApi.getData("https://rickandmortyapi.com/api/location/3");
		RickyMortyLocation location = translateData.getData(json, RickyMortyLocation.class);
		System.out.println(location);

		json = consumerApi.getData("https://rickandmortyapi.com/api/episode/28");
		RickyMortyEpisode episode = translateData.getData(json, RickyMortyEpisode.class);
		System.out.println(episode);
	}
}
