package com.gabriel.tiziano.teamheritage.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record ClubResponse(
        Long id,
        String name,
        LocalDate founded,
        String urlImg,
        LocalDateTime createdAt,
        Boolean active,
        StadiumResponse stadium

) {
}