package com.rickymorty.customer.controllers;

import com.rickymorty.customer.dto.RickAndMortyEpisodeDto;
import com.rickymorty.customer.services.RickAndMortyEpisodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RickAndMortyEpisodeController.class)
class RickAndMortyEpisodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RickAndMortyEpisodeService service;

    @Test
    void getAll_withEpisodes_returns200() throws Exception {
        var episode = buildEpisode(1L, "Pilot", LocalDate.of(2013, 12, 2), 1, 1);
        var page = new PageImpl<>(List.of(episode), PageRequest.of(0, 10), 1);
        when(service.getAll(isNull(), any())).thenReturn(new PageImpl<>(List.of(buildEpisodeEntity())));

        mockMvc.perform(get("/episodes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Pilot"));
    }

    @Test
    void getAll_withNoEpisodes_returns404() throws Exception {
        when(service.getAll(isNull(), any())).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/episodes"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_withNameFilter_returnsFilteredEpisodes() throws Exception {
        when(service.getAll(eq("Pilot"), any())).thenReturn(new PageImpl<>(List.of(buildEpisodeEntity())));

        mockMvc.perform(get("/episodes").param("name", "Pilot"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Pilot"));
    }

    @Test
    void getAll_withNameFilter_whenNoMatch_returns404() throws Exception {
        when(service.getAll(eq("unknown"), any())).thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/episodes").param("name", "unknown"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_withPagination_passesParamsToService() throws Exception {
        when(service.getAll(isNull(), any())).thenReturn(new PageImpl<>(List.of(buildEpisodeEntity())));

        mockMvc.perform(get("/episodes").param("page", "0").param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    private com.rickymorty.customer.models.RickAndMortyEpisode buildEpisodeEntity() {
        com.rickymorty.customer.models.RickAndMortyEpisode episode = new com.rickymorty.customer.models.RickAndMortyEpisode();
        episode.setId(1L);
        episode.setName("Pilot");
        episode.setAirDate(LocalDate.of(2013, 12, 2));
        episode.setSeason(1);
        episode.setEpisodeNumber(1);
        return episode;
    }

    private RickAndMortyEpisodeDto buildEpisode(Long id, String name, LocalDate airDate, int season, int episodeNumber) {
        return new RickAndMortyEpisodeDto(id, name, airDate, season, episodeNumber);
    }
}
