package com.rickymorty.customer.services;

import com.rickymorty.customer.models.RickAndMortyEpisode;
import com.rickymorty.customer.repositories.IEpisodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RickAndMortyEpisodeServiceTest {

    @Mock private IEpisodeRepository episodeRepository;

    @InjectMocks
    private RickAndMortyEpisodeService service;

    @Test
    void getAll_withName_callsFilteredRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RickAndMortyEpisode> page = new PageImpl<>(List.of());
        when(episodeRepository.findByNameContainingIgnoreCaseOrderByAirDateAsc("Pilot", pageable)).thenReturn(page);

        service.getAll("Pilot", pageable);

        verify(episodeRepository).findByNameContainingIgnoreCaseOrderByAirDateAsc("Pilot", pageable);
        verify(episodeRepository, never()).findAllByOrderByAirDateAsc(any());
    }

    @Test
    void getAll_withNullName_callsUnfilteredRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RickAndMortyEpisode> page = new PageImpl<>(List.of());
        when(episodeRepository.findAllByOrderByAirDateAsc(pageable)).thenReturn(page);

        service.getAll(null, pageable);

        verify(episodeRepository).findAllByOrderByAirDateAsc(pageable);
        verify(episodeRepository, never()).findByNameContainingIgnoreCaseOrderByAirDateAsc(any(), any());
    }

    @Test
    void getAll_withBlankName_callsUnfilteredRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RickAndMortyEpisode> page = new PageImpl<>(List.of());
        when(episodeRepository.findAllByOrderByAirDateAsc(pageable)).thenReturn(page);

        service.getAll("  ", pageable);

        verify(episodeRepository).findAllByOrderByAirDateAsc(pageable);
        verify(episodeRepository, never()).findByNameContainingIgnoreCaseOrderByAirDateAsc(any(), any());
    }

    @Test
    void getAll_returnsPageFromRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        RickAndMortyEpisode episode = new RickAndMortyEpisode();
        Page<RickAndMortyEpisode> page = new PageImpl<>(List.of(episode));
        when(episodeRepository.findAllByOrderByAirDateAsc(pageable)).thenReturn(page);

        Page<RickAndMortyEpisode> result = service.getAll(null, pageable);

        assertThat(result.getContent()).containsExactly(episode);
    }
}
