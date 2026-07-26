package com.gabriel.tiziano.teamheritage.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PlayerControllerIntegrationTest extends IntegrationTestSupport {

    @Autowired MockMvc mockMvc;

    @Test
    void getPlayers_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/players"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getPlayers_withWrongScope_shouldReturn403() throws Exception {
        mockMvc.perform(get("/players")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_club:read"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getPlayers_withReadScope_shouldReturnSeededData() throws Exception {
        mockMvc.perform(get("/players")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_player:read"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(600));
    }

    @Test
    void createPlayer_withWriteScope_shouldReturn201() throws Exception {
        mockMvc.perform(post("/players")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_player:write")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Novo Jogador\",\"position\":\"FORWARD\",\"shirtNumber\":50,\"clubId\":1}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createPlayer_withInvalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(post("/players")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_player:write")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Sem posicao\",\"shirtNumber\":50,\"clubId\":1}"))
                .andExpect(status().isBadRequest());
    }
}