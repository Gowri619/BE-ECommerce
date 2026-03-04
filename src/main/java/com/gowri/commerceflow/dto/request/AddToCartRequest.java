package com.gowri.commerceflow.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {
    private long productId;
    private int quantity;
}
