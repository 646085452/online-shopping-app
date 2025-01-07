package com.cjq.onlineshoppingapp.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "permission")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long permissionId;

    @Column(nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

