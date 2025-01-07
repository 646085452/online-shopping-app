package com.cjq.onlineshoppingapp.dto.user.response;

import com.cjq.onlineshoppingapp.dto.admin.response.AdminOrderItemInfo;
import lombok.Data;

@Data
public class UserOrderItemInfo {
    private Long itemId;
    private Long productId;
    private String productName;
    private Double purchasedPrice;
    private Integer quantity;

    public UserOrderItemInfo(AdminOrderItemInfo adminOrderItemInfo) {
        this.itemId = adminOrderItemInfo.getItemId();
        this.productId = adminOrderItemInfo.getProductId();
        this.productName = adminOrderItemInfo.getProductName();
        this.purchasedPrice = adminOrderItemInfo.getPurchasedPrice();
        this.quantity = adminOrderItemInfo.getQuantity();
    }
}
