package com.rickymorty.customer.dto;

import com.rickymorty.customer.models.RickAndMortyCharacter;

public record RickAndMortyTopCharacterDto(String name, int episodes) {

    public RickAndMortyTopCharacterDto(RickAndMortyCharacter character) {
        this(character.getName(), character.getEpisodes().size());
    }

}