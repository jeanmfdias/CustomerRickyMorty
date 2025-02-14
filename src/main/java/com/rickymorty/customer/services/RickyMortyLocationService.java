package com.rickymorty.customer.services;

import com.rickymorty.customer.dto.RickyMortyLocationDTO;
import com.rickymorty.customer.models.RickyMortyLocation;
import com.rickymorty.customer.models.RickyMortyLocationRecord;
import com.rickymorty.customer.repositories.ILocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RickyMortyLocationService {

    @Autowired
    private ILocationRepository locationRepository;

    @Autowired
    private ConsumerApi consumerApi;

    @Autowired
    private TranslateData translateData;

    private final String ADDRESS_LOCATION = "https://rickandmortyapi.com/api/location/";

    public RickyMortyLocation getOneLocation(int locationId) {
        String fullAddress = ADDRESS_LOCATION + locationId;

        var json = this.consumerApi.getData(fullAddress);

        RickyMortyLocationRecord rmLocation = translateData.getData(json, RickyMortyLocationRecord.class);
        return new RickyMortyLocation(rmLocation);
    }

    public List<RickyMortyLocationDTO> getAll() {
        return this.locationRepository.findAll()
                .stream()
                .map(l -> new RickyMortyLocationDTO(l.getId(), l.getName(), l.getType(), l.getDimension(), l.getResidents()))
                .collect(Collectors.toList());
    }

    public boolean saveLocationFromWeb(int id) {
        try {
            RickyMortyLocation location = this.getOneLocation(id);
            this.locationRepository.save(location);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
