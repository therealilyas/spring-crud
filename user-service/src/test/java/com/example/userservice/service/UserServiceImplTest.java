package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.UserUpdateRequest;
import com.example.userservice.entity.User;
import com.example.userservice.exception.ResourceNotFoundException;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Ilyas", "ilyas@example.com");
    }

    @Test
    void create_shouldSaveUser() {
        UserRequest request = new UserRequest();
        request.setName("Ilyas");
        request.setEmail("ilyas@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.create(request);

        assertEquals(1L, response.getId());
        assertEquals("Ilyas", response.getName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getById_shouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.getById(1L);

        assertEquals("ilyas@example.com", response.getEmail());
    }

    @Test
    void getById_shouldThrowWhenMissing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getById(99L));
    }

    @Test
    void getAll_shouldReturnUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        assertEquals(1, userService.getAll().size());
    }

    @Test
    void update_shouldUpdateUser() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setName("Updated");
        request.setEmail("updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.update(1L, request);

        assertEquals("Updated", user.getName());
        verify(userRepository).save(user);
    }

    @Test
    void delete_shouldDeleteExistingUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }
}
