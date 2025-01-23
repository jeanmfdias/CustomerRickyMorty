package com.rickymorty.customer.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "characters")
public class RickyMortyCharacter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String species;

    @ManyToMany
    @JoinTable(
            name = "characters_episodes",
            joinColumns = @JoinColumn(name = "character_id"),
            inverseJoinColumns = @JoinColumn(name = "episode_id")
    )
    private List<RickyMortyEpisode> episodes;

    @ManyToOne
    private RickyMortyLocation location;

    public RickyMortyCharacter() {

    }

    public RickyMortyCharacter(RickyMortyCharacterRecord character) {
        this.name = character.name();
        this.status = character.status();
        this.species = character.species();
//        this.episodes = character.episodes();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<RickyMortyEpisode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<RickyMortyEpisode> episodes) {
        this.episodes = episodes;
    }

    public RickyMortyLocation getLocation() {
        return location;
    }

    public void setLocation(RickyMortyLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "#%d | %s".formatted(this.getId(), this.getName());
    }
}
