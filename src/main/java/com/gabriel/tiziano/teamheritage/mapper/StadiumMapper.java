package com.gabriel.tiziano.teamheritage.mapper;

import com.gabriel.tiziano.teamheritage.dto.request.StadiumRequest;
import com.gabriel.tiziano.teamheritage.dto.response.StadiumResponse;
import com.gabriel.tiziano.teamheritage.entities.Stadium;

public class StadiumMapper {
    private StadiumMapper() {}

    public static StadiumResponse toResponse(Stadium stadium) {
        if (stadium == null) {
            return null;
        }

        return StadiumResponse.builder()
                .id(stadium.getId())
                .name(stadium.getName())
                .city(stadium.getCity())
                .capacity(stadium.getCapacity())
                .urlImg(stadium.getUrlImg())
                .build();
    }

    //POST
    public static Stadium toEntity(StadiumRequest request) {
        if (request == null) {
            return null;
        }
        return Stadium.builder()
                .name(request.name())
                .city(request.city())
                .capacity(request.capacity())
                .urlImg(request.urlImg())
                .build();
    }

    //PUT
    public static void updateEntity(Stadium stadium, StadiumRequest request) {
        stadium.setName(request.name());
        stadium.setCity(request.city());
        stadium.setCapacity(request.capacity());
        stadium.setUrlImg(request.urlImg());
    }
}
