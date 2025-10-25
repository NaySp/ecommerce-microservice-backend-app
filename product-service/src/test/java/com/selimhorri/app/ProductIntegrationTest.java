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
class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenGetAllProducts_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/products")
            .contentType("application/json"))
            .andExpect(status().isOk());
    }
}
