package com.gabriel.tiziano.teamheritage.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class StadiumControllerIntegrationTest extends IntegrationTestSupport {

    @Autowired MockMvc mockMvc;

    @Test
    void getStadiums_withoutToken_shouldReturn401() throws Exception {
        mockMvc.perform(get("/stadiums"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getStadiums_withReadScope_shouldReturnSeededData() throws Exception {
        mockMvc.perform(get("/stadiums")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_stadium:read"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(60));
    }

    @Test
    void createStadium_withReadScope_shouldReturn403() throws Exception {
        mockMvc.perform(post("/stadiums")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_stadium:read")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Novo Estadio\",\"city\":\"Cidade\",\"capacity\":10000}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void createStadium_withWriteScope_shouldReturn201() throws Exception {
        mockMvc.perform(post("/stadiums")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_stadium:write")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Novo Estadio\",\"city\":\"Cidade\",\"capacity\":10000}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createStadium_withInvalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(post("/stadiums")
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_stadium:write")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"city\":\"Cidade\",\"capacity\":10000}"))
                .andExpect(status().isBadRequest());
    }
}