package com.rickymorty.customer.repositories;

import com.rickymorty.customer.models.RickyMortyCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICharacterRepository extends JpaRepository<RickyMortyCharacter, Long> {
}
