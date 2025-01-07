package com.cjq.onlineshoppingapp.service;

import com.cjq.onlineshoppingapp.dto.common.request.UserRegistrationRequest;
import com.cjq.onlineshoppingapp.dto.user.response.UserProductInfo;
import com.cjq.onlineshoppingapp.exception.IllegalRoleException;
import com.cjq.onlineshoppingapp.exception.InvalidCredentialsException;
import com.cjq.onlineshoppingapp.model.Product;
import com.cjq.onlineshoppingapp.model.User;
import com.cjq.onlineshoppingapp.repository.ProductDao;
import com.cjq.onlineshoppingapp.repository.UserDao;
import com.cjq.onlineshoppingapp.util.common.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cjq.onlineshoppingapp.security.AuthUserDetail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cjq.onlineshoppingapp.util.constant.ResponseConstant.*;

@Service
public class UserService implements UserDetailsService {
    private final UserDao userDao;
    private final ProductDao productDao;

    @Autowired
    public UserService(UserDao userDao, ProductDao productDao) {
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @Transactional
    public Long findUserIdByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user == null) {
            return -1L;
        }
        return user.getUserId();
    }

    @Transactional
    public String registerUser(UserRegistrationRequest request) {
        if (userDao.isUsernameOrEmailUnique(request.getUsername(), request.getEmail())) {
            // change to throw custom exception?
            return USERNAME_OR_EMAIL_IN_USE;
        }

        String encodedPwd = PasswordUtil.encodePassword(request.getPassword());
//        String help = PasswordUtil.encodePassword("adminpassword");
//        System.out.println("helper encrypt password for admin: " + help);

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPwd)
                .role(1)
                .build();

        userDao.addUser(user);
        return USER_REGISTER_SUCCESSFUL;
    }

    @Transactional
    public String login(String username, String password) {
        User user = userDao.findByUsername(username);
        if (user == null || !PasswordUtil.matchesPassword(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
        return USER_LOGIN_SUCCESSFUL;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Username does not exist");
        }

        return AuthUserDetail.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(getAuthoritiesFromUser(user))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();
    }

    private List<GrantedAuthority> getAuthoritiesFromUser(User user) {
        String role;
        if (user.getRole() == 1) {
            role = "ROLE_USER";
        } else if (user.getRole() == 2) {
            role = "ROLE_ADMIN";
        } else {
            throw new IllegalRoleException("Invalid role: " + user.getRole());
        }

        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
