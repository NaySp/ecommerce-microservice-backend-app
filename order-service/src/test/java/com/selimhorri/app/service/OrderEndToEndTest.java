package com.selimhorri.app.service;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OrderEndToEndTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void listAllOrdersE2E() throws Exception {
        // E2E: Obtener todas las órdenes
        mockMvc.perform(get("/api/orders")
            .contentType("application/json"))
            .andExpect(status().isOk());
    }
    
    @Test
    void getOrderByIdE2E() throws Exception {
        // E2E: Obtener orden específica por ID
        mockMvc.perform(get("/api/orders/1")
            .contentType("application/json"))
            .andExpect(status().isOk());
    }
    
    @Test
    void orderListingWithAccept() throws Exception {
        // E2E: Obtener órdenes con header Accept
        mockMvc.perform(get("/api/orders")
            .contentType("application/json")
            .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"));
    }
    
    @Test
    void orderMultipleRetrievalFlow() throws Exception {
        // Obtener todas las órdenes
        mockMvc.perform(get("/api/orders")
            .contentType("application/json"))
            .andExpect(status().isOk());
        
        // Luego obtener orden específica
        mockMvc.perform(get("/api/orders/1")
            .contentType("application/json"))
            .andExpect(status().isOk());
    }
}
