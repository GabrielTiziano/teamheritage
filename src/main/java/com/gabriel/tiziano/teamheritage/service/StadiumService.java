package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.dto.request.StadiumRequest;
import com.gabriel.tiziano.teamheritage.dto.response.StadiumResponse;
import com.gabriel.tiziano.teamheritage.entities.Stadium;
import com.gabriel.tiziano.teamheritage.mapper.StadiumMapper;
import com.gabriel.tiziano.teamheritage.repository.StadiumRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StadiumService {

    private final StadiumRepository stadiumRepository;

    public StadiumService(StadiumRepository stadiumRepository) {
        this.stadiumRepository = stadiumRepository;
    }

    @Transactional(readOnly = true)
    public Page<StadiumResponse> getStadiums(Pageable pageable) {
        return stadiumRepository.findAll(pageable)
                .map(StadiumMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public StadiumResponse getStadiumById(Long stadiumId) {
        return StadiumMapper.toResponse(resolveStadium(stadiumId));
    }

    @Transactional
    public StadiumResponse createStadium(StadiumRequest stadiumRequest) {
        Stadium stadium = StadiumMapper.toEntity(stadiumRequest);
        return StadiumMapper.toResponse(stadiumRepository.save(stadium));
    }

    @Transactional
    public StadiumResponse updateStadium(Long id, StadiumRequest stadiumRequest) {
        Stadium stadium = resolveStadium(id);
        StadiumMapper.updateEntity(stadium, stadiumRequest);
        return StadiumMapper.toResponse(stadium);
    }

    @Transactional
    public void deleteStadium(Long id) {
        stadiumRepository.delete(resolveStadium(id));
    }

    private Stadium resolveStadium(Long stadiumId) {
        return stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new EntityNotFoundException("Stadium not found with id: " + stadiumId));
    }
}