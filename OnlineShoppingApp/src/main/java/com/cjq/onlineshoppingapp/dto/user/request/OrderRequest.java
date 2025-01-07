package com.cjq.onlineshoppingapp.dto.user.request;

import lombok.Data;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderRequest {
    @NotNull
    private List<OrderItemRequest> order;
}

