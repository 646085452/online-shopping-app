package com.cjq.onlineshoppingapp.dto.admin.response;

import lombok.Data;
import com.cjq.onlineshoppingapp.model.Order;
import java.time.LocalDateTime;

@Data
public class AdminOrderSummary {
    private Long orderId;
    private LocalDateTime datePlaced;
    private String orderStatus;
    private Long userId;

    public AdminOrderSummary(Order order) {
        this.orderId = order.getOrderId();
        this.datePlaced = order.getDatePlaced();
        this.orderStatus = order.getOrderStatus();
        this.userId = order.getUser().getUserId();
    }
}

