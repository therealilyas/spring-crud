package com.example.orderservice.client;

import com.example.orderservice.dto.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {

    private final RestTemplate restTemplate;
    private final String userServiceUrl;

    public UserClient(
            RestTemplate restTemplate,
            @Value("${user-service.url}") String userServiceUrl
    ) {
        this.restTemplate = restTemplate;
        this.userServiceUrl = userServiceUrl;
    }

    public UserResponse getUser(Long userId) {
        try {
            return restTemplate.getForObject(
                    userServiceUrl + "/api/users/" + userId,
                    UserResponse.class
            );
        } catch (RestClientException ex) {
            throw new IllegalArgumentException(
                    "User not found with id: " + userId
            );
        }
    }
}