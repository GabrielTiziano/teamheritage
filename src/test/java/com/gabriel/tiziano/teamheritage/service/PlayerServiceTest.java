package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.dto.request.PlayerRequest;
import com.gabriel.tiziano.teamheritage.dto.response.PlayerResponse;
import com.gabriel.tiziano.teamheritage.enums.Position;
import com.gabriel.tiziano.teamheritage.entities.Club;
import com.gabriel.tiziano.teamheritage.entities.Player;
import com.gabriel.tiziano.teamheritage.exception.ResourceNotFoundException;
import com.gabriel.tiziano.teamheritage.mapper.PlayerMapper;
import com.gabriel.tiziano.teamheritage.repository.ClubRepository;
import com.gabriel.tiziano.teamheritage.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock PlayerRepository playerRepository;
    @Mock ClubRepository clubRepository;
    @Mock PlayerMapper playerMapper;

    @InjectMocks PlayerService playerService;

    @Test
    void getPlayers_shouldReturnPageOfPlayers() {
        Pageable pageable = PageRequest.of(0, 20);
        Player player = Player.builder().id(1L).name("Vinicius Junior").build();
        PlayerResponse response = new PlayerResponse(1L, "Vinicius Junior", Position.FORWARD, "Atacante", 7, null, 10L, "Real Madrid");

        when(playerRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(player)));
        when(playerMapper.toResponse(player)).thenReturn(response);

        Page<PlayerResponse> result = playerService.getPlayers(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getPlayerById_whenExists_shouldReturnPlayer() {
        Player player = Player.builder().id(1L).name("Vinicius Junior").build();
        PlayerResponse response = new PlayerResponse(1L, "Vinicius Junior", Position.FORWARD, "Atacante", 7, null, 10L, "Real Madrid");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(playerMapper.toResponse(player)).thenReturn(response);

        PlayerResponse result = playerService.getPlayerById(1L);

        assertEquals("Vinicius Junior", result.name());
    }

    @Test
    void getPlayerById_whenNotFound_shouldThrowException() {
        when(playerRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.getPlayerById(999L));
        verify(playerMapper, never()).toResponse(any());
    }

    @Test
    void getPlayersByClubId_whenClubNotFound_shouldThrowException() {
        when(clubRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> playerService.getPlayersByClubId(999L, PageRequest.of(0, 20)));
        verify(playerRepository, never()).findByClubId(anyLong(), any());
    }

    @Test
    void createPlayer_shouldSaveAndReturnResponse() {
        PlayerRequest request = new PlayerRequest("Vinicius Junior", Position.FORWARD, 7, null, 10L);
        Club club = Club.builder().id(10L).name("Real Madrid").build();
        Player player = Player.builder().name("Vinicius Junior").club(club).build();
        Player saved = Player.builder().id(1L).name("Vinicius Junior").club(club).build();
        PlayerResponse response = new PlayerResponse(1L, "Vinicius Junior", Position.FORWARD, "Atacante", 7, null, 10L, "Real Madrid");

        when(clubRepository.findById(10L)).thenReturn(Optional.of(club));
        when(playerMapper.toEntity(request, club)).thenReturn(player);
        when(playerRepository.save(player)).thenReturn(saved);
        when(playerMapper.toResponse(saved)).thenReturn(response);

        PlayerResponse result = playerService.createPlayer(request);

        assertEquals(1L, result.id());
        verify(playerRepository).save(player);
    }

    @Test
    void createPlayer_whenClubNotFound_shouldThrowException() {
        PlayerRequest request = new PlayerRequest("Vinicius Junior", Position.FORWARD, 7, null, 999L);

        when(clubRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.createPlayer(request));
        verify(playerRepository, never()).save(any());
    }

    @Test
    void deletePlayer_shouldDeletePhysically() {
        Player player = Player.builder().id(1L).name("Vinicius Junior").build();
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        playerService.deletePlayer(1L);

        verify(playerRepository).delete(player);
    }
}