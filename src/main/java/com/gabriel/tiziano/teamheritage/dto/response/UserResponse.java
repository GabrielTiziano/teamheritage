package com.gabriel.tiziano.teamheritage.dto.response;

import java.util.List;

public record UserResponse(
        Long id,
        String name,
        String email,
        boolean active,
        List<String> scopes
) {
}
