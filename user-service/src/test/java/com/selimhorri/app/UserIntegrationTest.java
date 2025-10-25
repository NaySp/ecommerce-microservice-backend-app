package com.selimhorri.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenGetAllUsers_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    
    @Test
    void whenGetUserById_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/users/1")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    
    @Test
    void whenGetAllUsersWithContentType_thenVerifyResponse() throws Exception {
        mockMvc.perform(get("/api/users")
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}