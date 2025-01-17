package com.rickymorty.customer.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.OptionalInt;

public class RMEpisode {
    private String name;
    private LocalDate airDate;
    private int episodeNumber;
    private int season;

    public RMEpisode(RickyMortyEpisode episode) {
        this.name = episode.name();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        this.airDate = LocalDate.parse(episode.airDate(), formatter);

        this.episodeNumber = OptionalInt.of(Integer.valueOf(episode.episode().substring(1, 2))).orElse(0);
        this.season = OptionalInt.of(Integer.valueOf(episode.episode().substring(4, 5))).orElse(0);
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
}
