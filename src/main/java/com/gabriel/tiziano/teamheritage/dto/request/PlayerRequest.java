package com.gabriel.tiziano.teamheritage.dto.request;

import com.gabriel.tiziano.teamheritage.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlayerRequest(
        @Schema(description = "Nome do jogador", example = "Germán Cano",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome do jogador é obrigatório")
        @Size(max = 255, message = "O nome deve ter, no máximo, 255 caracteres")
        String name,

        @Schema(description = "Posição do jogador", example = "FORWARD",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "A posição é obrigatória")
        Position position,

        @Schema(description = "Número da camisa (1 a 99)", example = "14",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O número da camisa é obrigatório")
        @Min(value = 1, message = "O número da camisa deve ser, no mínimo, 1")
        @Max(value = 99, message = "O número da camisa deve ser, no máximo, 99")
        Integer shirtNumber,

        @Schema(description = "URL da foto do jogador", example = "https://exemplo.com/german-cano.png")
        @Size(max = 255, message = "A url da imagem deve ter, no máximo, 255 caracteres")
        String urlImg,

        @Schema(description = "Id do clube do jogador", example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O clube é obrigatório")
        Long clubId
) {
}