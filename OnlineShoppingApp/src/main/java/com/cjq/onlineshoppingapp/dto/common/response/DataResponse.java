package com.cjq.onlineshoppingapp.dto.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DataResponse {
    private Boolean success;
    private String message;
    private Object data;
}
