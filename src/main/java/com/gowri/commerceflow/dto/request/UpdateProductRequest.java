package com.gowri.commerceflow.dto.request;

import com.gowri.commerceflow.entity.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;

@Getter
@Setter
public class UpdateProductRequest {

    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Category category;
    private Boolean active;
}
