package com.gabriel.tiziano.teamheritage.controller;

import com.gabriel.tiziano.teamheritage.dto.request.StadiumRequest;
import com.gabriel.tiziano.teamheritage.dto.response.StadiumResponse;
import com.gabriel.tiziano.teamheritage.service.StadiumService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("stadiums")
public class StadiumController {
    private final StadiumService stadiumService;

    public StadiumController(StadiumService stadiumService) {
        this.stadiumService = stadiumService;
    }

    @GetMapping
    public ResponseEntity<Page<StadiumResponse>> getStadiums(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(stadiumService.getStadiums(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StadiumResponse> getStadiumById(@PathVariable Long id) {
        return ResponseEntity.ok(stadiumService.getStadiumById(id));
    }

    @PostMapping
    public ResponseEntity<StadiumResponse> createStadium(@Valid @RequestBody StadiumRequest stadiumRequest) {
        StadiumResponse created = stadiumService.createStadium(stadiumRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StadiumResponse> updateStadium(@PathVariable Long id,
                                                         @Valid @RequestBody StadiumRequest stadiumRequest) {
        return ResponseEntity.ok(stadiumService.updateStadium(id, stadiumRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStadium(@PathVariable Long id) {
        stadiumService.deleteStadium(id);
        return ResponseEntity.noContent().build();
    }
}
