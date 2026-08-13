package com.gowri.commerceflow.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomUserDetailsServiceTest {
    @Test
    void compileCheck() {
        assertNotNull(CustomUserDetailsService.class);
    }
}
