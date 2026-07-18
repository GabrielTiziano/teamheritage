package com.gabriel.tiziano.teamheritage.dto.request;

import com.gabriel.tiziano.teamheritage.enums.Position;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PlayerRequest(
        @NotBlank(message = "O nome do jogador e obrigatorio")
        @Size(max = 255, message = "O nome deve ter no maximo 255 caracteres")
        String name,

        @NotNull(message = "A posicao e obrigatoria")
        Position position,

        @NotNull(message = "O numero da camisa e obrigatorio")
        @Min(value = 1, message = "O numero da camisa deve ser no minimo 1")
        @Max(value = 99, message = "O numero da camisa deve ser no maximo 99")
        Integer shirtNumber,

        @Size(max = 255, message = "A url da imagem deve ter no maximo 255 caracteres")
        String urlImg,

        @NotNull(message = "O clube e obrigatorio")
        Long clubId
) {
}