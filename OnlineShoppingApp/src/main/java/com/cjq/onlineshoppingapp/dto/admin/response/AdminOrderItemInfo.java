package com.cjq.onlineshoppingapp.dto.admin.response;

import com.cjq.onlineshoppingapp.model.OrderItem;
import lombok.Data;

@Data
public class AdminOrderItemInfo {
    private Long itemId;
    private Long productId;
    private String productName;
    private Double purchasedPrice;
    private Integer quantity;
    private Double wholesalePrice;

    public AdminOrderItemInfo(OrderItem orderItem) {
        this.itemId = orderItem.getItemId();
        this.productId = orderItem.getProduct().getProductId();
        this.productName = orderItem.getProduct().getName();
        this.purchasedPrice = orderItem.getPurchasedPrice();
        this.quantity = orderItem.getQuantity();
        this.wholesalePrice = orderItem.getWholesalePrice();
    }
}

