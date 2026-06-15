package com.rickymorty.customer.dto;

import com.rickymorty.customer.models.RickAndMortyCharacterRecord;
import com.rickymorty.customer.models.RickAndMortyEpisodeRecord;

import java.util.List;

public record RickAndMortyEpisodesAndCharactersDto(List<RickAndMortyCharacterRecord> residents,
                                                   List<RickAndMortyEpisodeRecord> episodes) {

}