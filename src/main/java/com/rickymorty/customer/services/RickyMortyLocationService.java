package com.rickymorty.customer.services;

import com.rickymorty.customer.models.RickyMortyLocation;
import com.rickymorty.customer.models.RickyMortyLocationRecord;

public class RickyMortyLocationService {
    private final String ADDRESS_LOCATION = "https://rickandmortyapi.com/api/location/";

    private final ConsumerApi consumerApi = new ConsumerApi();

    private final TranslateData translateData = new TranslateData();

    public RickyMortyLocation getOneLocation(int locationId) {
        String fullAddress = ADDRESS_LOCATION + locationId;

        var json = this.consumerApi.getData(fullAddress);

        RickyMortyLocationRecord rmLocation = translateData.getData(json, RickyMortyLocationRecord.class);
        return new RickyMortyLocation(rmLocation);
    }
}
