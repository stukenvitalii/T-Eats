package org.tinkoff.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.tinkoff.orderservice.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> , JpaSpecificationExecutor<Order> {
}