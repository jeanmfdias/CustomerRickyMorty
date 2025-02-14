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
    private ILocationRepository locationRepository;

    private final RickyMortyLocationService rickyMortyLocationService = new RickyMortyLocationService();

    @GetMapping("/locations")
    public ResponseEntity<List<RickyMortyLocationDTO>> getLocations() {
        List<RickyMortyLocationDTO> locations = this.locationRepository.findAll()
                .stream()
                .map(l -> new RickyMortyLocationDTO(l.getId(), l.getName(), l.getType(), l.getDimension(), l.getResidents()))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(locations);
    }

    @GetMapping("/locations/save-from-web")
    public ResponseEntity<Map<String, String>> saveLocationsFromWeb() {
        RickyMortyLocation location = this.rickyMortyLocationService.getOneLocation(3);
        this.locationRepository.save(location);
        Map<String, String> result = new HashMap<>();
        result.put("message", "Location created with sucess");
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
