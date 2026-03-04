package com.gowri.commerceflow.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CartResponse(
        Long cartId,
        List<CartItemResponse> items,
        Double grandTotal
) {
}
