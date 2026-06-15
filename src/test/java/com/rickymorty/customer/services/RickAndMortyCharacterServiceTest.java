package com.rickymorty.customer.services;

import com.rickymorty.customer.models.*;
import com.rickymorty.customer.repositories.ICharacterRepository;
import com.rickymorty.customer.repositories.IEpisodeRepository;
import com.rickymorty.customer.repositories.ILocationRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RickAndMortyCharacterServiceTest {

    @Mock private ILocationRepository locationRepository;
    @Mock private ICharacterRepository characterRepository;
    @Mock private IEpisodeRepository episodeRepository;
    @Mock private ConsumerApi consumerApi;
    @Mock private TranslateData translateData;

    @InjectMocks
    private RickAndMortyCharacterService service;

    @Test
    void saveCharacterByLocation_whenLocationNotFound_returnsFalse() {
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(service.saveCharacterByLocation(1L)).isFalse();
        verify(locationRepository, never()).save(any());
    }

    @Test
    void saveCharacterByLocation_whenLocationFound_savesAndReturnsTrue() {
        RickAndMortyLocation location = new RickAndMortyLocation();
        location.setName("Earth");

        RickAndMortyLocationListRecord listRecord = new RickAndMortyLocationListRecord(
                List.of(new RickAndMortyLocationRecord("Earth", "Planet", "C-137", List.of()))
        );

        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));
        when(consumerApi.getData(anyString())).thenReturn("{}");
        when(translateData.getData(eq("{}"), eq(RickAndMortyLocationListRecord.class))).thenReturn(listRecord);

        assertThat(service.saveCharacterByLocation(1L)).isTrue();
        verify(locationRepository).save(location);
    }

    @Test
    void saveEpisodesByCharacter_whenCharacterNotFound_returnsFalse() {
        when(characterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThat(service.saveEpisodesByCharacter(1L)).isFalse();
        verify(characterRepository, never()).save(any());
        verify(episodeRepository, never()).save(any());
    }

    @Test
    void saveEpisodesByCharacter_whenCharacterFound_savesEpisodesAndReturnsTrue() {
        RickAndMortyCharacter character = new RickAndMortyCharacter();
        character.setName("Rick");

        RickAndMortyCharacterListRecord listRecord = new RickAndMortyCharacterListRecord(
                List.of(new RickAndMortyCharacterRecord("Rick", "Alive", "Human", List.of("ep-url")))
        );
        RickAndMortyEpisodeRecord episodeRecord = new RickAndMortyEpisodeRecord("Pilot", "December 2, 2013", "S01E01");

        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        when(consumerApi.getData(anyString())).thenReturn("{}");
        when(translateData.getData(eq("{}"), eq(RickAndMortyCharacterListRecord.class))).thenReturn(listRecord);
        when(translateData.getData(eq("{}"), eq(RickAndMortyEpisodeRecord.class))).thenReturn(episodeRecord);

        assertThat(service.saveEpisodesByCharacter(1L)).isTrue();
        verify(episodeRepository).save(any(RickAndMortyEpisode.class));
        verify(characterRepository).save(character);
    }

    @Test
    void getCountByLocationId_returnsDelegatedValue() {
        when(characterRepository.countByLocationId(1L)).thenReturn(5L);

        assertThat(service.getCountByLocationId(1L)).isEqualTo(5L);
    }

    @Test
    void getAllByLocationName_returnsDelegatedList() {
        RickAndMortyCharacter character = new RickAndMortyCharacter();
        when(characterRepository.findByLocationNameContainingIgnoreCase("earth")).thenReturn(List.of(character));

        assertThat(service.getAllByLocationName("earth")).containsExactly(character);
    }

    @Test
    void findByName_whenFound_returnsOptionalWithCharacter() {
        RickAndMortyCharacter character = new RickAndMortyCharacter();
        when(characterRepository.findByNameContainingIgnoreCase("Rick")).thenReturn(Optional.of(character));

        assertThat(service.findByName("Rick")).contains(character);
    }

    @Test
    void findByName_whenNotFound_returnsEmpty() {
        when(characterRepository.findByNameContainingIgnoreCase("unknown")).thenReturn(Optional.empty());

        assertThat(service.findByName("unknown")).isEmpty();
    }

    @Test
    void getAllByLocationId_withName_callsFilteredRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RickAndMortyCharacter> page = new PageImpl<>(List.of());
        when(characterRepository.findByLocationIdAndNameContainingIgnoreCase(1L, "Rick", pageable)).thenReturn(page);

        service.getAllByLocationId(1L, "Rick", pageable);

        verify(characterRepository).findByLocationIdAndNameContainingIgnoreCase(1L, "Rick", pageable);
        verify(characterRepository, never()).findByLocationId(anyLong(), any());
    }

    @Test
    void getAllByLocationId_withNullName_callsUnfilteredRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RickAndMortyCharacter> page = new PageImpl<>(List.of());
        when(characterRepository.findByLocationId(1L, pageable)).thenReturn(page);

        service.getAllByLocationId(1L, null, pageable);

        verify(characterRepository).findByLocationId(1L, pageable);
        verify(characterRepository, never()).findByLocationIdAndNameContainingIgnoreCase(anyLong(), any(), any());
    }

    @Test
    void getAllByLocationId_withBlankName_callsUnfilteredRepository() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RickAndMortyCharacter> page = new PageImpl<>(List.of());
        when(characterRepository.findByLocationId(1L, pageable)).thenReturn(page);

        service.getAllByLocationId(1L, "   ", pageable);

        verify(characterRepository).findByLocationId(1L, pageable);
        verify(characterRepository, never()).findByLocationIdAndNameContainingIgnoreCase(anyLong(), any(), any());
    }

    @Test
    void getTopFiveByEpisodes_requestsTopFiveAndReturnsDelegatedList() {
        RickAndMortyCharacter character = new RickAndMortyCharacter();
        when(characterRepository.findTopByEpisodesCount(PageRequest.of(0, 5))).thenReturn(List.of(character));

        assertThat(service.getTopFiveByEpisodes()).containsExactly(character);
        verify(characterRepository).findTopByEpisodesCount(PageRequest.of(0, 5));
    }
}
