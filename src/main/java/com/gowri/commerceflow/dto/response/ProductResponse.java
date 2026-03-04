package com.gowri.commerceflow.dto.response;

import com.gowri.commerceflow.entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductResponse {
    private long id;
    private String name;
    private String description;
    private double price;
    private  int stockQuantity;
    private Category category;
}
