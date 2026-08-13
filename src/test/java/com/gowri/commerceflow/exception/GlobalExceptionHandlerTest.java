package com.gowri.commerceflow.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    @Test
    void compileCheck() {
        assertNotNull(GlobalExceptionHandler.class);
    }
}
