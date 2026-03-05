package com.rickymorty.customer.services;

import com.rickymorty.customer.dto.RickAndMortyLocationDTO;
import com.rickymorty.customer.models.RickAndMortyLocation;
import com.rickymorty.customer.models.RickAndMortyLocationRecord;
import com.rickymorty.customer.repositories.ILocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RickAndMortyLocationService {

    @Autowired
    private ILocationRepository locationRepository;

    @Autowired
    private ConsumerApi consumerApi;

    @Autowired
    private TranslateData translateData;

    private final String ADDRESS_LOCATION = "https://rickandmortyapi.com/api/location/";

    public RickAndMortyLocation getOneLocation(int locationId) {
        String fullAddress = ADDRESS_LOCATION + locationId;

        var json = this.consumerApi.getData(fullAddress);

        RickAndMortyLocationRecord rmLocation = translateData.getData(json, RickAndMortyLocationRecord.class);
        return new RickAndMortyLocation(rmLocation);
    }

    public List<RickAndMortyLocationDTO> getAll() {
        return this.locationRepository.findAll()
                .stream()
                .map(l -> new RickAndMortyLocationDTO(l.getId(), l.getName(), l.getType(), l.getDimension(), l.getResidents()
                        .stream()
                        .map(r -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", r.getId());
                            map.put("name", r.getName());
                            return map;
                        })
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public boolean saveLocationFromWeb(int id) {
        try {
            RickAndMortyLocation location = this.getOneLocation(id);
            this.locationRepository.save(location);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
