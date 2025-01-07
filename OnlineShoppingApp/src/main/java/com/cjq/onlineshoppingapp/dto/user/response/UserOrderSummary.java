package com.cjq.onlineshoppingapp.dto.user.response;

import lombok.Data;
import com.cjq.onlineshoppingapp.model.Order;
import java.time.LocalDateTime;

@Data
public class UserOrderSummary {
    private Long orderId;
    private LocalDateTime datePlaced;
    private String orderStatus;
    private Long userId;

    public UserOrderSummary(Order order) {
        this.orderId = order.getOrderId();
        this.datePlaced = order.getDatePlaced();
        this.orderStatus = order.getOrderStatus();
        this.userId = order.getUser().getUserId();
    }
}
