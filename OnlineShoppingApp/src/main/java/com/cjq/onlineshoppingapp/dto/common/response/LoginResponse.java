package com.cjq.onlineshoppingapp.dto.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String message;
    private String role;
    private String token;
}
