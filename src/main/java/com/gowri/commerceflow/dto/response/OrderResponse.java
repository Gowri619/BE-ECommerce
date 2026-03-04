package com.gowri.commerceflow.dto.response;

import com.gowri.commerceflow.entity.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponse (
        long orderId,
        OrderStatus orderStatus,
        double totalAmount,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
}
