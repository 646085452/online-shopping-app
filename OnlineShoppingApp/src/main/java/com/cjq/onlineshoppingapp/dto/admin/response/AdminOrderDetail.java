package com.cjq.onlineshoppingapp.dto.admin.response;

import com.cjq.onlineshoppingapp.model.Order;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AdminOrderDetail {
    private Long orderId;
    private LocalDateTime datePlaced;
    private String orderStatus;
    private Long userId;
    private Set<AdminOrderItemInfo> orderItems;

    public AdminOrderDetail(Order order) {
        this.orderId = order.getOrderId();
        this.datePlaced = order.getDatePlaced();
        this.orderStatus = order.getOrderStatus();
        this.userId = order.getUser().getUserId();
        this.orderItems = order.getOrderItems().stream()
                .map(AdminOrderItemInfo::new)
                .collect(Collectors.toSet());
    }
}
