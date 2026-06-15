package com.rickymorty.customer.services;

import com.rickymorty.customer.dto.RickAndMortyEpisodesAndCharactersDto;
import com.rickymorty.customer.dto.RickAndMortyLocationDTO;
import com.rickymorty.customer.models.RickAndMortyCharacter;
import com.rickymorty.customer.models.RickAndMortyCharacterRecord;
import com.rickymorty.customer.models.RickAndMortyEpisodeRecord;
import com.rickymorty.customer.models.RickAndMortyLocation;
import com.rickymorty.customer.models.RickAndMortyLocationRecord;
import com.rickymorty.customer.repositories.ILocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RickAndMortyLocationServiceTest {

    @Mock private ILocationRepository locationRepository;
    @Mock private ConsumerApi consumerApi;
    @Mock private TranslateData translateData;

    @InjectMocks
    private RickAndMortyLocationService service;

    @Test
    void getOneLocation_returnsBuiltLocation() {
        RickAndMortyLocationRecord record = new RickAndMortyLocationRecord("Earth", "Planet", "C-137", List.of());
        when(consumerApi.getData(anyString())).thenReturn("{}");
        when(translateData.getData("{}", RickAndMortyLocationRecord.class)).thenReturn(record);

        RickAndMortyLocation location = service.getOneLocation(1);

        assertThat(location.getName()).isEqualTo("Earth");
        assertThat(location.getType()).isEqualTo("Planet");
        assertThat(location.getDimension()).isEqualTo("C-137");
    }

    @Test
    void getEpisodesAndCharacters_returnsResidentsAndTheirEpisodes() {
        RickAndMortyLocationRecord locationRecord = new RickAndMortyLocationRecord("Earth", "Planet", "C-137", List.of("char-url"));
        RickAndMortyCharacterRecord characterRecord = new RickAndMortyCharacterRecord("Rick", "Alive", "Human", List.of("ep-url"));
        RickAndMortyEpisodeRecord episodeRecord = new RickAndMortyEpisodeRecord("Pilot", "December 2, 2013", "S01E01");

        when(consumerApi.getData("https://rickandmortyapi.com/api/location/1")).thenReturn("location-json");
        when(consumerApi.getData("char-url")).thenReturn("character-json");
        when(consumerApi.getData("ep-url")).thenReturn("episode-json");
        when(translateData.getData("location-json", RickAndMortyLocationRecord.class)).thenReturn(locationRecord);
        when(translateData.getData("character-json", RickAndMortyCharacterRecord.class)).thenReturn(characterRecord);
        when(translateData.getData("episode-json", RickAndMortyEpisodeRecord.class)).thenReturn(episodeRecord);

        RickAndMortyEpisodesAndCharactersDto result = service.getEpisodesAndCharacters(1);

        assertThat(result.residents()).containsExactly(characterRecord);
        assertThat(result.episodes()).containsExactly(episodeRecord);
    }

    @Test
    void getAll_returnsMappedDTOs() {
        RickAndMortyLocation location = new RickAndMortyLocation();
        location.setName("Earth");
        location.setType("Planet");
        location.setDimension("C-137");

        RickAndMortyCharacter character = new RickAndMortyCharacter();
        character.setName("Rick");
        location.setResidents(List.of(character));

        when(locationRepository.findAll()).thenReturn(List.of(location));

        List<RickAndMortyLocationDTO> result = service.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Earth");
        assertThat(result.get(0).residents()).hasSize(1);
        assertThat(result.get(0).residents().get(0)).containsEntry("name", "Rick");
    }

    @Test
    void saveLocationFromWeb_whenSucceeds_returnsTrue() {
        RickAndMortyLocationRecord record = new RickAndMortyLocationRecord("Earth", "Planet", "C-137", List.of());
        when(consumerApi.getData(anyString())).thenReturn("{}");
        when(translateData.getData("{}", RickAndMortyLocationRecord.class)).thenReturn(record);

        assertThat(service.saveLocationFromWeb(1)).isTrue();
        verify(locationRepository).save(any(RickAndMortyLocation.class));
    }

    @Test
    void saveLocationFromWeb_whenExceptionThrown_returnsFalse() {
        when(consumerApi.getData(anyString())).thenThrow(new RuntimeException("API error"));

        assertThat(service.saveLocationFromWeb(1)).isFalse();
        verify(locationRepository, never()).save(any());
    }
}
