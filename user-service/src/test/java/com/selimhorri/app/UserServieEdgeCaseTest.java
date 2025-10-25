package com.selimhorri.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import com.selimhorri.app.dto.UserDto;

class UserServieEdgeCaseTest {
    
    @Test
    void testNullEmailHandling() {
        UserDto user = UserDto.builder()
                .userId(1)
                .firstName("John")
                .lastName("Doe")
                .email(null)
                .build();
        
        assertNull(user.getEmail(), "Email should be null when not provided");
    }
    
    @Test
    void testEmptyPhoneNumber() {
        UserDto user = UserDto.builder()
                .userId(2)
                .firstName("Jane")
                .lastName("Smith")
                .phone("")
                .build();
        
        assertTrue(user.getPhone().isEmpty(), "Phone should be empty string");
    }
}
