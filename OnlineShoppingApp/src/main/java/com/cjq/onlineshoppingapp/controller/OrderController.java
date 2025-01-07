package com.cjq.onlineshoppingapp.controller;

import com.cjq.onlineshoppingapp.dto.admin.response.AdminOrderDetail;
import com.cjq.onlineshoppingapp.dto.admin.response.AdminOrderSummary;
import com.cjq.onlineshoppingapp.dto.common.response.DataResponse;
import com.cjq.onlineshoppingapp.dto.user.request.OrderRequest;
import com.cjq.onlineshoppingapp.dto.user.response.UserOrderDetail;
import com.cjq.onlineshoppingapp.dto.user.response.UserOrderSummary;
import com.cjq.onlineshoppingapp.dto.user.response.UserProductInfo;
import com.cjq.onlineshoppingapp.exception.InsufficientStockException;
import com.cjq.onlineshoppingapp.service.OrderService;
import com.cjq.onlineshoppingapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.crypto.Data;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.cjq.onlineshoppingapp.util.constant.ResponseConstant.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public DataResponse getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            List<AdminOrderSummary> adminOrderSummaries = orderService.getAllOrders();
            return DataResponse.builder()
                    .success(true)
                    .message("Get admin order summary successful")
                    .data(adminOrderSummaries)
                    .build();
        }

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            String username = authentication.getName();
            Long userId = userService.findUserIdByUsername(username);
            List<UserOrderSummary> userOrderSummaries = orderService.getAllUserOrdersByUserId(userId);
            return DataResponse.builder()
                    .success(true)
                    .message("Get user order summary successful")
                    .data(userOrderSummaries)
                    .build();
        }

        return DataResponse.builder()
                .success(false)
                .message(NO_AUTHORITY)
                .data(null)
                .build();
    }

    @GetMapping("/{id}")
    public DataResponse getOrderDetailById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            AdminOrderDetail adminOrderDetail = orderService.getOrderDetailById(id);
            return DataResponse.builder()
                    .success(true)
                    .message("Get admin order detail successful")
                    .data(adminOrderDetail)
                    .build();
        }

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            String username = authentication.getName();
            Long userId = userService.findUserIdByUsername(username);
            AdminOrderDetail adminOrderDetail = orderService.getOrderDetailById(id);

            // check if user try to access another user's order info
            if (!Objects.equals(adminOrderDetail.getUserId(), userId)) {
                return DataResponse.builder()
                        .success(false)
                        .message(NO_AUTHORITY)
                        .data(null)
                        .build();
            }

            UserOrderDetail userOrderDetail = new UserOrderDetail(adminOrderDetail);
            return DataResponse.builder()
                    .success(true)
                    .message("Get user order detail successful")
                    .data(userOrderDetail)
                    .build();
        }

        return DataResponse.builder()
                .success(false)
                .message(NO_AUTHORITY)
                .data(null)
                .build();
    }

    @PostMapping
    public DataResponse placeNewOrder(@Valid @RequestBody OrderRequest orderRequest, BindingResult result) {
        if (result.hasErrors()) {
            return DataResponse.builder()
                    .success(false)
                    .message(VALIDATION_FAIL + result.getAllErrors().get(0).getDefaultMessage())
                    .build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);

        String message = orderService.placeNewOrder(orderRequest, userId);
        return DataResponse.builder()
                .success(true)
                .message(message)
                .build();
    }

    @PatchMapping("/{id}/cancel")
    public DataResponse cancelOrder(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);
        AdminOrderDetail adminOrderDetail = orderService.getOrderDetailById(id);

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            String message = orderService.cancelOrder(id);
            boolean success = message.equals(ORDER_CANCEL_SUCCESSFUL);

            return DataResponse.builder()
                    .success(success)
                    .message(message)
                    .build();
        }

        // user case
        // check if user try to cancel another user's order
        if (!Objects.equals(adminOrderDetail.getUserId(), userId)) {
            return DataResponse.builder()
                    .success(false)
                    .message(NO_AUTHORITY)
                    .data(null)
                    .build();
        }

        String message = orderService.cancelOrder(id);
        boolean success = message.equals(ORDER_CANCEL_SUCCESSFUL);

        return DataResponse.builder()
                .success(success)
                .message(message)
                .build();
    }

    @PatchMapping("/{id}/complete")
    public DataResponse completeOrder(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            String message = orderService.completeOrder(id);
            boolean success = message.equals(ORDER_COMPLETE_SUCCESSFUL);

            return DataResponse.builder()
                    .success(success)
                    .message(message)
                    .build();
        }

        return DataResponse.builder()
                .success(false)
                .message(NO_AUTHORITY)
                .data(null)
                .build();
    }
}
