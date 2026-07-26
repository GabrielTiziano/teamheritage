package com.gabriel.tiziano.teamheritage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClubRequest(
        @Schema(description = "Nome do clube", example = "Fluminense",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome do clube é obrigatório")
        @Size(max = 255, message = "O nome deve ter, no máximo, 255 caracteres")
        String name,

        @Schema(description = "Data de fundação do clube", example = "1895-11-15",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "A data de fundação é obrigatória")
        @PastOrPresent(message = "A data de fundação não pode estar no futuro")
        LocalDate founded,

        @Schema(description = "URL do escudo do clube", example = "https://exemplo.com/fluminense.png")
        @Size(max = 255, message = "A url da imagem deve ter, no máximo, 255 caracteres")
        String urlImg,

        Long stadiumId
) {
}