package com.prudhvi.FoodieExpress.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prudhvi.FoodieExpress.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long>{

	public Optional<Item> findByName(String name);
}
