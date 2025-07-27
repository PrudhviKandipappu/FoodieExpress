package com.prudhvi.FoodieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prudhvi.FoodieExpress.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long>{

}
