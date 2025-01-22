package com.rickymorty.customer;

import com.rickymorty.customer.repositories.ILocationRepository;
import com.rickymorty.customer.view.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CustomerApplication implements CommandLineRunner {
	@Autowired
	private ILocationRepository locationRepository;

	public static void main(String[] args) {
		SpringApplication.run(CustomerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(locationRepository);
		main.showMenu();
	}
}
