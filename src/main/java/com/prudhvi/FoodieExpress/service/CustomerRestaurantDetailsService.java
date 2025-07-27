package com.prudhvi.FoodieExpress.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.prudhvi.FoodieExpress.entity.Customer;
import com.prudhvi.FoodieExpress.entity.Restaurant;
import com.prudhvi.FoodieExpress.repository.CustomerRepository;
import com.prudhvi.FoodieExpress.repository.RestaurantRepository;

@Service
public class CustomerRestaurantDetailsService implements UserDetailsService{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private RestaurantRepository restaurantRepository;

	@Override
	public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
		Optional<Customer> customer = customerRepository.findByMobileNumber(mobileNumber);
		
		Optional<Restaurant> restaurant = restaurantRepository.findByMobileNumber(mobileNumber);
		if (customer.isPresent()) {
//			System.out.println("\nCustomer Authentication\n");
			var customerObj = customer.get();
			return User.builder()
					.username(customerObj.getMobileNumber())
					.password(customerObj.getPassword())
					.roles(getCustomerRole(customerObj))
					.build();
		} 
		else if(restaurant.isPresent()) {
//			System.out.println("\nCustomer Authentication\n");
			var restaurantObj = restaurant.get();
//			System.out.println("\n" + getRestaurantRole(restaurantObj) + "\n");
			return User.builder()
					.username(restaurantObj.getMobileNumber())
					.password(restaurantObj.getPassword())
					.roles(getRestaurantRole(restaurantObj))
					.build();
				
		}
		else {
			throw new UsernameNotFoundException(mobileNumber);
		}
	}

	private String getCustomerRole(Customer customer) {
		if (customer.getRole() == null) return "TEMP";
		return customer.getRole();
	}
	
	private String getRestaurantRole(Restaurant restaurant) {
		if (restaurant.getRole() == null) return "TEMP";
		return restaurant.getRole();
	}
}
