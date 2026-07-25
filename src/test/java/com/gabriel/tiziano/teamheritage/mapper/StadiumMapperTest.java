package com.gabriel.tiziano.teamheritage.mapper;

import com.gabriel.tiziano.teamheritage.dto.request.StadiumRequest;
import com.gabriel.tiziano.teamheritage.dto.response.StadiumResponse;
import com.gabriel.tiziano.teamheritage.entities.Stadium;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class StadiumMapperTest {

    private final StadiumMapper mapper = Mappers.getMapper(StadiumMapper.class);

    @Test
    void toResponse() {
        //Triple A (AAA) pattern: Arrange, Act, Assert ou Given, When, Then
        //Arrange ou Given
        Stadium stadium = Stadium.builder()
                .id(1L)
                .name("Stadium Name")
                .city("City Name")
                .capacity(50000)
                .urlImg("https://example.com/stadium.jpg")
                .build();

        //Action ou When
        StadiumResponse stadiumResponse = mapper.toResponse(stadium);

        //Assertion ou Then
        //verifica se o valor não é nulo
        assertNotNull(stadiumResponse);

        //checa se o valor é o esperado/são iguais, em que o primeiro é o esperado e o segundo o que o código realmente retornou
        assertEquals(stadium.getId(), stadiumResponse.id());
        assertEquals(stadium.getName(), stadiumResponse.name());
        assertEquals(stadium.getCity(), stadiumResponse.city());
        assertEquals(stadium.getCapacity(), stadiumResponse.capacity());
        assertEquals(stadium.getUrlImg(), stadiumResponse.urlImg());
    }

    @Test
    void toEntity() {
        // Arrange ou Given
        StadiumRequest request = new StadiumRequest(
                "Stadium Name", "City Name", 50000, "https://example.com/stadium.jpg");

        // Action ou When
        Stadium stadium = mapper.toEntity(request);

        // Assert ou Then
        assertNotNull(stadium);
        assertNull(stadium.getId());
        assertEquals("Stadium Name", stadium.getName());
        assertEquals("City Name", stadium.getCity());
        assertEquals(50000, stadium.getCapacity());
        assertEquals("https://example.com/stadium.jpg", stadium.getUrlImg());
    }

}