package com.gabriel.tiziano.teamheritage.mapper;

import com.gabriel.tiziano.teamheritage.dto.request.PlayerRequest;
import com.gabriel.tiziano.teamheritage.dto.response.PlayerResponse;
import com.gabriel.tiziano.teamheritage.entities.Club;
import com.gabriel.tiziano.teamheritage.entities.Player;

public class PlayerMapper {
    private PlayerMapper() {}

    public static PlayerResponse toResponse(Player player) {
        if (player == null) {
            return null;
        }

        return PlayerResponse.builder()
                .id(player.getId())
                .name(player.getName())
                .position(player.getPosition())
                .positionDescription(player.getPosition().getDescription())
                .shirtNumber(player.getShirtNumber())
                .urlImg(player.getUrlImg())
                .clubId(player.getClub().getId())
                .clubName(player.getClub().getName())
                .build();
    }

    //POST
    public static Player toEntity(PlayerRequest request, Club club) {
        if (request == null) {
            return null;
        }
        return Player.builder()
                .name(request.name())
                .position(request.position())
                .shirtNumber(request.shirtNumber())
                .urlImg(request.urlImg())
                .club(club)
                .build();
    }

    //PUT
    public static void updateEntity(Player player, PlayerRequest request, Club club) {
        player.setName(request.name());
        player.setPosition(request.position());
        player.setShirtNumber(request.shirtNumber());
        player.setUrlImg(request.urlImg());
        player.setClub(club);
    }
}
