package com.e_commerce.demo.mapper;

import com.e_commerce.demo.dto.request.UserRequest;
import com.e_commerce.demo.dto.response.UserResponse;
import com.e_commerce.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    void updateEntityFromRequest(UserRequest request, @MappingTarget User user);
}
