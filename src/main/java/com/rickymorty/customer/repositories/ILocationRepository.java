package com.rickymorty.customer.repositories;

import com.rickymorty.customer.models.RickAndMortyLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILocationRepository extends JpaRepository<RickAndMortyLocation, Long> {
}
