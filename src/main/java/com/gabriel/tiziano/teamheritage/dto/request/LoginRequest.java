package com.gabriel.tiziano.teamheritage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @Schema(description = "Email do usuário", example = "admin@teamheritage.com",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @Schema(description = "Senha do usuário", example = "admin123",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A senha é obrigatória")
        String password

) {
}