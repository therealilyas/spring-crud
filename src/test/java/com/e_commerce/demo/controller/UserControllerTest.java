package com.e_commerce.demo.controller;

import com.e_commerce.demo.dto.request.UserRequest;
import com.e_commerce.demo.dto.response.UserResponse;
import com.e_commerce.demo.exception.ResourceNotFoundException;
import com.e_commerce.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserResponse buildResponse() {
        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setFullName("John Doe");
        response.setEmail("john@example.com");
        return response;
    }

    private UserRequest buildRequest() {
        UserRequest request = new UserRequest();
        request.setFullName("John Doe");
        request.setEmail("john@example.com");
        return request;
    }

    @Test
    void create_returnsCreatedResponse() throws Exception {
        when(userService.save(any(UserRequest.class))).thenReturn(buildResponse());

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    void create_withInvalidRequest_returnsBadRequest() throws Exception {
        UserRequest invalid = new UserRequest();
        invalid.setFullName("");
        invalid.setEmail("not-an-email");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).save(any());
    }

    @Test
    void getAll_returnsListOfUsers() throws Exception {
        when(userService.getAll()).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("john@example.com"));
    }

    @Test
    void getById_whenFound_returnsUser() throws Exception {
        when(userService.getById(1L)).thenReturn(buildResponse());

        mockMvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getById_whenNotFound_returnsNotFound() throws Exception {
        when(userService.getById(99L)).thenThrow(new ResourceNotFoundException("User not found with id: 99"));

        mockMvc.perform(get("/users/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id: 99"));
    }

    @Test
    void update_returnsUpdatedUser() throws Exception {
        when(userService.update(eq(1L), any(UserRequest.class))).thenReturn(buildResponse());

        mockMvc.perform(put("/users/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        doNothing().when(userService).delete(1L);

        mockMvc.perform(delete("/users/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(userService).delete(1L);
    }
}
