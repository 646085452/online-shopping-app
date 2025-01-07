package com.cjq.onlineshoppingapp.controller;

import com.cjq.onlineshoppingapp.dto.common.response.DataResponse;
import com.cjq.onlineshoppingapp.dto.user.response.UserProductInfo;
import com.cjq.onlineshoppingapp.service.UserService;
import com.cjq.onlineshoppingapp.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cjq.onlineshoppingapp.util.constant.ResponseConstant.*;
import static com.cjq.onlineshoppingapp.util.constant.ResponseConstant.WATCHLIST_RETRIEVE_SUCCESSFUL;

@RestController
@RequestMapping("/watchlist")
public class WatchListController {
    private final WatchListService watchListService;
    private final UserService userService;

    @Autowired
    public WatchListController(WatchListService watchListService, UserService userService) {
        this.watchListService = watchListService;
        this.userService = userService;
    }

    @PostMapping("/product/{productId}")
    public DataResponse addToWatchlist(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);
        String message = watchListService.addToWatchlist(userId, productId);
        boolean success = message.equals(WATCHLIST_ADD_SUCCESSFUL);

        return DataResponse.builder()
                .success(success)
                .message(message)
                .build();
    }

    @DeleteMapping("/product/{productId}")
    public DataResponse removeFromWatchlist(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);
        String message = watchListService.removeFromWatchlist(userId, productId);
        boolean success = message.equals(WATCHLIST_REMOVE_SUCCESSFUL);

        return DataResponse.builder()
                .success(success)
                .message(message)
                .build();
    }

    @GetMapping("/products/all")
    public DataResponse getAllWatchlistProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);
        List<UserProductInfo> products = watchListService.getAllWatchlistProducts(userId);

        String message = products.isEmpty() ? EMPTY_WATCHLIST : WATCHLIST_RETRIEVE_SUCCESSFUL;

        return DataResponse.builder()
                .success(!products.isEmpty())
                .message(message)
                .data(products)
                .build();
    }
}
