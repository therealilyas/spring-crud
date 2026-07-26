package com.e_commerce.demo.mapper;

import com.e_commerce.demo.dto.request.OrderRequest;
import com.e_commerce.demo.dto.response.OrderResponse;
import com.e_commerce.demo.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    // user is resolved separately by the service (it requires a repository lookup)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "payment", ignore = true)
    Order toEntity(OrderRequest request);

    @Mapping(target = "userId", source = "user.id")
    OrderResponse toResponse(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "payment", ignore = true)
    void updateEntityFromRequest(OrderRequest request, @MappingTarget Order order);
}
