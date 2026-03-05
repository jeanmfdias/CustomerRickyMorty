package com.rickymorty.customer.controllers;

import com.rickymorty.customer.dto.RickAndMortyEpisodeDto;
import com.rickymorty.customer.services.RickAndMortyEpisodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/episodes")
public class RickAndMortyEpisodeController {

    @Autowired
    private RickAndMortyEpisodeService rickAndMortyEpisodeService;

    @GetMapping
    public ResponseEntity<Page<RickAndMortyEpisodeDto>> getAll(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var response = this.rickAndMortyEpisodeService
                .getAll(name, PageRequest.of(page, size))
                .map(RickAndMortyEpisodeDto::new);

        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
}
