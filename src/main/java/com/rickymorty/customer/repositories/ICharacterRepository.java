package com.rickymorty.customer.repositories;

import com.rickymorty.customer.models.RickyMortyCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ICharacterRepository extends JpaRepository<RickyMortyCharacter, Long> {
    Optional<RickyMortyCharacter> findByNameContainingIgnoreCase(String name);

    List<RickyMortyCharacter> findByLocationNameContainingIgnoreCase(String name);

    Long countByLocationId(Long id);
}
