package com.gabriel.tiziano.teamheritage.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClubControllerIntegrationTest extends IntegrationTestSupport {

    @Autowired MockMvc mockMvc;

    @Test
    void getClubs_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/clubs"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getClubs_withWrongScope_shouldReturn403() throws Exception {
        mockMvc.perform(get("/clubs")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_player:read"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void getClubs_withReadScope_shouldReturnSeededData() throws Exception {
        mockMvc.perform(get("/clubs")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_club:read"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(60));
    }

    @Test
    void createClub_withReadScope_shouldReturn403() throws Exception {
        mockMvc.perform(post("/clubs")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_club:read")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Novo Clube\",\"founded\":\"2000-01-01\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createClub_withWriteScope_shouldReturn201() throws Exception {
        mockMvc.perform(post("/clubs")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_club:write")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Novo Clube\",\"founded\":\"2000-01-01\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createClub_withInvalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(post("/clubs")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_club:write")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"founded\":\"2000-01-01\"}"))
                .andExpect(status().isBadRequest());
    }
}