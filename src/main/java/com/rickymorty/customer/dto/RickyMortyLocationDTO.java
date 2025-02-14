package com.rickymorty.customer.dto;

import com.rickymorty.customer.models.RickyMortyCharacter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.List;

public record RickyMortyLocationDTO(Long id,
                                    String name,
                                    String type,
                                    String dimension,
                                    List<RickyMortyCharacter> residents) {
}
