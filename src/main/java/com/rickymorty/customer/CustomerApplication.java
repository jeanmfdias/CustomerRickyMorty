package com.rickymorty.customer;

import com.rickymorty.customer.service.ConsumerApi;
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
		var json = consumerApi.getData("https://rickandmortyapi.com/api/character");
		System.out.println(json);
	}
}
