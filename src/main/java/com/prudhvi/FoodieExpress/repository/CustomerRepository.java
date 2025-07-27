package com.prudhvi.FoodieExpress.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.prudhvi.FoodieExpress.entity.Customer;
import com.prudhvi.FoodieExpress.entity.FoodCart;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	public Optional<Customer> findByMobileNumber(String mobileNumber);

	public boolean existsByMobileNumber(String mobileNumber);

	public Optional<Customer> findByMobileNumberAndPassword(String mobileNumber, String password);

	@Query("SELECT c.cart FROM Customer c WHERE c.mobileNumber = :mobileNumber")
	public FoodCart getFoodCartByMobileNumber(@Param("mobileNumber") String mobileNumber);
}
