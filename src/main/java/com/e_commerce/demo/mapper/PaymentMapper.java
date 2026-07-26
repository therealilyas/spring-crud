package com.e_commerce.demo.mapper;

import com.e_commerce.demo.dto.request.PaymentRequest;
import com.e_commerce.demo.dto.response.PaymentResponse;
import com.e_commerce.demo.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    // order is resolved separately by the service (it requires a repository lookup)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    Payment toEntity(PaymentRequest request);

    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toResponse(Payment payment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    void updateEntityFromRequest(PaymentRequest request, @MappingTarget Payment payment);
}
