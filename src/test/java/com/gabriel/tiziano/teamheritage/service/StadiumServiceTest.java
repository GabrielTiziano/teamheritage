package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.dto.request.StadiumRequest;
import com.gabriel.tiziano.teamheritage.dto.response.StadiumResponse;
import com.gabriel.tiziano.teamheritage.entities.Stadium;
import com.gabriel.tiziano.teamheritage.exception.ResourceNotFoundException;
import com.gabriel.tiziano.teamheritage.mapper.StadiumMapper;
import com.gabriel.tiziano.teamheritage.repository.StadiumRepository;
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
class StadiumServiceTest {

    @Mock StadiumRepository stadiumRepository;
    @Mock StadiumMapper stadiumMapper;

    @InjectMocks StadiumService stadiumService;

    @Test
    void getStadiums_shouldReturnPageOfStadiums() {
        Pageable pageable = PageRequest.of(0, 20);
        Stadium stadium = Stadium.builder().id(1L).name("Bernabeu").build();
        StadiumResponse response = new StadiumResponse(1L, "Bernabeu", "Madrid", 80000, null);

        when(stadiumRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(stadium)));
        when(stadiumMapper.toResponse(stadium)).thenReturn(response);

        Page<StadiumResponse> result = stadiumService.getStadiums(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void getStadiumById_whenExists_shouldReturnStadium() {
        Stadium stadium = Stadium.builder().id(1L).name("Bernabeu").build();
        StadiumResponse response = new StadiumResponse(1L, "Bernabeu", "Madrid", 80000, null);

        when(stadiumRepository.findById(1L)).thenReturn(Optional.of(stadium));
        when(stadiumMapper.toResponse(stadium)).thenReturn(response);

        StadiumResponse result = stadiumService.getStadiumById(1L);

        assertEquals("Bernabeu", result.name());
    }

    @Test
    void getStadiumById_whenNotFound_shouldThrowException() {
        when(stadiumRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> stadiumService.getStadiumById(999L));
        verify(stadiumMapper, never()).toResponse(any());
    }

    @Test
    void createStadium_shouldSaveAndReturnResponse() {
        StadiumRequest request = new StadiumRequest("Bernabeu", "Madrid", 80000, null);
        Stadium stadium = Stadium.builder().name("Bernabeu").build();
        Stadium saved = Stadium.builder().id(1L).name("Bernabeu").build();
        StadiumResponse response = new StadiumResponse(1L, "Bernabeu", "Madrid", 80000, null);

        when(stadiumMapper.toEntity(request)).thenReturn(stadium);
        when(stadiumRepository.save(stadium)).thenReturn(saved);
        when(stadiumMapper.toResponse(saved)).thenReturn(response);

        StadiumResponse result = stadiumService.createStadium(request);

        assertEquals(1L, result.id());
        verify(stadiumRepository).save(stadium);
    }

    @Test
    void updateStadium_shouldUpdateWithoutCallingSave() {
        StadiumRequest request = new StadiumRequest("New Name", "Madrid", 80000, null);
        Stadium stadium = Stadium.builder().id(1L).name("Bernabeu").build();
        StadiumResponse response = new StadiumResponse(1L, "New Name", "Madrid", 80000, null);

        when(stadiumRepository.findById(1L)).thenReturn(Optional.of(stadium));
        when(stadiumMapper.toResponse(stadium)).thenReturn(response);

        stadiumService.updateStadium(1L, request);

        verify(stadiumMapper).updateEntity(stadium, request);
        verify(stadiumRepository, never()).save(any());
    }

    @Test
    void deleteStadium_shouldDeletePhysically() {
        Stadium stadium = Stadium.builder().id(1L).name("Bernabeu").build();
        when(stadiumRepository.findById(1L)).thenReturn(Optional.of(stadium));

        stadiumService.deleteStadium(1L);

        verify(stadiumRepository).delete(stadium);
    }
}