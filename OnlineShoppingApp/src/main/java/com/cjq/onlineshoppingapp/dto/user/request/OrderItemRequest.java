package com.cjq.onlineshoppingapp.dto.user.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderItemRequest {
    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;
}
