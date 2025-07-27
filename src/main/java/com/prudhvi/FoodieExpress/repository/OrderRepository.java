package com.prudhvi.FoodieExpress.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prudhvi.FoodieExpress.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	@Query("SELECT o FROM Order o JOIN o.orderItems oi WHERE oi.restaurantItem.restaurant.id = :restaurantId")
	List<Order> findOrdersByRestaurantId(@Param("restaurantId") long restaurantId);

	@Query("SELECT o FROM Order o WHERE o.customer.id = :customerId")
	List<Order> findOrdersByCustomerId(@Param("customerId") long customerId);
}
