package com.rickymorty.customer.controllers;

import com.rickymorty.customer.services.RickyMortyCharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/characters")
public class RickyMortyCharacterController {

    @Autowired
    private RickyMortyCharacterService rickyMortyCharacterService;

    @PostMapping("/save-by-location/{locationId}")
    public ResponseEntity<Map<String, String>> saveCharacterByLocation(@PathVariable int locationId) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Error on save characters by location");
        int status = 400;

        boolean responseStatus = this.rickyMortyCharacterService.saveCharacterByLocation((long) locationId);
        if (responseStatus) {
            status = 201;
            response.put("message", "Save character to location (%d) with success".formatted(locationId));
        }

        return ResponseEntity.status(HttpStatus.resolve(status))
                .body(response);
    }
}
