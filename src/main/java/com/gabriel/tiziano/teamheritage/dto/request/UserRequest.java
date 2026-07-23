package com.gabriel.tiziano.teamheritage.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record UserRequest(
        @NotBlank(message = "O nome e obrigatorio")
        @Size(max = 100, message = "O nome deve ter no maximo 100 caracteres")
        String name,

        @NotBlank(message = "O email e obrigatorio")
        @Email(message = "Email invalido")
        @Size(max = 100, message = "O email deve ter no maximo 100 caracteres")
        String email,

        @NotBlank(message = "A senha e obrigatoria")
        @Size(min = 8, max = 100, message = "A senha deve ter entre 8 e 100 caracteres")
        String password,

        @NotEmpty(message = "Informe ao menos um scope")
        List<@NotNull Long> scopes
) {
}
