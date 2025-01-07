package com.cjq.onlineshoppingapp.dto.admin.response;

import com.cjq.onlineshoppingapp.model.Product;
import lombok.Data;

@Data
public class AdminProductInfo {
    private Long productId;
    private String description;
    private String name;
    private Integer quantity;
    private Double retailPrice;
    private Double wholesalePrice;

    public AdminProductInfo(Product product) {
        this.productId = product.getProductId();
        this.description = product.getDescription();
        this.name = product.getName();
        this.quantity = product.getQuantity();
        this.retailPrice = product.getRetailPrice();
        this.wholesalePrice = product.getWholesalePrice();
    }
}
