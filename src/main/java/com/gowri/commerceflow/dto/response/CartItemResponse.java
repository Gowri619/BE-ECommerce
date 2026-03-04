package com.gowri.commerceflow.dto.response;

import lombok.Builder;

@Builder
public record CartItemResponse(
        Long productId,
        String productName,
        Double price,
        Integer quantity,
        Double totalPrice
) {
}
