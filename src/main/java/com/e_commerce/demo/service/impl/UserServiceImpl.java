package com.e_commerce.demo.service.impl;

import com.e_commerce.demo.dto.request.UserRequest;
import com.e_commerce.demo.dto.response.UserResponse;
import com.e_commerce.demo.entity.User;
import com.e_commerce.demo.exception.ResourceNotFoundException;
import com.e_commerce.demo.mapper.UserMapper;
import com.e_commerce.demo.repository.UserRepository;
import com.e_commerce.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return userMapper.toResponse(findUserOrThrow(id));
    }

    @Override
    public UserResponse save(UserRequest request) {
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse update(Long id, UserRequest request) {
        User existing = findUserOrThrow(id);
        userMapper.updateEntityFromRequest(request, existing);
        User updatedUser = userRepository.save(existing);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
