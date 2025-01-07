package com.cjq.onlineshoppingapp.controller;

import com.cjq.onlineshoppingapp.dto.common.request.UserRegistrationRequest;
import com.cjq.onlineshoppingapp.dto.common.response.DataResponse;
import com.cjq.onlineshoppingapp.dto.common.response.LoginResponse;
import com.cjq.onlineshoppingapp.dto.user.response.UserProductInfo;
import com.cjq.onlineshoppingapp.exception.InvalidCredentialsException;
import com.cjq.onlineshoppingapp.security.AuthUserDetail;
import com.cjq.onlineshoppingapp.security.JwtProvider;
import com.cjq.onlineshoppingapp.service.ProductService;
import com.cjq.onlineshoppingapp.service.UserService;
import com.cjq.onlineshoppingapp.util.common.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;
import java.util.Map;

import static com.cjq.onlineshoppingapp.util.constant.ResponseConstant.*;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    public DataResponse registerUser(@Valid @RequestBody UserRegistrationRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return DataResponse.builder()
                    .success(false)
                    .message(VALIDATION_FAIL + result.getAllErrors().get(0).getDefaultMessage())
                    .build();
        }

        String message = userService.registerUser(request);
        boolean success = message.equals(USER_REGISTER_SUCCESSFUL);

        return DataResponse.builder()
                .success(success)
                .message(message)
                .build();
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        Authentication authentication;

        System.out.println("Start login process.");
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
//            System.out.println("Catched AuthenticationException." + e.getMessage());
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // successfully authenticated
        AuthUserDetail authUserDetail = (AuthUserDetail) authentication.getPrincipal(); // return user object
        String role = authUserDetail.getAuthorities().toString().equals("[ROLE_USER]") ? "user" : "admin";
        System.out.println(role);

        // create token
        String token = jwtProvider.createToken(authUserDetail);

        return LoginResponse.builder()
                .message("Welcome " + authUserDetail.getUsername())
                .token(token)
                .role(role)
                .build();
    }
}
