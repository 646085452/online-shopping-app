package com.cjq.onlineshoppingapp.dto.user.response;

import com.cjq.onlineshoppingapp.model.Product;
import lombok.Data;

@Data
public class UserProductInfo {
    private Long productId;
    private String name;
    private String description;
    private Double retailPrice;

    public UserProductInfo(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.retailPrice = product.getRetailPrice();
    }
}
