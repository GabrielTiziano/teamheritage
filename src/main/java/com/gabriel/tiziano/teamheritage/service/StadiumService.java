package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.dto.request.StadiumRequest;
import com.gabriel.tiziano.teamheritage.dto.response.StadiumResponse;
import com.gabriel.tiziano.teamheritage.entities.Stadium;
import com.gabriel.tiziano.teamheritage.exception.ResourceNotFoundException;
import com.gabriel.tiziano.teamheritage.mapper.StadiumMapper;
import com.gabriel.tiziano.teamheritage.repository.StadiumRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StadiumService {

    private final StadiumRepository stadiumRepository;
    private final StadiumMapper stadiumMapper;

    public StadiumService(StadiumRepository stadiumRepository, StadiumMapper stadiumMapper) {
        this.stadiumRepository = stadiumRepository;
        this.stadiumMapper = stadiumMapper;
    }

    @Transactional(readOnly = true)
    public Page<StadiumResponse> getStadiums(Pageable pageable) {
        return stadiumRepository.findAll(pageable)
                .map(stadiumMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public StadiumResponse getStadiumById(Long stadiumId) {
        return stadiumMapper.toResponse(resolveStadium(stadiumId));
    }

    @Transactional
    public StadiumResponse createStadium(StadiumRequest stadiumRequest) {
        Stadium stadium = stadiumMapper.toEntity(stadiumRequest);
        return stadiumMapper.toResponse(stadiumRepository.save(stadium));
    }

    @Transactional
    public StadiumResponse updateStadium(Long id, StadiumRequest stadiumRequest) {
        Stadium stadium = resolveStadium(id);
        stadiumMapper.updateEntity(stadium, stadiumRequest);
        return stadiumMapper.toResponse(stadium);
    }

    @Transactional
    public void deleteStadium(Long id) {
        stadiumRepository.delete(resolveStadium(id));
    }

    private Stadium resolveStadium(Long stadiumId) {
        return stadiumRepository.findById(stadiumId)
                .orElseThrow(() -> new ResourceNotFoundException("Stadium not found with id: " + stadiumId));
    }
}