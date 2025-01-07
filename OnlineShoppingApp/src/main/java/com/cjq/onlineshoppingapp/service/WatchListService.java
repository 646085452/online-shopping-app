package com.cjq.onlineshoppingapp.service;

import com.cjq.onlineshoppingapp.dto.user.response.UserProductInfo;
import com.cjq.onlineshoppingapp.model.Product;
import com.cjq.onlineshoppingapp.model.User;
import com.cjq.onlineshoppingapp.repository.ProductDao;
import com.cjq.onlineshoppingapp.repository.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.cjq.onlineshoppingapp.util.constant.ResponseConstant.*;
import static com.cjq.onlineshoppingapp.util.constant.ResponseConstant.WATCHLIST_REMOVE_SUCCESSFUL;

@Service
public class WatchListService {
    private final UserDao userDao;
    private final ProductDao productDao;

    public WatchListService(UserDao userDao, ProductDao productDao) {
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @Transactional
    public String addToWatchlist(Long userId, Long productId) {
        User user = userDao.findById(userId);
        if (user == null) {
            return USER_NOT_FOUND;
        }

        Product product = productDao.getProductById(productId);
        if (product == null) {
            return PRODUCT_NOT_FOUND;
        }

        if (user.getWatchlist().contains(product)) {
            return PRODUCT_ON_WATCHLIST;
        }

        user.getWatchlist().add(product);
        userDao.updateUser(user);

        return WATCHLIST_ADD_SUCCESSFUL;
    }

    @Transactional
    public String removeFromWatchlist(Long userId, Long productId) {
        User user = userDao.findById(userId);
        if (user == null) {
            return USER_NOT_FOUND;
        }

        Product product = productDao.getProductById(productId);
        if (product == null) {
            return PRODUCT_NOT_FOUND;
        }

        if (!user.getWatchlist().contains(product)) {
            return PRODUCT_NOT_ON_WATCHLIST;
        }

        user.getWatchlist().remove(product);
        userDao.updateUser(user);

        return WATCHLIST_REMOVE_SUCCESSFUL;
    }

    @Transactional(readOnly = true)
    public List<UserProductInfo> getAllWatchlistProducts(Long userId) {
        User user = userDao.findById(userId);
        if (user == null) {
            return Collections.emptyList();
        }

        // filter 0 quantity
        return user.getWatchlist().stream()
                .filter(product -> product.getQuantity() > 0)
                .map(UserProductInfo::new)
                .collect(Collectors.toList());
    }
}
