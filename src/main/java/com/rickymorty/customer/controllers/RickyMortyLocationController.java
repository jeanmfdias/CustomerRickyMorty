package com.rickymorty.customer.controllers;

import com.rickymorty.customer.dto.RickyMortyLocationDTO;
import com.rickymorty.customer.services.RickyMortyLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/locations")
public class RickyMortyLocationController {
    @Autowired
    private RickyMortyLocationService rickyMortyLocationService;

    @GetMapping
    public ResponseEntity<List<RickyMortyLocationDTO>> getLocations() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(rickyMortyLocationService.getAll());
    }

    @PostMapping("/save-from-web/{locationId}")
    public ResponseEntity<Map<String, String>> saveLocationsFromWeb(@PathVariable int locationId) {
        Map<String, String> result = new HashMap<>();
        result.put("message", "Error on save location");

        boolean saveWithSuccess = rickyMortyLocationService.saveLocationFromWeb(locationId);
        if (saveWithSuccess) {
            result.put("message", "Location created with success");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
