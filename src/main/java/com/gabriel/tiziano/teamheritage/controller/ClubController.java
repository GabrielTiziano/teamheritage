package com.gabriel.tiziano.teamheritage.controller;

import com.gabriel.tiziano.teamheritage.dto.request.ClubRequest;
import com.gabriel.tiziano.teamheritage.dto.response.ClubResponse;
import com.gabriel.tiziano.teamheritage.dto.response.PlayerResponse;
import com.gabriel.tiziano.teamheritage.service.ClubService;
import com.gabriel.tiziano.teamheritage.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("clubs")
public class ClubController {
    private final ClubService clubService;
    private final PlayerService playerService;

    public ClubController(ClubService clubService, PlayerService playerService) {
        this.clubService = clubService;
        this.playerService = playerService;
    }

    @GetMapping
    public ResponseEntity<Page<ClubResponse>> getClubs(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(clubService.getClubs(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<ClubResponse> getClubById(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.getClubById(id));
    }

    @GetMapping("/{id}/players")
    public ResponseEntity<Page<PlayerResponse>> getClubPlayers(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "shirtNumber") Pageable pageable) {
        return ResponseEntity.ok(playerService.getPlayersByClubId(id, pageable));
    }

    @PostMapping
    public ResponseEntity<ClubResponse> createClub(@Valid @RequestBody ClubRequest clubRequest) {
        ClubResponse created = clubService.createClub(clubRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<ClubResponse> updateClub(@PathVariable Long id, @Valid @RequestBody ClubRequest clubRequest) {
        return ResponseEntity.ok(clubService.updateClub(id, clubRequest));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);
        return ResponseEntity.noContent().build();
    }
}
