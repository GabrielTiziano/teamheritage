package com.gabriel.tiziano.teamheritage.dto.response;

import com.gabriel.tiziano.teamheritage.enums.Position;
import lombok.Builder;

@Builder
public record PlayerResponse(
        Long id,
        String name,
        Position position,
        String positionDescription,
        Integer shirtNumber,
        String urlImg,
        Long clubId,
        String clubName

) {
}