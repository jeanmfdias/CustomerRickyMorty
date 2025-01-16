package com.rickymorty.customer;

import com.rickymorty.customer.models.RickyMortyCharacter;
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

		var json = consumerApi.getData("https://rickandmortyapi.com/api/character/2");
		System.out.println(json);

		RickyMortyCharacter character = translateData.getData(json, RickyMortyCharacter.class);
		System.out.println(character);
	}
}
