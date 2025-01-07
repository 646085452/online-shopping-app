package com.cjq.onlineshoppingapp.dto.admin.request;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ProductCreateRequest {
    @NotNull
    private String name;

    private String description;

    @NotNull
    private Integer quantity;

    @NotNull
    private Double retailPrice;

    @NotNull
    private Double wholesalePrice;
}

