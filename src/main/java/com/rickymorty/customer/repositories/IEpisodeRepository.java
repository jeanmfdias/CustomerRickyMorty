package com.rickymorty.customer.repositories;

import com.rickymorty.customer.models.RickyMortyEpisode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEpisodeRepository extends JpaRepository<RickyMortyEpisode, Long> {
}
