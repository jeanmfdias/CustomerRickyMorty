package com.rickymorty.customer.dto;

import com.rickymorty.customer.models.RickAndMortyCharacter;

public record RickAndMortyCharacterDto(Long id, String name, String status, String species) {

    public RickAndMortyCharacterDto(RickAndMortyCharacter character) {
        this(character.getId(), character.getName(), character.getStatus(), character.getSpecies());
    }

}
