package com.gabriel.tiziano.teamheritage.dto.response;

public record LoginResponse(
        String accessToken,
        String expiresIn
) {
}
