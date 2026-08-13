package com.gowri.commerceflow.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtAuthenticationFilterTest {
    @Test
    void compileCheck() {
        assertNotNull(JwtAuthenticationFilter.class);
    }
}
