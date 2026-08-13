package com.gowri.commerceflow.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtServiceTest {
    @Test
    void compileCheck() {
        assertNotNull(JwtService.class);
    }
}
