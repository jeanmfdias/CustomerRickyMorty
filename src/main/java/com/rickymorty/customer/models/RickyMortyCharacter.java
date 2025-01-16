package com.rickymorty.customer.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RickyMortyCharacter(@JsonAlias("name") String name,
                                  @JsonAlias("status") String status,
                                  @JsonAlias("species") String species,
                                  @JsonAlias("episode") List<String> episodes) {
}
