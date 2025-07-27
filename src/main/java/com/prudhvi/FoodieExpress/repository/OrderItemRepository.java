package com.prudhvi.FoodieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prudhvi.FoodieExpress.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
