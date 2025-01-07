package com.cjq.onlineshoppingapp.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "purchased_price", nullable = false)
    private Double purchasedPrice;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "wholesale_price")
    private Double wholesalePrice;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}

