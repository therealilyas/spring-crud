package com.e_commerce.demo.dto.response;

import lombok.Data;

@Data
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;

}