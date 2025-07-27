package com.prudhvi.FoodieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prudhvi.FoodieExpress.entity.FoodCart;

public interface FoodCartRepository extends JpaRepository<FoodCart, Long> {
}
