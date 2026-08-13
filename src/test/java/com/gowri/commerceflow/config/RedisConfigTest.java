package com.gowri.commerceflow.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RedisConfigTest {
    @Test
    void compileCheck() {
        assertNotNull(RedisConfig.class);
    }
}
