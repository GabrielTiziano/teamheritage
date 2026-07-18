package com.gabriel.tiziano.teamheritage.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ClubRequest(
        @NotBlank(message = "O nome do clube e obrigatorio")
        @Size(max = 255, message = "O nome deve ter no maximo 255 caracteres")
        String name,

        @NotNull(message = "A data de fundacao e obrigatoria")
        @PastOrPresent(message = "A data de fundacao nao pode estar no futuro")
        LocalDate founded,

        @Size(max = 255, message = "A url da imagem deve ter no maximo 255 caracteres")
        String urlImg,

        Long stadiumId
) {
}