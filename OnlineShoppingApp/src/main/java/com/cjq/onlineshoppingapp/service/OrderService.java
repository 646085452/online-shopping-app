package com.cjq.onlineshoppingapp.service;

import com.cjq.onlineshoppingapp.dto.admin.response.AdminOrderDetail;
import com.cjq.onlineshoppingapp.dto.admin.response.AdminOrderSummary;
import com.cjq.onlineshoppingapp.dto.admin.response.AdminProductInfo;
import com.cjq.onlineshoppingapp.dto.user.request.OrderItemRequest;
import com.cjq.onlineshoppingapp.dto.user.request.OrderRequest;
import com.cjq.onlineshoppingapp.dto.user.response.UserOrderSummary;
import com.cjq.onlineshoppingapp.dto.user.response.UserProductInfo;
import com.cjq.onlineshoppingapp.exception.InsufficientStockException;
import com.cjq.onlineshoppingapp.model.Order;
import com.cjq.onlineshoppingapp.model.OrderItem;
import com.cjq.onlineshoppingapp.model.Product;
import com.cjq.onlineshoppingapp.model.User;
import com.cjq.onlineshoppingapp.repository.OrderDao;
import com.cjq.onlineshoppingapp.repository.ProductDao;
import com.cjq.onlineshoppingapp.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.cjq.onlineshoppingapp.util.constant.ResponseConstant.*;

@Service
public class OrderService {

    private final ProductDao productDao;
    private final UserDao userDao;
    private final OrderDao orderDao;
    private final ProductService productService;

    @Autowired
    public OrderService(ProductDao productDao, UserDao userDao, OrderDao orderDao, ProductService productService) {
        this.productDao = productDao;
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public List<AdminOrderSummary> getAllOrders() {
        return orderDao.getAllOrders().stream()
                .map(AdminOrderSummary::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserOrderSummary> getAllUserOrdersByUserId(Long userId) {
        return orderDao.getAllOrdersByUserId(userId).stream()
                .map(UserOrderSummary::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminOrderDetail getOrderDetailById(long orderId) {
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
        return new AdminOrderDetail(order);
    }

    @Transactional
    public String placeNewOrder(OrderRequest orderRequest, Long userId) {
        User user = userDao.findById(userId);
        if (user == null) {
            return USER_NOT_FOUND;
        }

        Set<OrderItem> orderItems = new HashSet<>();

        // check stock, add order item, update stock
        for(OrderItemRequest itemRequest : orderRequest.getOrder()) {
            if (!productDao.isStockAvailable(itemRequest.getProductId(), itemRequest.getQuantity())) {
                throw new InsufficientStockException("Insufficient stock for product ID: " + itemRequest.getProductId());
            }

            Product product = (Product) productDao.findById(itemRequest.getProductId());
            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .purchasedPrice(product.getRetailPrice())
                    .wholesalePrice(product.getWholesalePrice())
                    .build();
            orderItems.add(orderItem);

            productDao.updateProductStock(itemRequest.getProductId(), itemRequest.getQuantity());
        }

        Order order = Order.builder()
                .user(user)
                .datePlaced(LocalDateTime.now())
                .orderStatus("Processing")
                .orderItems(orderItems)
                .build();

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }

        orderDao.saveOrder(order);
        return ORDER_PLACE_SUCCESSFUL;
    }

    @Transactional
    public String cancelOrder(Long orderId) {
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            return ORDER_NOT_FOUND + orderId;
        }

        if ("Completed".equals(order.getOrderStatus())) {
            return CANNOT_CANCEL_COMPLETED_ORDER;
        }

        if ("Processing".equals(order.getOrderStatus())) {
            for (OrderItem orderItem : order.getOrderItems()) {
                Product product = orderItem.getProduct();
                product.setQuantity(product.getQuantity() + orderItem.getQuantity());
                productDao.updateProduct(product);
            }

            order.setOrderStatus("Canceled");
            orderDao.updateOrder(order);

            return ORDER_CANCEL_SUCCESSFUL;
        }

        return CANNOT_CANCEL_OTHER_ORDER;
    }

    @Transactional
    public String completeOrder(Long orderId) {
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            return ORDER_NOT_FOUND + orderId;
        }

        if ("Canceled".equals(order.getOrderStatus())) {
            return CANNOT_COMPLETE_CANCALED_ORDER;
        }

        if ("Processing".equals(order.getOrderStatus())) {
            order.setOrderStatus("Completed");
            orderDao.updateOrder(order);
            productService.clearCache();

            return ORDER_COMPLETE_SUCCESSFUL;
        }

        return CANNOT_COMPLETE_OTHER_ORDER;
    }

    // TODO: implement this in repository
    @Transactional(readOnly = true)
    public List<UserProductInfo> getTopFrequentProducts(Long userId, int limit) {
        List<Order> orders = orderDao.getOrdersByUserIdAndStatus(userId, "Processing", "Completed");

        // count frequencies
        return orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProduct,
                        Collectors.summingInt(OrderItem::getQuantity)
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> {
                    int frequencyComparison = e2.getValue().compareTo(e1.getValue());
                    if (frequencyComparison == 0) {
                        // tie breaker by productId
                        return e1.getKey().getProductId().compareTo(e2.getKey().getProductId());
                    }
                    return frequencyComparison;
                })
                .limit(limit)
                .map(entry -> new UserProductInfo(entry.getKey()))
                .collect(Collectors.toList());
    }

    // TODO: implement this in repository
    @Transactional(readOnly = true)
    public List<UserProductInfo> getTopRecentProducts(Long userId, int limit) {
        List<Order> orders = orderDao.getOrdersByUserIdAndStatus(userId, "Processing", "Completed");

        // sort by date and item ID
        return orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .sorted((o1, o2) -> {
                    int dateComparison = o2.getOrder().getDatePlaced().compareTo(o1.getOrder().getDatePlaced());
                    if (dateComparison == 0) {
                        // tie breaker
                        return o1.getItemId().compareTo(o2.getItemId());
                    }
                    return dateComparison;
                })
                .map(orderItem -> new UserProductInfo(orderItem.getProduct()))
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    // TODO: implement this in repository
    @Transactional(readOnly = true)
    public List<AdminProductInfo> getTopProfitableProducts(int limit) {
        List<Order> completedOrders = orderDao.getOrdersByStatus("Completed");

        // profit per product
        return completedOrders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProduct,
                        Collectors.summingDouble(orderItem ->
                                (orderItem.getProduct().getRetailPrice() - orderItem.getProduct().getWholesalePrice()) * orderItem.getQuantity()
                        )
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> {
                    int profitComparison = e2.getValue().compareTo(e1.getValue());  // sort by profit descending
                    if (profitComparison == 0) {
                        // tie breaker by productID
                        return e1.getKey().getProductId().compareTo(e2.getKey().getProductId());
                    }
                    return profitComparison;
                })
                .limit(limit)
                .map(entry -> new AdminProductInfo(entry.getKey()))
                .collect(Collectors.toList());
    }

    // TODO: implement this in repository
    @Transactional(readOnly = true)
    public List<AdminProductInfo> getTopPopularProducts(int limit) {
        List<Order> completedOrders = orderDao.getOrdersByStatus("Completed");

        // total quantity sold per product
        return completedOrders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::getProduct,
                        Collectors.summingInt(OrderItem::getQuantity)
                ))
                .entrySet().stream()
                .sorted((e1, e2) -> {
                    int quantityComparison = e2.getValue().compareTo(e1.getValue());  // sort by quantity descending
                    if (quantityComparison == 0) {
                        // tie breaker by productID
                        return e1.getKey().getProductId().compareTo(e2.getKey().getProductId());
                    }
                    return quantityComparison;
                })
                .limit(limit)
                .map(entry -> new AdminProductInfo(entry.getKey()))
                .collect(Collectors.toList());
    }
}
