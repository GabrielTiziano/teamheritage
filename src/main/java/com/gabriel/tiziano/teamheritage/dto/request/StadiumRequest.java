package com.gabriel.tiziano.teamheritage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record StadiumRequest(
        @NotBlank(message = "O nome do estadio e obrigatorio")
        @Size(max = 255, message = "O nome deve ter no maximo 255 caracteres")
        String name,

        @NotBlank(message = "A cidade e obrigatoria")
        @Size(max = 255, message = "A cidade deve ter no maximo 255 caracteres")
        String city,

        @Positive(message = "A capacidade deve ser maior que zero")
        Integer capacity,

        @Size(max = 255, message = "A url da imagem deve ter no maximo 255 caracteres")
        String urlImg
) {
}