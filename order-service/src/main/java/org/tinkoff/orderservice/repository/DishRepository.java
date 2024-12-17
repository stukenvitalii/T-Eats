package org.tinkoff.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tinkoff.orderservice.entity.Dish;

public interface DishRepository extends JpaRepository<Dish, Long> {
}