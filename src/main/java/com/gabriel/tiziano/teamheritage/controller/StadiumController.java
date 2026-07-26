package com.gabriel.tiziano.teamheritage.controller;

import com.gabriel.tiziano.teamheritage.config.security.annotations.stadium.CanReadStadium;
import com.gabriel.tiziano.teamheritage.config.security.annotations.stadium.CanWriteStadium;
import com.gabriel.tiziano.teamheritage.dto.request.StadiumRequest;
import com.gabriel.tiziano.teamheritage.dto.response.StadiumResponse;
import com.gabriel.tiziano.teamheritage.service.StadiumService;
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
@RequestMapping("stadiums")
@Tag(name = "Estádios", description = "Gerenciamento de estádios")
@SecurityRequirement(name = "bearer-jwt")
public class StadiumController {
    private final StadiumService stadiumService;

    public StadiumController(StadiumService stadiumService) {
        this.stadiumService = stadiumService;
    }

    @Operation(summary = "Lista estádios", description = "Retorna uma página de estádios ordenada por nome.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Página retornada"),
            @ApiResponse(responseCode = "401", description = "Token ausente ou inválido"),
            @ApiResponse(responseCode = "403", description = "Sem o scope stadium:read")
    })
    @CanReadStadium
    @GetMapping
    public ResponseEntity<Page<StadiumResponse>> getStadiums(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(stadiumService.getStadiums(pageable));
    }

    @Operation(summary = "Busca estádio por id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estádio encontrado"),
            @ApiResponse(responseCode = "404", description = "Estádio não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem o scope stadium:read")
    })
    @CanReadStadium
    @GetMapping("/{id}")
    public ResponseEntity<StadiumResponse> getStadiumById(@PathVariable Long id) {
        return ResponseEntity.ok(stadiumService.getStadiumById(id));
    }

    @Operation(summary = "Cria um estádio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Estádio criado"),
            @ApiResponse(responseCode = "400", description = "Payload inválido"),
            @ApiResponse(responseCode = "403", description = "Sem o scope stadium:write")
    })
    @CanWriteStadium
    @PostMapping
    public ResponseEntity<StadiumResponse> createStadium(@Valid @RequestBody StadiumRequest stadiumRequest) {
        StadiumResponse created = stadiumService.createStadium(stadiumRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Atualiza um estádio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estádio atualizado"),
            @ApiResponse(responseCode = "404", description = "Estádio não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem o scope stadium:write")
    })
    @CanWriteStadium
    @PutMapping("/{id}")
    public ResponseEntity<StadiumResponse> updateStadium(@PathVariable Long id,
                                                         @Valid @RequestBody StadiumRequest stadiumRequest) {
        return ResponseEntity.ok(stadiumService.updateStadium(id, stadiumRequest));
    }

    @Operation(summary = "Remove um estádio")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estádio removido"),
            @ApiResponse(responseCode = "404", description = "Estádio não encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem o scope stadium:write")
    })
    @CanWriteStadium
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStadium(@PathVariable Long id) {
        stadiumService.deleteStadium(id);
        return ResponseEntity.noContent().build();
    }
}
