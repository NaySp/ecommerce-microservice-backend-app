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
public class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void whenGetAllProducts_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/products")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    
    @Test
    void whenGetProductById_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/products/1")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
    
    @Test
    void whenGetAllProductsWithContentType_thenVerify() throws Exception {
        mockMvc.perform(get("/api/products")
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
    
    @Test
    void whenGetProductByIdWithAccept_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/products/1")
                .contentType("application/json")
                .accept("application/json"))
                .andExpect(status().isOk());
    }
    
    @Test
    void whenGetMultipleProducts_thenChainRequests() throws Exception {
        mockMvc.perform(get("/api/products")
                .contentType("application/json"))
                .andExpect(status().isOk());
        
        mockMvc.perform(get("/api/products/1")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}
