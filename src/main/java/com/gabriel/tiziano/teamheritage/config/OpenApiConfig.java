package com.gabriel.tiziano.teamheritage.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "TeamHeritage API",
                version = "v1",
                description = "API REST para gerenciamento de estádios, clubes e jogadores de futebol. "
                        + "Autenticação stateless com JWT assinado por RSA e autorização por scopes.",
                contact = @Contact(name = "Gabriel Tiziano", url = "https://github.com/GabrielTiziano")
        )
)
@SecurityScheme(
        name = "bearer-jwt",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Cole aqui apenas o token retornado pelo POST /login, sem a palavra Bearer."
)
public class OpenApiConfig {
}