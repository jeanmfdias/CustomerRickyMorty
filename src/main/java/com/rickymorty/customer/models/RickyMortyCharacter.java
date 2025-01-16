package com.rickymorty.customer.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RickyMortyCharacter(@JsonAlias("name") String name,
                                  @JsonAlias("status") String status,
                                  @JsonAlias("species") String species) {
}
