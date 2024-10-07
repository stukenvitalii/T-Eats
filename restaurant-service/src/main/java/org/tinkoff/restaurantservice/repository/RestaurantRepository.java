package org.tinkoff.restaurantservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tinkoff.restaurantservice.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> , JpaSpecificationExecutor<Restaurant> {
}