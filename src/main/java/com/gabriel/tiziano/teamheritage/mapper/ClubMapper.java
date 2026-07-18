package com.gabriel.tiziano.teamheritage.mapper;

import com.gabriel.tiziano.teamheritage.dto.request.ClubRequest;
import com.gabriel.tiziano.teamheritage.dto.response.ClubResponse;
import com.gabriel.tiziano.teamheritage.entities.Club;
import com.gabriel.tiziano.teamheritage.entities.Stadium;

public class ClubMapper {
    private ClubMapper() {}

    public static ClubResponse toResponse(Club club) {
        if (club == null) {
            return null;
        }
        return ClubResponse.builder()
                .id(club.getId())
                .name(club.getName())
                .founded(club.getFounded())
                .urlImg(club.getUrlImg())
                .createdAt(club.getCreatedAt())
                .active(club.getActive())
                .stadium(StadiumMapper.toResponse(club.getStadium()))
                .build();
    }

    //POST
    public static Club toEntity(ClubRequest request, Stadium stadium) {
        if (request == null) {
            return null;
        }
        return Club.builder()
                .name(request.name())
                .founded(request.founded())
                .urlImg(request.urlImg())
                .stadium(stadium)
                .active(true)
                .build();
    }

    //PUT
    public static void updateEntity(Club club, ClubRequest request, Stadium stadium) {
        club.setName(request.name());
        club.setFounded(request.founded());
        club.setUrlImg(request.urlImg());
        club.setStadium(stadium);
    }

}