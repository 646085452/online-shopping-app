package com.cjq.onlineshoppingapp.dto.admin.request;

import lombok.Data;

@Data
public class ProductUpdateRequest {
    private String name;
    private String description;
    private Integer quantity;
    private Double retailPrice;
    private Double wholesalePrice;
}

