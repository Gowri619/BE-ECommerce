package com.gowri.commerceflow.dto.response;

import lombok.Builder;

@Builder
public record OrderItemResponse (
        long productId,
        String productName,
        double price,
        int quantity,
        double totalPrice
) {
}
