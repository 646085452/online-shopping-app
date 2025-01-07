package com.cjq.onlineshoppingapp.model;

import lombok.*;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column
    private String description;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "retail_price", nullable = false)
    private Double retailPrice;

    @Column(name = "wholesale_price")
    private Double wholesalePrice;

    @ManyToMany(mappedBy = "watchlist")
    private Set<User> usersWatching;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;
}

