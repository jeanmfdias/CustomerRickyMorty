package com.rickymorty.customer.controllers;

import com.rickymorty.customer.dto.RickAndMortyEpisodesAndCharactersDto;
import com.rickymorty.customer.dto.RickAndMortyLocationDTO;
import com.rickymorty.customer.services.RickAndMortyLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/locations")
public class RickAndMortyLocationController {
    @Autowired
    private RickAndMortyLocationService rickAndMortyLocationService;

    @GetMapping
    public ResponseEntity<List<RickAndMortyLocationDTO>> getLocations() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(rickAndMortyLocationService.getAll());
    }

    @GetMapping("/episodes-and-characters/{locationId}")
    public ResponseEntity<RickAndMortyEpisodesAndCharactersDto> getEpisodesAndCharacters(@PathVariable int locationId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(rickAndMortyLocationService.getEpisodesAndCharacters(locationId));
    }

    @PostMapping("/save-from-web/{locationId}")
    public ResponseEntity<Map<String, String>> saveLocationsFromWeb(@PathVariable int locationId) {
        Map<String, String> result = new HashMap<>();
        result.put("message", "Error on save location");

        boolean saveWithSuccess = rickAndMortyLocationService.saveLocationFromWeb(locationId);
        if (saveWithSuccess) {
            result.put("message", "Location created with success");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
