package com.gabriel.tiziano.teamheritage.mapper;

import com.gabriel.tiziano.teamheritage.dto.request.PlayerRequest;
import com.gabriel.tiziano.teamheritage.dto.response.PlayerResponse;
import com.gabriel.tiziano.teamheritage.entities.Club;
import com.gabriel.tiziano.teamheritage.entities.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface PlayerMapper {

    @Mapping(target = "positionDescription", expression = "java(player.getPosition().getDescription())")
    @Mapping(target = "clubId", source = "club.id")
    @Mapping(target = "clubName", source = "club.name")
    PlayerResponse toResponse(Player player);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "position", source = "request.position")
    @Mapping(target = "shirtNumber", source = "request.shirtNumber")
    @Mapping(target = "urlImg", source = "request.urlImg")
    @Mapping(target = "club", source = "club")
    Player toEntity(PlayerRequest request, Club club);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "position", source = "request.position")
    @Mapping(target = "shirtNumber", source = "request.shirtNumber")
    @Mapping(target = "urlImg", source = "request.urlImg")
    @Mapping(target = "club", source = "club")
    void updateEntity(@MappingTarget Player player, PlayerRequest request, Club club);
}