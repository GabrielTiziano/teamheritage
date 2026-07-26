package com.gabriel.tiziano.teamheritage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record StadiumRequest(
        @Schema(description = "Nome do estádio", example = "Maracanã",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome do estádio é obrigatório")
        @Size(max = 255, message = "O nome deve ter, no máximo, 255 caracteres")
        String name,

        @Schema(description = "Cidade onde fica o estádio", example = "Rio de Janeiro",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A cidade é obrigatória")
        @Size(max = 255, message = "A cidade deve ter, no máximo, 255 caracteres")
        String city,

        @Schema(description = "Capacidade de público", example = "78838")
        @Positive(message = "A capacidade deve ser maior que zero")
        Integer capacity,

        @Schema(description = "URL da imagem do estádio", example = "https://exemplo.com/maracana.png")
        @Size(max = 255, message = "A url da imagem deve ter, no máximo, 255 caracteres")
        String urlImg
) {
}