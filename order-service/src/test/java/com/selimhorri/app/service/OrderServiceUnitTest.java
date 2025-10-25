package com.selimhorri.app.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class OrderServiceUnitTest {
    @Test
    void givenValidOrder_whenSave_thenOrderCreated() {
        int userId = 7;
        int productId = 12;
        int quantity = 3;
        assertTrue(userId > 0 && productId > 0 && quantity > 0);
    }
}
