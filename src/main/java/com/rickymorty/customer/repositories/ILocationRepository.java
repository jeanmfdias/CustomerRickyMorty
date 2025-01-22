package com.rickymorty.customer.repositories;

import com.rickymorty.customer.models.RickyMortyLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ILocationRepository extends JpaRepository<RickyMortyLocation, Long> {
}
