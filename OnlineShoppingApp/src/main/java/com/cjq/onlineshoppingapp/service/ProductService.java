package com.cjq.onlineshoppingapp.service;

import com.cjq.onlineshoppingapp.dto.admin.request.ProductCreateRequest;
import com.cjq.onlineshoppingapp.dto.admin.request.ProductUpdateRequest;
import com.cjq.onlineshoppingapp.dto.admin.response.AdminAdvanceProductInfo;
import com.cjq.onlineshoppingapp.dto.admin.response.AdminProductInfo;
import com.cjq.onlineshoppingapp.repository.ProductDao;
import com.cjq.onlineshoppingapp.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) { this.productDao = productDao; }

    @Transactional
    public List<Product> getAllProducts() { return productDao.getAllProducts(); }

    @Transactional
    public Product getProductById(Long productId) { return productDao.getProductById(productId); }

    @Transactional
    public List<AdminAdvanceProductInfo> getTotalSoldAmount() {
        List<Product> products = productDao.getAllProducts();
        List<AdminAdvanceProductInfo> res = products.stream()
                .map(product -> {
                    Long totalSoldAmount = productDao.getTotalSoldAmount(product);
                    return new AdminAdvanceProductInfo(product, totalSoldAmount);
                })
                .collect(Collectors.toList());
        return res;
    }

    @Transactional
    public Product createProduct(ProductCreateRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .retailPrice(request.getRetailPrice())
                .wholesalePrice(request.getWholesalePrice())
                .build();
        productDao.saveProduct(product);
        clearCache();
        return product;
    }

    @Transactional
    public AdminProductInfo updateProduct(Long productId, ProductUpdateRequest request) {
        Product product = productDao.getProductById(productId);
        if (product != null) {
            if (request.getName() != null) {
                product.setName(request.getName());
            }
            if (request.getDescription() != null) {
                product.setDescription(request.getDescription());
            }
            if (request.getQuantity() != null) {
                product.setQuantity(request.getQuantity());
            }
            if (request.getRetailPrice() != null) {
                product.setRetailPrice(request.getRetailPrice());
            }
            if (request.getWholesalePrice() != null) {
                product.setWholesalePrice(request.getWholesalePrice());
            }
            productDao.updateProduct(product);
            clearCache();
            return new AdminProductInfo(product);
        }
        return null;
    }

    @CacheEvict(value = "totalSoldAmountCache", key = "#product.productId")
    public void clearCache() {}

}
