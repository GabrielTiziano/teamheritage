package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.dto.request.ClubRequest;
import com.gabriel.tiziano.teamheritage.dto.response.ClubResponse;
import com.gabriel.tiziano.teamheritage.entities.Club;
import com.gabriel.tiziano.teamheritage.entities.Stadium;
import com.gabriel.tiziano.teamheritage.mapper.ClubMapper;
import com.gabriel.tiziano.teamheritage.repository.ClubRepository;
import com.gabriel.tiziano.teamheritage.repository.StadiumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClubService {

    private final ClubRepository clubRepository;
    private final StadiumRepository stadiumRepository;
    private final ClubMapper clubMapper;

    public ClubService(ClubRepository clubRepository, StadiumRepository stadiumRepository, ClubMapper clubMapper) {
        this.clubRepository = clubRepository;
        this.stadiumRepository = stadiumRepository;
        this.clubMapper = clubMapper;
    }

    @Transactional(readOnly = true)
    public Page<ClubResponse> getClubs(Pageable pageable) {
        return clubRepository.findAll(pageable)
                .map(clubMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ClubResponse getClubById(Long clubId) {
        return clubMapper.toResponse(resolveClub(clubId));
    }

    @Transactional
    public ClubResponse createClub(ClubRequest clubRequest) {
        Stadium stadium = resolveStadium(clubRequest.stadiumId());
        Club club = clubMapper.toEntity(clubRequest, stadium);
        return clubMapper.toResponse(clubRepository.save(club));
    }

    @Transactional
    public ClubResponse updateClub(Long id, ClubRequest clubRequest) {
        Club club = resolveClub(id);
        Stadium stadium = resolveStadium(clubRequest.stadiumId());
        clubMapper.updateEntity(club, clubRequest, stadium);
        return clubMapper.toResponse(club);
    }

    @Transactional
    public void deleteClub(Long id) {
        Club club = resolveClub(id);
        club.setActive(false);
    }

    private Club resolveClub(Long clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new EntityNotFoundException("Club not found with id: " + clubId));
    }

    private Stadium resolveStadium(Long stadiumId) {
        if (stadiumId == null) {
            return null;
        }
        return stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new EntityNotFoundException("Stadium not found with id: " + stadiumId));
    }
}