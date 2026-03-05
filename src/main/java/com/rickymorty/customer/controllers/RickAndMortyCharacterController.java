package com.rickymorty.customer.controllers;

import com.rickymorty.customer.dto.RickAndMortyCharacterDto;
import com.rickymorty.customer.services.RickAndMortyCharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/characters")
public class RickAndMortyCharacterController {

    @Autowired
    private RickAndMortyCharacterService rickAndMortyCharacterService;

    @PostMapping("/save-by-location/{locationId}")
    public ResponseEntity<Map<String, String>> saveCharacterByLocation(@PathVariable int locationId) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Error on save characters by location");
        int status = 400;

        boolean responseStatus = this.rickAndMortyCharacterService.saveCharacterByLocation((long) locationId);
        if (responseStatus) {
            Long countCharacters = this.rickAndMortyCharacterService.getCountByLocationId((long) locationId);
            status = 201;
            response.put("message", "Save %d character(s) to location (%d) with success".formatted(countCharacters, locationId));
        }

        return ResponseEntity.status(HttpStatus.resolve(status))
                .body(response);
    }

    @GetMapping("location-name")
    public ResponseEntity<List<RickAndMortyCharacterDto>> getByLocationName(@RequestParam String name) {
        var listRickAndMortyCharacter = this.rickAndMortyCharacterService.getAllByLocationName(name);
        var response = listRickAndMortyCharacter.stream().map(RickAndMortyCharacterDto::new).toList();

        return ResponseEntity.ok(response);
    }
}
