package com.gabriel.tiziano.teamheritage.service;

import com.gabriel.tiziano.teamheritage.dto.request.ClubRequest;
import com.gabriel.tiziano.teamheritage.dto.response.ClubResponse;
import com.gabriel.tiziano.teamheritage.entities.Club;
import com.gabriel.tiziano.teamheritage.entities.Stadium;
import com.gabriel.tiziano.teamheritage.exception.ResourceNotFoundException;
import com.gabriel.tiziano.teamheritage.mapper.ClubMapper;
import com.gabriel.tiziano.teamheritage.repository.ClubRepository;
import com.gabriel.tiziano.teamheritage.repository.StadiumRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubServiceTest {

    @Mock ClubRepository clubRepository;
    @Mock StadiumRepository stadiumRepository;
    @Mock ClubMapper clubMapper;

    @InjectMocks ClubService clubService;

    @Test
    void getClubs_shouldReturnPageOfClubs() {
        Pageable pageable = PageRequest.of(0, 20);
        Club club = Club.builder().id(1L).name("Real Madrid").build();
        ClubResponse response = new ClubResponse(1L, "Real Madrid", null, null, null, true, null);

        when(clubRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(club)));
        when(clubMapper.toResponse(club)).thenReturn(response);

        Page<ClubResponse> result = clubService.getClubs(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Real Madrid", result.getContent().get(0).name());
    }

    @Test
    void getClubById_whenExists_shouldReturnClub() {
        Club club = Club.builder().id(1L).name("Real Madrid").build();
        ClubResponse response = new ClubResponse(1L, "Real Madrid", null, null, null, true, null);

        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(clubMapper.toResponse(club)).thenReturn(response);

        ClubResponse result = clubService.getClubById(1L);

        assertNotNull(result);
        assertEquals("Real Madrid", result.name());
    }

    @Test
    void getClubById_whenNotFound_shouldThrowException() {
        when(clubRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clubService.getClubById(999L));
        verify(clubMapper, never()).toResponse(any());
    }

    @Test
    void createClub_shouldSaveAndReturnResponse() {
        ClubRequest request = new ClubRequest("Real Madrid", LocalDate.of(1902, 3, 6), null, 5L);
        Stadium stadium = Stadium.builder().id(5L).name("Bernabeu").build();
        Club club = Club.builder().name("Real Madrid").stadium(stadium).build();
        Club saved = Club.builder().id(1L).name("Real Madrid").stadium(stadium).build();
        ClubResponse response = new ClubResponse(1L, "Real Madrid", null, null, null, true, null);

        when(stadiumRepository.findById(5L)).thenReturn(Optional.of(stadium));
        when(clubMapper.toEntity(request, stadium)).thenReturn(club);
        when(clubRepository.save(club)).thenReturn(saved);
        when(clubMapper.toResponse(saved)).thenReturn(response);

        ClubResponse result = clubService.createClub(request);

        assertEquals(1L, result.id());
        verify(clubRepository).save(club);
    }

    @Test
    void createClub_whenStadiumNotFound_shouldThrowException() {
        ClubRequest request = new ClubRequest("Real Madrid", LocalDate.of(1902, 3, 6), null, 999L);

        when(stadiumRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clubService.createClub(request));
        verify(clubRepository, never()).save(any());
    }

    @Test
    void updateClub_shouldUpdateWithoutCallingSave() {
        ClubRequest request = new ClubRequest("New Name", LocalDate.of(1902, 3, 6), null, 5L);
        Club club = Club.builder().id(1L).name("Real Madrid").build();
        Stadium stadium = Stadium.builder().id(5L).name("Bernabeu").build();
        ClubResponse response = new ClubResponse(1L, "New Name", null, null, null, true, null);

        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(stadiumRepository.findById(5L)).thenReturn(Optional.of(stadium));
        when(clubMapper.toResponse(club)).thenReturn(response);

        clubService.updateClub(1L, request);

        verify(clubMapper).updateEntity(club, request, stadium);
        verify(clubRepository, never()).save(any());
    }

    @Test
    void deleteClub_shouldPerformSoftDelete() {
        Club club = Club.builder().id(1L).name("Real Madrid").active(true).build();

        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));

        clubService.deleteClub(1L);

        assertFalse(club.getActive());
        verify(clubRepository, never()).delete(any());
    }
}