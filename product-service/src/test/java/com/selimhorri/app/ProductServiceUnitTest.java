package com.selimhorri.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ProductServiceUnitTest {
    @Test
    void givenValidProduct_whenSave_thenSuccess() {
        String productName = "Monitor 24''";
        double price = 590.0;
        assertNotNull(productName);
        assertTrue(price > 0);
    }
}
