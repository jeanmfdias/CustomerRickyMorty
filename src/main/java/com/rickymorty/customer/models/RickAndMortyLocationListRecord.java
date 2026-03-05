package com.rickymorty.customer.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RickAndMortyLocationListRecord(@JsonAlias("results") List<RickAndMortyLocationRecord> locations) {
}
