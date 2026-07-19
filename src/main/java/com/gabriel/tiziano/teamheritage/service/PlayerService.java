package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.dto.request.PlayerRequest;
import com.gabriel.tiziano.teamheritage.dto.response.PlayerResponse;
import com.gabriel.tiziano.teamheritage.entities.Club;
import com.gabriel.tiziano.teamheritage.entities.Player;
import com.gabriel.tiziano.teamheritage.mapper.PlayerMapper;
import com.gabriel.tiziano.teamheritage.repository.ClubRepository;
import com.gabriel.tiziano.teamheritage.repository.PlayerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ClubRepository clubRepository;
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository, ClubRepository clubRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.clubRepository = clubRepository;
        this.playerMapper = playerMapper;
    }

    @Transactional(readOnly = true)
    public Page<PlayerResponse> getPlayers(Pageable pageable) {
        return playerRepository.findAll(pageable)
                .map(playerMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PlayerResponse getPlayerById(Long playerId) {
        return playerMapper.toResponse(resolvePlayer(playerId));
    }

    @Transactional(readOnly = true)
    public Page<PlayerResponse> getPlayersByClubId(Long clubId, Pageable pageable) {
        if (!clubRepository.existsById(clubId)) {
            throw new EntityNotFoundException("Club not found with id: " + clubId);
        }
        return playerRepository.findByClubId(clubId, pageable)
                .map(playerMapper::toResponse);
    }

    @Transactional
    public PlayerResponse createPlayer(PlayerRequest playerRequest) {
        Club club = resolveClub(playerRequest.clubId());
        Player player = playerMapper.toEntity(playerRequest, club);
        return playerMapper.toResponse(playerRepository.save(player));
    }

    @Transactional
    public PlayerResponse updatePlayer(Long id, PlayerRequest playerRequest) {
        Player player = resolvePlayer(id);
        Club club = resolveClub(playerRequest.clubId());
        playerMapper.updateEntity(player, playerRequest, club);
        return playerMapper.toResponse(player);
    }

    @Transactional
    public void deletePlayer(Long id) {
        playerRepository.delete(resolvePlayer(id));
    }

    private Player resolvePlayer(Long playerId) {
        return playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));
    }

    private Club resolveClub(Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));
    }
}