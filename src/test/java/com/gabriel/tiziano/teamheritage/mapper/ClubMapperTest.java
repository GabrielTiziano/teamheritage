package com.gabriel.tiziano.teamheritage.mapper;

import com.gabriel.tiziano.teamheritage.dto.request.ClubRequest;
import com.gabriel.tiziano.teamheritage.dto.response.ClubResponse;
import com.gabriel.tiziano.teamheritage.entities.Club;
import com.gabriel.tiziano.teamheritage.entities.Stadium;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClubMapperTest {


    private final ClubMapper mapper = new ClubMapperImpl(new StadiumMapperImpl());

    @Test
    void toResponse() {
        Stadium stadium = Stadium.builder().id(5L).name("Bernabeu").city("Madrid").capacity(80000).build();
        Club club = Club.builder()
                .id(1L).name("Real Madrid")
                .founded(LocalDate.of(1902, 3, 6))
                .active(true)
                .stadium(stadium)
                .build();

        ClubResponse response = mapper.toResponse(club);

        assertNotNull(response);
        assertEquals("Real Madrid", response.name());
        assertEquals(LocalDate.of(1902, 3, 6), response.founded());
        assertNotNull(response.stadium());
        assertEquals("Bernabeu", response.stadium().name());
    }

    @Test
    void toEntity() {
        ClubRequest request = new ClubRequest("Real Madrid", LocalDate.of(1902, 3, 6), null, 5L);
        Stadium stadium = Stadium.builder().id(5L).name("Bernabeu").build();

        Club club = mapper.toEntity(request, stadium);

        assertNotNull(club);
        assertNull(club.getId());
        assertEquals("Real Madrid", club.getName());
        assertTrue(club.getActive());
        assertNotNull(club.getStadium());
        assertEquals(5L, club.getStadium().getId());
    }
}