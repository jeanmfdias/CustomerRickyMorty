package com.rickymorty.customer.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RickyMortyEpisode(@JsonAlias("name") String name,
                                @JsonProperty("air_date") String airDate,
                                @JsonAlias("episode") String episode) {
}
