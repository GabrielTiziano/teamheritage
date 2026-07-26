package com.gabriel.tiziano.teamheritage.controller;

import com.gabriel.tiziano.teamheritage.config.security.annotations.club.CanReadClub;
import com.gabriel.tiziano.teamheritage.config.security.annotations.club.CanWriteClub;
import com.gabriel.tiziano.teamheritage.dto.request.ClubRequest;
import com.gabriel.tiziano.teamheritage.dto.response.ClubResponse;
import com.gabriel.tiziano.teamheritage.dto.response.PlayerResponse;
import com.gabriel.tiziano.teamheritage.service.ClubService;
import com.gabriel.tiziano.teamheritage.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Clubes", description = "Gerenciamento de clubes")
@SecurityRequirement(name = "bearer-jwt")
public class ClubController {
    private final ClubService clubService;
    private final PlayerService playerService;

    public ClubController(ClubService clubService, PlayerService playerService) {
        this.clubService = clubService;
        this.playerService = playerService;
    }

    @Operation(summary = "Lista clubes", description = "Retorna uma página de clubes ordenada por nome.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página retornada"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido"),
            @ApiResponse(responseCode = "403", description = "Sem o scope club:read")
    })
    @CanReadClub
    @GetMapping
    public ResponseEntity<Page<ClubResponse>> getClubs(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(clubService.getClubs(pageable));
    }

    @Operation(summary = "Busca clube por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clube encontrado"),
            @ApiResponse(responseCode = "404", description = "Clube não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem o scope club:read")
    })
    @CanReadClub
    @GetMapping("{id}")
    public ResponseEntity<ClubResponse> getClubById(@PathVariable Long id) {
        return ResponseEntity.ok(clubService.getClubById(id));
    }

    @Operation(summary = "Lista os jogadores de um clube",
            description = "Retorna uma página de jogadores do clube informado, ordenada pelo número da camisa.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página retornada"),
            @ApiResponse(responseCode = "404", description = "Clube não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem o scope club:read")
    })
    @CanReadClub
    @GetMapping("/{id}/players")
    public ResponseEntity<Page<PlayerResponse>> getClubPlayers(
            @PathVariable Long id,
            @PageableDefault(size = 20, sort = "shirtNumber") Pageable pageable) {
        return ResponseEntity.ok(playerService.getPlayersByClubId(id, pageable));
    }

    @Operation(summary = "Cria um clube")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Clube criado"),
            @ApiResponse(responseCode = "400", description = "Payload inválido"),
            @ApiResponse(responseCode = "403", description = "Sem o scope club:write")
    })
    @CanWriteClub
    @PostMapping
    public ResponseEntity<ClubResponse> createClub(@Valid @RequestBody ClubRequest clubRequest) {
        ClubResponse created = clubService.createClub(clubRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Atualiza um clube")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clube atualizado"),
            @ApiResponse(responseCode = "404", description = "Clube não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem o scope club:write")
    })
    @CanWriteClub
    @PutMapping("{id}")
    public ResponseEntity<ClubResponse> updateClub(@PathVariable Long id, @Valid @RequestBody ClubRequest clubRequest) {
        return ResponseEntity.ok(clubService.updateClub(id, clubRequest));
    }

    @Operation(summary = "Remove um clube")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Clube removido"),
            @ApiResponse(responseCode = "404", description = "Clube não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem o scope club:write")
    })
    @CanWriteClub
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        clubService.deleteClub(id);
        return ResponseEntity.noContent().build();
    }
}
