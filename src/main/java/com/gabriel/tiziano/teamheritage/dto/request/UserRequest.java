package com.gabriel.tiziano.teamheritage.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;

public record UserRequest(
        @Schema(description = "Nome do usuário", example = "João Silva",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 100, message = "O nome deve ter, no máximo, 100 caracteres")
        String name,

        @Schema(description = "Email do usuário", example = "joao@teamheritage.com",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Email inválido")
        @Size(max = 100, message = "O email deve ter, no máximo, 100 caracteres")
        String email,

        @Schema(description = "Senha (mínimo 8 caracteres)", example = "senhaSegura123",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, max = 100, message = "A senha deve ter entre 8 e 100 caracteres")
        String password,

        @Schema(description = "Lista de ids dos scopes concedidos", example = "[3, 5, 7]",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "Informe ao menos um scope")
        List<@NotNull Long> scopes
) {
}
