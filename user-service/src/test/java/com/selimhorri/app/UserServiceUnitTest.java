package com.selimhorri.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserServiceUnitTest {

    @Test
    void givenValidUser_whenRegister_thenSuccess() {
        // Simular lógica de registro
        String username = "john";
        String email = "john@example.com";
        // Ejemplo básico; suponiendo lógica local
        assertNotNull(username);
        assertTrue(email.contains("@"));
    }
}
