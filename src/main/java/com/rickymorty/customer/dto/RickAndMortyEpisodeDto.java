package com.rickymorty.customer.dto;

import com.rickymorty.customer.models.RickAndMortyEpisode;

import java.time.LocalDate;

public record RickAndMortyEpisodeDto(Long id, String name, LocalDate airDate, int season, int episodeNumber) {

    public RickAndMortyEpisodeDto(RickAndMortyEpisode episode) {
        this(episode.getId(), episode.getName(), episode.getAirDate(), episode.getSeason(), episode.getEpisodeNumber());
    }

}
