package com.gabriel.tiziano.teamheritage.mapper;

import com.gabriel.tiziano.teamheritage.dto.request.PlayerRequest;
import com.gabriel.tiziano.teamheritage.dto.response.PlayerResponse;
import com.gabriel.tiziano.teamheritage.enums.Position;
import com.gabriel.tiziano.teamheritage.entities.Club;
import com.gabriel.tiziano.teamheritage.entities.Player;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMapperTest {

    private final PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

    @Test
    void toResponse() {
        Club club = Club.builder().id(10L).name("Real Madrid").build();
        Player player = Player.builder()
                .id(1L)
                .name("Vinicius Junior")
                .position(Position.FORWARD)
                .shirtNumber(7)
                .urlImg("https://example.com/vini.jpg")
                .club(club)
                .build();

        PlayerResponse response = mapper.toResponse(player);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals(Position.FORWARD, response.position());
        assertEquals("Atacante", response.positionDescription());
        assertEquals(7, response.shirtNumber());
        assertEquals(10L, response.clubId());
        assertEquals("Real Madrid", response.clubName());
    }

    @Test
    void toEntity() {
        PlayerRequest request = new PlayerRequest(
                "Vinicius Junior", Position.FORWARD, 7, "https://example.com/vini.jpg", 10L);
        Club club = Club.builder().id(10L).name("Real Madrid").build();

        Player player = mapper.toEntity(request, club);

        assertNotNull(player);
        assertNull(player.getId());
        assertEquals("Vinicius Junior", player.getName());
        assertEquals(Position.FORWARD, player.getPosition());
        assertNotNull(player.getClub());
        assertEquals(10L, player.getClub().getId());
    }
}