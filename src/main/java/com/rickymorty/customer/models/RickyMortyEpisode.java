package com.rickymorty.customer.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.OptionalInt;

@Entity
@Table(name = "episodes")
public class RickyMortyEpisode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private LocalDate airDate;

    @Column
    private int episodeNumber;

    @Column
    private int season;

    @ManyToMany(mappedBy = "episodes")
    private List<RickyMortyCharacter> characters;

    public RickyMortyEpisode() {

    }

    public RickyMortyEpisode(RickyMortyEpisodeRecord episode) {
        this.name = episode.name();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        this.airDate = LocalDate.parse(episode.airDate(), formatter);

        this.episodeNumber = OptionalInt.of(Integer.valueOf(episode.episode().substring(1, 2))).orElse(0);
        this.season = OptionalInt.of(Integer.valueOf(episode.episode().substring(4, 5))).orElse(0);
    }

    public Long getId() {
        return this.id;
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

    public LocalDate getAirDate() {
        return airDate;
    }

    public void setAirDate(LocalDate airDate) {
        this.airDate = airDate;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public int getSeason() {
        return season;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public List<RickyMortyCharacter> getCharacters() {
        return characters;
    }

    public void setCharacters(List<RickyMortyCharacter> characters) {
        this.characters = characters;
    }
}
