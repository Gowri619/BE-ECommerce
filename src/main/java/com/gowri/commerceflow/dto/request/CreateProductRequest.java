package com.gowri.commerceflow.dto.request;

import com.gowri.commerceflow.entity.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Positive
    private double price;

    @NotNull
    @Min(0)
    private int stockQuantity;

    @NotNull
    private Category category;
}
