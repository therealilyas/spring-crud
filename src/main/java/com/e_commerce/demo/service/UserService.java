package com.e_commerce.demo.service;

import com.e_commerce.demo.dto.request.UserRequest;
import com.e_commerce.demo.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> getAll();

    UserResponse getById(Long id);

    UserResponse save(UserRequest request);

    UserResponse update(Long id, UserRequest request);

    void delete(Long id);
}
