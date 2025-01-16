package com.rickymorty.customer.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RickyMortyLocation(@JsonAlias("name") String name,
                                 @JsonAlias("type") String type,
                                 @JsonAlias("dimension") String dimension,
                                 @JsonAlias("residents") List<String> residents) {
}
