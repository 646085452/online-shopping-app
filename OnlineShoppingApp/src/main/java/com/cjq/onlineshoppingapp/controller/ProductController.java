package com.cjq.onlineshoppingapp.controller;

import com.cjq.onlineshoppingapp.dto.admin.request.ProductCreateRequest;
import com.cjq.onlineshoppingapp.dto.admin.request.ProductUpdateRequest;
import com.cjq.onlineshoppingapp.dto.admin.response.AdminAdvanceProductInfo;
import com.cjq.onlineshoppingapp.dto.admin.response.AdminProductInfo;
import com.cjq.onlineshoppingapp.dto.common.response.DataResponse;
import com.cjq.onlineshoppingapp.dto.user.response.UserProductInfo;
import com.cjq.onlineshoppingapp.model.Product;
import com.cjq.onlineshoppingapp.security.AuthUserDetail;
import com.cjq.onlineshoppingapp.service.OrderService;
import com.cjq.onlineshoppingapp.service.ProductService;
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
import java.util.stream.Collectors;

import static com.cjq.onlineshoppingapp.util.constant.ResponseConstant.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public ProductController(ProductService productService, OrderService orderService, UserService userService) {
        this.productService = productService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public DataResponse getAllProducts() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        List<Product> products = productService.getAllProducts();

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            List<AdminProductInfo> adminProductInfoList = products.stream()
                    .map(AdminProductInfo::new)
                    .collect(Collectors.toList());
            return DataResponse.builder()
                    .success(true)
                    .message("Returning all product info for admin")
                    .data(adminProductInfoList)
                    .build();
        }

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            List<UserProductInfo> userProductInfoList = products.stream()
                    .filter(product -> product.getQuantity() > 0)
                    .map(UserProductInfo::new)
                    .collect(Collectors.toList());
            return DataResponse.builder()
                    .success(true)
                    .message("Returning in-stock product info for user")
                    .data(userProductInfoList)
                    .build();
        }

        return DataResponse.builder()
                .success(false)
                .message(NO_AUTHORITY)
                .data(null)
                .build();
    }

    @GetMapping("/{id}")
    public DataResponse getProductById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Product product = productService.getProductById(id);
        if (product == null) {
            return DataResponse.builder()
                    .success(false)
                    .message(PRODUCT_NOT_FOUND + id)
                    .build();
        }

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            AdminProductInfo adminProductInfo = new AdminProductInfo(product);
            return DataResponse.builder()
                    .success(true)
                    .message(PRODUCT_RETRIEVED_SUCCESSFUL)
                    .data(adminProductInfo)
                    .build();
        }

        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER"))) {
            if (product.getQuantity() == 0) {
                return DataResponse.builder()
                        .success(false)
                        .message("This product is currently out of stock. Sorry for the inconvenience.")
                        .data(null)
                        .build();
            }

            UserProductInfo userProductInfo = new UserProductInfo(product);
            return DataResponse.builder()
                    .success(true)
                    .message(PRODUCT_RETRIEVED_SUCCESSFUL)
                    .data(userProductInfo)
                    .build();
        }

        return DataResponse.builder()
                .success(false)
                .message(NO_AUTHORITY)
                .data(null)
                .build();
    }

    @PostMapping
    public DataResponse createProduct(@Valid @RequestBody ProductCreateRequest request, BindingResult result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            if (result.hasErrors()) {
                return DataResponse.builder()
                        .success(false)
                        .message(VALIDATION_FAIL + result.getAllErrors().get(0).getDefaultMessage())
                        .build();
            }

            Product product = productService.createProduct(request);
            return DataResponse.builder()
                    .success(true)
                    .message(PRODUCT_CREATE_SUCCESSFUL)
                    .data(product)
                    .build();
        }

        return DataResponse.builder()
                .success(false)
                .message(NO_AUTHORITY)
                .data(null)
                .build();
    }

    @PatchMapping("/{id}")
    public DataResponse updateProduct(@PathVariable Long id, @RequestBody ProductUpdateRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            AdminProductInfo productInfo = productService.updateProduct(id, request);
            if (productInfo == null) {
                return DataResponse.builder()
                        .success(false)
                        .message(PRODUCT_NOT_FOUND + id)
                        .build();
            }
            return DataResponse.builder()
                    .success(true)
                    .message(PRODUCT_UPDATE_SUCCESSFUL)
                    .data(productInfo)
                    .build();
        }

        return DataResponse.builder()
                .success(false)
                .message(NO_AUTHORITY)
                .data(null)
                .build();
    }

    @GetMapping("/frequent/{limit}")
    public DataResponse getTopFrequentProducts(@PathVariable int limit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return DataResponse.builder()
                    .success(false)
                    .message(USER_ONLY)
                    .data(null)
                    .build();
        }

        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);
        List<UserProductInfo> frequentProducts = orderService.getTopFrequentProducts(userId, limit);

        return DataResponse.builder()
                .success(true)
                .message("Get top frequent bought products successful")
                .data(frequentProducts)
                .build();
    }

    @GetMapping("/recent/{limit}")
    public DataResponse getTopRecentProducts(@PathVariable int limit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return DataResponse.builder()
                    .success(false)
                    .message(USER_ONLY)
                    .data(null)
                    .build();
        }

        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);
        List<UserProductInfo> recentProducts = orderService.getTopRecentProducts(userId, limit);

        return DataResponse.builder()
                .success(true)
                .message("Get top recent bought products successful")
                .data(recentProducts)
                .build();
    }

    @GetMapping("/profit/{limit}")
    public DataResponse getTopProfitableProducts(@PathVariable int limit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            List<AdminProductInfo> resData = orderService.getTopProfitableProducts(limit);
            return DataResponse.builder()
                    .success(true)
                    .message("Get profitable products successful")
                    .data(resData)
                    .build();
        }

        return DataResponse.builder()
                .success(false)
                .message(NO_AUTHORITY)
                .data(null)
                .build();
    }

    @GetMapping("/popular/{limit}")
    public DataResponse getTopPopularProducts(@PathVariable int limit) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            List<AdminProductInfo> resData = orderService.getTopPopularProducts(limit);
            return DataResponse.builder()
                    .success(true)
                    .message("Get popular products successful")
                    .data(resData)
                    .build();
        }

        return DataResponse.builder()
                .success(false)
                .message(NO_AUTHORITY)
                .data(null)
                .build();
    }

    @GetMapping("/total-amount-sold")
    public DataResponse getTotalAmountSold() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            List<AdminAdvanceProductInfo> resData = productService.getTotalSoldAmount();
            return DataResponse.builder()
                    .success(true)
                    .message("Get total sold amount of each product successful")
                    .data(resData)
                    .build()
                    ;
        }

        return DataResponse.builder()
                .success(false)
                .message(NO_AUTHORITY)
                .data(null)
                .build();
    }
}
