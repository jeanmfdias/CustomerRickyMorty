package com.rickymorty.customer.services;

import com.rickymorty.customer.models.*;
import com.rickymorty.customer.repositories.ICharacterRepository;
import com.rickymorty.customer.repositories.ILocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RickAndMortyCharacterService {
    @Autowired
    private ILocationRepository locationRepository;

    @Autowired
    private ICharacterRepository characterRepository;

    @Autowired
    private ConsumerApi consumerApi;

    @Autowired
    private TranslateData translateData;

    private final String ADDRESS_LOCATION = "https://rickandmortyapi.com/api/location/";

    public boolean saveCharacterByLocation(Long locationId) {
        Optional<RickAndMortyLocation> location = this.locationRepository.findById(locationId);;

        if (location.isPresent()) {
            RickAndMortyLocation locationFinded = location.get();

            String url = ADDRESS_LOCATION + "?name=" + locationFinded.getName().toLowerCase().replace(" ", "+");
            String json = this.consumerApi.getData(url);
            RickAndMortyLocationListRecord locationRecords = this.translateData.getData(json, RickAndMortyLocationListRecord.class);

            List<RickAndMortyCharacter> characters = new ArrayList<>();
            for (RickAndMortyLocationRecord record : locationRecords.locations()) {
                for (String urlCharacter : record.residents()) {
                    json = this.consumerApi.getData(urlCharacter);
                    RickAndMortyCharacterRecord characterRecord = this.translateData.getData(json, RickAndMortyCharacterRecord.class);
                    RickAndMortyCharacter character = new RickAndMortyCharacter(characterRecord);

                    Optional<RickAndMortyCharacter> exists = characters.stream()
                            .filter(c -> c.getName().toLowerCase().equals(character.getName().toLowerCase()))
                            .findFirst();
                    if (exists.isEmpty()) {
                        characters.add(character);
                    }
                }
            }
            locationFinded.setResidents(characters);
            this.locationRepository.save(locationFinded);
            return true;
        }
        return false;
    }

    public Long getCountByLocationId(Long locationId) {
        return this.characterRepository.countByLocationId(locationId);
    }

    public List<RickAndMortyCharacter> getAllByLocationName(String locationName) {
        return this.characterRepository.findByLocationNameContainingIgnoreCase(locationName);
    }

    public Optional<RickAndMortyCharacter> findByName(String name) {
        return this.characterRepository.findByNameContainingIgnoreCase(name);
    }
}
