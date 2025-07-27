package com.prudhvi.FoodieExpress.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prudhvi.FoodieExpress.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>{
	
	public Optional<Restaurant> findByMobileNumber(String mobileNumber);

	public boolean existsByMobileNumber(String mobileNumber);

	public Optional<Restaurant> findByMobileNumberAndPassword(String mobileNumber, String password);
}
