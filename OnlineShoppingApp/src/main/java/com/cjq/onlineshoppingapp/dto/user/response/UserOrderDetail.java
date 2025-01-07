package com.cjq.onlineshoppingapp.dto.user.response;

import com.cjq.onlineshoppingapp.dto.admin.response.AdminOrderDetail;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UserOrderDetail {
    private Long orderId;
    private LocalDateTime datePlaced;
    private String orderStatus;
    private Long userId;
    private Set<UserOrderItemInfo> orderItems;

    public UserOrderDetail(AdminOrderDetail adminOrderDetail) {
        this.orderId = adminOrderDetail.getOrderId();
        this.datePlaced = adminOrderDetail.getDatePlaced();
        this.orderStatus = adminOrderDetail.getOrderStatus();
        this.userId = adminOrderDetail.getUserId();
        this.orderItems = adminOrderDetail.getOrderItems().stream()
                .map(UserOrderItemInfo::new)
                .collect(Collectors.toSet());
    }
}