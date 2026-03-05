package com.rickymorty.customer.repositories;

import com.rickymorty.customer.models.RickAndMortyEpisode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEpisodeRepository extends JpaRepository<RickAndMortyEpisode, Long> {
}
