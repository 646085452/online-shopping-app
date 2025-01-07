package com.cjq.onlineshoppingapp.dto.admin.response;

import com.cjq.onlineshoppingapp.model.Product;
import lombok.Data;

@Data
public class AdminAdvanceProductInfo {
    private Long productId;
    private String name;
    private Double retailPrice;
    private Double wholesalePrice;
    private Long totalSoldAmount;

    public AdminAdvanceProductInfo(Product product, Long totalSoldAmount) {
        this.name = product.getName();
        this.productId = product.getProductId();
        this.retailPrice = product.getRetailPrice();
        this.wholesalePrice = product.getWholesalePrice();
        this.totalSoldAmount = totalSoldAmount;
    }
}
