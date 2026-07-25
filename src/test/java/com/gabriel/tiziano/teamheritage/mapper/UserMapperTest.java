package com.gabriel.tiziano.teamheritage.mapper;

import com.gabriel.tiziano.teamheritage.dto.request.UserRequest;
import com.gabriel.tiziano.teamheritage.dto.response.UserResponse;
import com.gabriel.tiziano.teamheritage.entities.Scope;
import com.gabriel.tiziano.teamheritage.entities.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntity() {
        UserRequest request = new UserRequest(
                "Gabriel", "gabriel@teamheritage.com", "senhaSegura123", List.of(1L, 2L));

        User user = mapper.toEntity(request);

        assertNotNull(user);
        assertEquals("Gabriel", user.getName());
        assertEquals("gabriel@teamheritage.com", user.getEmail());
        assertNull(user.getId());
        assertNull(user.getPassword());
        assertFalse(user.isActive());
        assertTrue(user.getScopes().isEmpty());
    }

    @Test
    void toResponse() {
        Scope read = Scope.builder().id(1L).name("club:read").build();
        Scope write = Scope.builder().id(2L).name("club:write").build();
        User user = User.builder()
                .id(1L)
                .name("Gabriel")
                .email("gabriel@teamheritage.com")
                .password("$2b$10$hashed")
                .active(true)
                .scopes(List.of(read, write))
                .build();

        UserResponse response = mapper.toResponse(user);

        assertNotNull(response);
        assertEquals("Gabriel", response.name());
        assertTrue(response.active());
        assertEquals(List.of("club:read", "club:write"), response.scopes());
    }
}