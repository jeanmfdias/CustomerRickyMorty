package com.rickymorty.customer.controllers;

import com.rickymorty.customer.dto.RickAndMortyCharacterDto;
import com.rickymorty.customer.services.RickAndMortyCharacterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RickAndMortyCharacterController.class)
class RickAndMortyCharacterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RickAndMortyCharacterService service;

    @Test
    void saveCharacterByLocation_whenSuccess_returns201() throws Exception {
        when(service.saveCharacterByLocation(1L)).thenReturn(true);
        when(service.getCountByLocationId(1L)).thenReturn(3L);

        mockMvc.perform(post("/characters/save-by-location/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Save 3 character(s) to location (1) with success"));
    }

    @Test
    void saveCharacterByLocation_whenFailure_returns400() throws Exception {
        when(service.saveCharacterByLocation(1L)).thenReturn(false);

        mockMvc.perform(post("/characters/save-by-location/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error on save characters by location"));
    }

    @Test
    void getByLocationName_returnsList() throws Exception {
        RickAndMortyCharacterDto dto = new RickAndMortyCharacterDto(1L, "Rick", "Alive", "Human");
        when(service.getAllByLocationName("earth")).thenReturn(List.of());
        when(service.getAllByLocationName("earth")).thenAnswer(inv -> List.of());

        mockMvc.perform(get("/characters/location-name").param("name", "earth"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getByLocationId_returnsPage() throws Exception {
        RickAndMortyCharacterDto dto = new RickAndMortyCharacterDto(1L, "Rick", "Alive", "Human");
        var page = new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);
        when(service.getAllByLocationId(eq(1L), isNull(), any())).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/characters/location/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getByLocationId_withNameFilter_returnsFilteredPage() throws Exception {
        when(service.getAllByLocationId(eq(1L), eq("Rick"), any())).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/characters/location/1").param("name", "Rick"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void getByName_whenFound_returns200() throws Exception {
        RickAndMortyCharacterDto dto = new RickAndMortyCharacterDto(1L, "Rick", "Alive", "Human");
        when(service.findByName("Rick")).thenReturn(Optional.of(buildCharacter(1L, "Rick", "Alive", "Human")));

        mockMvc.perform(get("/characters/name").param("name", "Rick"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rick"))
                .andExpect(jsonPath("$.status").value("Alive"));
    }

    @Test
    void getByName_whenNotFound_returns404() throws Exception {
        when(service.findByName("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(get("/characters/name").param("name", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTopFiveByEpisodes_returnsListWithNameAndEpisodeCount() throws Exception {
        com.rickymorty.customer.models.RickAndMortyCharacter rick = buildCharacter(1L, "Rick", "Alive", "Human");
        rick.setEpisodes(List.of(new com.rickymorty.customer.models.RickAndMortyEpisode(),
                new com.rickymorty.customer.models.RickAndMortyEpisode()));
        when(service.getTopFiveByEpisodes()).thenReturn(List.of(rick));

        mockMvc.perform(get("/characters/top-five-episodes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Rick"))
                .andExpect(jsonPath("$[0].episodes").value(2));
    }

    private com.rickymorty.customer.models.RickAndMortyCharacter buildCharacter(Long id, String name, String status, String species) {
        com.rickymorty.customer.models.RickAndMortyCharacter c = new com.rickymorty.customer.models.RickAndMortyCharacter();
        c.setId(id);
        c.setName(name);
        c.setStatus(status);
        c.setSpecies(species);
        return c;
    }
}
