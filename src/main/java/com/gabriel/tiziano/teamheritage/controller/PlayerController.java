package com.gabriel.tiziano.teamheritage.controller;

import com.gabriel.tiziano.teamheritage.config.security.annotations.player.CanReadPlayer;
import com.gabriel.tiziano.teamheritage.config.security.annotations.player.CanWritePlayer;
import com.gabriel.tiziano.teamheritage.dto.request.PlayerRequest;
import com.gabriel.tiziano.teamheritage.dto.response.PlayerResponse;
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
@RequestMapping("players")
@Tag(name = "Jogadores", description = "Gerenciamento de jogadores")
@SecurityRequirement(name = "bearer-jwt")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @Operation(summary = "Lista jogadores", description = "Retorna uma página de jogadores ordenada por nome.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página retornada"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido"),
            @ApiResponse(responseCode = "403", description = "Sem o scope player:read")
    })
    @CanReadPlayer
    @GetMapping
    public ResponseEntity<Page<PlayerResponse>> getPlayers(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(playerService.getPlayers(pageable));
    }

    @Operation(summary = "Busca jogador por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jogador encontrado"),
            @ApiResponse(responseCode = "404", description = "Jogador não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem o scope player:read")
    })
    @CanReadPlayer
    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponse> getPlayerById(@PathVariable Long id) {
        return ResponseEntity.ok(playerService.getPlayerById(id));
    }

    @Operation(summary = "Cria um jogador")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Jogador criado"),
            @ApiResponse(responseCode = "400", description = "Payload inválido"),
            @ApiResponse(responseCode = "403", description = "Sem o scope player:write")
    })
    @CanWritePlayer
    @PostMapping
    public ResponseEntity<PlayerResponse> createPlayer(@Valid @RequestBody PlayerRequest playerRequest) {
        PlayerResponse created = playerService.createPlayer(playerRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Atualiza um jogador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jogador atualizado"),
            @ApiResponse(responseCode = "404", description = "Jogador não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem o scope player:write")
    })
    @CanWritePlayer
    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponse> updatePlayer(@PathVariable Long id,
                                                       @Valid @RequestBody PlayerRequest playerRequest) {
        return ResponseEntity.ok(playerService.updatePlayer(id, playerRequest));
    }

    @Operation(summary = "Remove um jogador")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Jogador removido"),
            @ApiResponse(responseCode = "404", description = "Jogador não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem o scope player:write")
    })
    @CanWritePlayer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }
}
