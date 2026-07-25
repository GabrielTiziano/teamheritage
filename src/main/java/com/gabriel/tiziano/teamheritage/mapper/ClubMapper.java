package com.gabriel.tiziano.teamheritage.mapper;

import com.gabriel.tiziano.teamheritage.dto.request.ClubRequest;
import com.gabriel.tiziano.teamheritage.dto.response.ClubResponse;
import com.gabriel.tiziano.teamheritage.entities.Club;
import com.gabriel.tiziano.teamheritage.entities.Stadium;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(uses = StadiumMapper.class, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ClubMapper {

    ClubResponse toResponse(Club club);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "founded", source = "request.founded")
    @Mapping(target = "urlImg", source = "request.urlImg")
    @Mapping(target = "stadium", source = "stadium")
    Club toEntity(ClubRequest request, Stadium stadium);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "founded", source = "request.founded")
    @Mapping(target = "urlImg", source = "request.urlImg")
    @Mapping(target = "stadium", source = "stadium")
    void updateEntity(@MappingTarget Club club, ClubRequest request, Stadium stadium);
}