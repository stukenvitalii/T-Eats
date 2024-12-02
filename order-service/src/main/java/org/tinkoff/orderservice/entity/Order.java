package org.tinkoff.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Moscow")
    private Instant orderDate = Instant.now();

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private int totalAmount;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "Pending"; // Статус по умолчанию

    @Column(name = "updated_at")
    private Instant updatedAt = Instant.now();

    @Column(name = "user_id", nullable = false)
    private Long userId; // Храним только ID пользователя

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId; // Храним только ID ресторана

}
