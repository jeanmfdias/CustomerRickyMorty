package com.rickymorty.customer.dto;

import com.rickymorty.customer.models.RickyMortyCharacter;

public record RickyMortyCharacterDto(Long id, String name, String status, String species) {

    public RickyMortyCharacterDto(RickyMortyCharacter character) {
        this(character.getId(), character.getName(), character.getStatus(), character.getSpecies());
    }

}
