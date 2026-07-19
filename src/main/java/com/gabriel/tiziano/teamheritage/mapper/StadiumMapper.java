package com.gabriel.tiziano.teamheritage.mapper;

import com.gabriel.tiziano.teamheritage.dto.request.StadiumRequest;
import com.gabriel.tiziano.teamheritage.dto.response.StadiumResponse;
import com.gabriel.tiziano.teamheritage.entities.Stadium;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface StadiumMapper {

    StadiumResponse toResponse(Stadium stadium);

    @Mapping(target = "id", ignore = true)
    Stadium toEntity(StadiumRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Stadium stadium, StadiumRequest request);
}