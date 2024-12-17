package org.tinkoff.restaurantservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tinkoff.restaurantservice.entity.Dish;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findAllByRestaurantId(Long restaurantId);
    Dish findByIdAndRestaurantId(Long id, Long restaurantId);
}