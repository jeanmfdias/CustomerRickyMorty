package com.rickymorty.customer.models;

import java.util.List;

public class RMCharacter {
    private String name;
    private String status;
    private String species;
    private List<String> episodes;

    public RMCharacter(RickyMortyCharacter character) {
        this.name = character.name();
        this.status = character.status();
        this.species = character.species();
        this.episodes = character.episodes();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public List<String> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<String> episodes) {
        this.episodes = episodes;
    }
}
