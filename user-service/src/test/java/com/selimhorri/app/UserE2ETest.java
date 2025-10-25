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
class UserE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void completeUserFlowE2E() throws Exception {
        // E2E Flujo 1: Obtener todos los usuarios
        mockMvc.perform(get("/api/users")
                .contentType("application/json"))
                .andExpect(status().isOk());
        
        // E2E Flujo 2: Obtener usuario específico por ID
        mockMvc.perform(get("/api/users/1")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    
    @Test
    void userRetrievalE2E() throws Exception {
        // Obtener todos
        mockMvc.perform(get("/api/users")
                .contentType("application/json"))
                .andExpect(status().isOk());
        
        // Obtener por ID
        mockMvc.perform(get("/api/users/1")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}