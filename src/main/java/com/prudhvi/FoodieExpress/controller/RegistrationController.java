package com.prudhvi.FoodieExpress.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.prudhvi.FoodieExpress.entity.Customer;
import com.prudhvi.FoodieExpress.entity.Restaurant;
import com.prudhvi.FoodieExpress.service.CustomerService;
import com.prudhvi.FoodieExpress.service.RestaurantService;

@Controller
@RequestMapping("/register")
public class RegistrationController {
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/customer")
	public ResponseEntity<String> registerCusomter(@RequestBody Customer customer) {
		try {
	        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

	        ResponseEntity<Customer> response = customerService.addCustomer(customer);
	        
	        String registrationStatus = response.getHeaders().getFirst("Registration-Status");

	        if ("Registration successful!".equals(registrationStatus)) {
	        	
	            return ResponseEntity.ok("Registration successful!");
	        } else if ("Mobile number already exists!".equals(registrationStatus)) {
	        	
	            return ResponseEntity.status(409).body("Mobile number already exists!");
	        } else {
	        	
	            return ResponseEntity.status(500).body("Registration failed. Please try again later!");
	        }
	    } catch (Exception e) {
	    	
	        return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
	    }
	}
	
	@PostMapping("/restaurant")
	public ResponseEntity<String> registerRestaurant(@ModelAttribute Restaurant restaurant, @RequestPart("itemImages[]") List<MultipartFile> itemImages) {
	    try {
	    	System.out.println(itemImages.size());
	    	
	        restaurant.setPassword(passwordEncoder.encode(restaurant.getPassword()));

	        ResponseEntity<Restaurant> response = restaurantService.registerRestaurant(restaurant, itemImages);
	        String registrationStatus = response.getHeaders().getFirst("Registration-Status");

	        if ("Registration successful!".equals(registrationStatus)) {
	            return ResponseEntity.ok("Registration successful!");
	        } else if ("Mobile number already exists!".equals(registrationStatus)) {
	            return ResponseEntity.status(409).body("Mobile number already exists!");
	        } else {
	            return ResponseEntity.status(500).body("Registration failed. Please try again later!");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
	    }
	}
}
