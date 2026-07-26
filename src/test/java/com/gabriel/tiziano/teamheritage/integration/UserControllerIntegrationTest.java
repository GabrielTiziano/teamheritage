package com.gabriel.tiziano.teamheritage.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerIntegrationTest extends IntegrationTestSupport {

    @Autowired MockMvc mockMvc;

    @Test
    void createUser_withValidBody_shouldReturn201() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Novo\",\"email\":\"novo@teamheritage.com\",\"password\":\"senha12345\",\"scopes\":[3]}"))
                .andExpect(status().isCreated());
    }

    @Test
    void createUser_withDuplicateEmail_shouldReturn409() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Duplicado\",\"email\":\"admin@teamheritage.com\",\"password\":\"senha12345\",\"scopes\":[3]}"))
                .andExpect(status().isConflict());
    }

    @Test
    void createUser_withInvalidBody_shouldReturn400() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Sem email\",\"password\":\"senha12345\",\"scopes\":[3]}"))
                .andExpect(status().isBadRequest());
    }
}