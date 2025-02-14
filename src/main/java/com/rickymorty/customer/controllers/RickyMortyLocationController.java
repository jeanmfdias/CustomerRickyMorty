package com.rickymorty.customer.controllers;

import com.rickymorty.customer.dto.RickyMortyLocationDTO;
import com.rickymorty.customer.models.RickyMortyLocation;
import com.rickymorty.customer.repositories.ILocationRepository;
import com.rickymorty.customer.services.RickyMortyLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class RickyMortyLocationController {
    @Autowired
    private RickyMortyLocationService rickyMortyLocationService;

    @GetMapping("/locations")
    public ResponseEntity<List<RickyMortyLocationDTO>> getLocations() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(rickyMortyLocationService.getAll());
    }

    @GetMapping("/locations/save-from-web")
    public ResponseEntity<Map<String, String>> saveLocationsFromWeb() {
        Map<String, String> result = new HashMap<>();
        result.put("message", "Error on save location");

        boolean saveWithSuccess = rickyMortyLocationService.saveLocationFromWeb(4);
        if (saveWithSuccess) {
            result.put("message", "Location created with success");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
