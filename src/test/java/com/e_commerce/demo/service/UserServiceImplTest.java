package com.e_commerce.demo.service;

import com.e_commerce.demo.dto.request.UserRequest;
import com.e_commerce.demo.dto.response.UserResponse;
import com.e_commerce.demo.entity.User;
import com.e_commerce.demo.exception.ResourceNotFoundException;
import com.e_commerce.demo.mapper.UserMapper;
import com.e_commerce.demo.repository.UserRepository;
import com.e_commerce.demo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFullName("John Doe");
        user.setEmail("john@example.com");

        userRequest = new UserRequest();
        userRequest.setFullName("John Doe");
        userRequest.setEmail("john@example.com");

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setFullName("John Doe");
        userResponse.setEmail("john@example.com");
    }

    @Test
    void getAll_returnsMappedResponses() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        List<UserResponse> result = userService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("john@example.com");
        verify(userRepository).findAll();
    }

    @Test
    void getById_whenFound_returnsResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFullName()).isEqualTo("John Doe");
    }

    @Test
    void getById_whenNotFound_throwsResourceNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void save_persistsAndReturnsResponse() {
        when(userMapper.toEntity(userRequest)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.save(userRequest);

        assertThat(result).isEqualTo(userResponse);
        verify(userRepository).save(user);
    }

    @Test
    void update_whenFound_updatesAndReturnsResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.update(1L, userRequest);

        assertThat(result).isEqualTo(userResponse);
        verify(userMapper).updateEntityFromRequest(userRequest, user);
        verify(userRepository).save(user);
    }

    @Test
    void update_whenNotFound_throwsResourceNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(99L, userRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void delete_whenExists_deletesUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void delete_whenNotExists_throwsResourceNotFoundException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository, never()).deleteById(anyLong());
    }
}
