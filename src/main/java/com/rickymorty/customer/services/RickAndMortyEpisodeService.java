package com.rickymorty.customer.services;

import com.rickymorty.customer.models.RickAndMortyEpisode;
import com.rickymorty.customer.repositories.IEpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RickAndMortyEpisodeService {

    @Autowired
    private IEpisodeRepository episodeRepository;

    public Page<RickAndMortyEpisode> getAll(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            return this.episodeRepository.findByNameContainingIgnoreCaseOrderByAirDateAsc(name, pageable);
        }
        return this.episodeRepository.findAllByOrderByAirDateAsc(pageable);
    }
}
