package com.rickymorty.customer.controllers;

import com.rickymorty.customer.dto.RickAndMortyEpisodesAndCharactersDto;
import com.rickymorty.customer.dto.RickAndMortyLocationDTO;
import com.rickymorty.customer.models.RickAndMortyCharacterRecord;
import com.rickymorty.customer.models.RickAndMortyEpisodeRecord;
import com.rickymorty.customer.services.RickAndMortyLocationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RickAndMortyLocationController.class)
class RickAndMortyLocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RickAndMortyLocationService service;

    @Test
    void getLocations_returnsOkWithList() throws Exception {
        RickAndMortyLocationDTO dto = new RickAndMortyLocationDTO(1L, "Earth", "Planet", "C-137",
                List.of(Map.of("id", 1L, "name", "Rick")));
        when(service.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Earth"))
                .andExpect(jsonPath("$[0].type").value("Planet"))
                .andExpect(jsonPath("$[0].residents[0].name").value("Rick"));
    }

    @Test
    void getLocations_whenEmpty_returnsEmptyList() throws Exception {
        when(service.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getEpisodesAndCharacters_returnsOkWithResidentsAndEpisodes() throws Exception {
        var dto = new RickAndMortyEpisodesAndCharactersDto(
                List.of(new RickAndMortyCharacterRecord("Rick", "Alive", "Human", List.of("ep-url"))),
                List.of(new RickAndMortyEpisodeRecord("Pilot", "December 2, 2013", "S01E01")));
        when(service.getEpisodesAndCharacters(1)).thenReturn(dto);

        mockMvc.perform(get("/locations/episodes-and-characters/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.residents[0].name").value("Rick"))
                .andExpect(jsonPath("$.episodes[0].name").value("Pilot"));
    }

    @Test
    void saveLocationsFromWeb_whenSuccess_returns201WithMessage() throws Exception {
        when(service.saveLocationFromWeb(1)).thenReturn(true);

        mockMvc.perform(post("/locations/save-from-web/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Location created with success"));
    }

    @Test
    void saveLocationsFromWeb_whenFailure_returns201WithErrorMessage() throws Exception {
        when(service.saveLocationFromWeb(1)).thenReturn(false);

        mockMvc.perform(post("/locations/save-from-web/1"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Error on save location"));
    }
}
