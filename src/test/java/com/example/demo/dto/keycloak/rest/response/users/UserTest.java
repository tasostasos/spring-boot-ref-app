package com.example.demo.dto.keycloak.rest.response.users;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {
    @Test
    public void testUserDTO() {
        // Create a User object with some sample data
        User user = new User();
        user.setTotp(true);
        user.setLastName("Smith");

        user.setCreatedTimestamp(1620211200000L);
        user.setEnabled(true);
        user.setNotBefore(0);
        user.setDisableableCredentialTypes(new ArrayList<>());
        user.setEmailVerified(true);
        user.setFirstName("John");
        user.setRequiredActions(new ArrayList<>());
        user.setId("123456");
        user.setEmail("john.smith@example.com");
        user.setUsername("johnsmith");

        // Assert that the getters return the correct values
        assertTrue(user.isTotp());
        assertEquals("Smith", user.getLastName());
        assertEquals(1620211200000L, user.getCreatedTimestamp());
        assertTrue(user.isEnabled());
        assertEquals(0, user.getNotBefore());
        assertEquals(new ArrayList<>(), user.getDisableableCredentialTypes());
        assertTrue(user.isEmailVerified());
        assertEquals("John", user.getFirstName());
        assertEquals(new ArrayList<>(), user.getRequiredActions());
        assertEquals("123456", user.getId());
        assertEquals("john.smith@example.com", user.getEmail());
        assertEquals("johnsmith", user.getUsername());
    }
}