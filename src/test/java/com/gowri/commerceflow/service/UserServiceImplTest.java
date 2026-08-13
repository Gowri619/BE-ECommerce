package com.gowri.commerceflow.service;

import com.gowri.commerceflow.dto.request.RegisterRequest;
import com.gowri.commerceflow.dto.response.RegisterResponse;
import com.gowri.commerceflow.entity.Role;
import com.gowri.commerceflow.entity.User;
import com.gowri.commerceflow.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void register_createsNewUser() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Gowri");
        request.setEmail("gowri@example.com");
        request.setPassword("password123");

        User savedUser = User.builder()
                .id(1L)
                .name("Gowri")
                .email("gowri@example.com")
                .password("encoded-password")
                .role(Role.USER)
                .build();

        when(userRepository.existsByEmail("gowri@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterResponse response = userService.register(request);

        verify(userRepository).save(any(User.class));
        assertEquals(1L, response.getId());
        assertEquals("gowri@example.com", response.getEmail());
        assertEquals("USER", response.getRole());
    }

    @Test
    void register_throwsWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Gowri");
        request.setEmail("gowri@example.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail("gowri@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.register(request));
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
