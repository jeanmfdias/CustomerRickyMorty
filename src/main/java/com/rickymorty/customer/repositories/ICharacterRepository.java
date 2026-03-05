package com.rickymorty.customer.repositories;

import com.rickymorty.customer.models.RickAndMortyCharacter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICharacterRepository extends JpaRepository<RickAndMortyCharacter, Long> {
    Optional<RickAndMortyCharacter> findByNameContainingIgnoreCase(String name);

    List<RickAndMortyCharacter> findByLocationNameContainingIgnoreCase(String name);

    Long countByLocationId(Long id);

    List<RickAndMortyCharacter> findByLocationId(Long locationId);

    Page<RickAndMortyCharacter> findByLocationId(Long locationId, Pageable pageable);

    Page<RickAndMortyCharacter> findByLocationIdAndNameContainingIgnoreCase(Long locationId, String name, Pageable pageable);
}
