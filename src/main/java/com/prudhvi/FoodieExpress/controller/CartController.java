package com.prudhvi.FoodieExpress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prudhvi.FoodieExpress.entity.CartItemDTO;
import com.prudhvi.FoodieExpress.entity.FoodCart;
import com.prudhvi.FoodieExpress.entity.Order;
import com.prudhvi.FoodieExpress.repository.CartItemRepository;
import com.prudhvi.FoodieExpress.repository.CustomerRepository;
import com.prudhvi.FoodieExpress.service.FoodCartService;
import com.prudhvi.FoodieExpress.service.OrderService;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private FoodCartService cartService;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private OrderService orderService;

	@PostMapping("/addItem")
	private ResponseEntity<FoodCart> addItemToCart(@ModelAttribute CartItemDTO cartItem) {
		User customer = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		long customerId = customerRepository.findByMobileNumber(customer.getUsername()).get().getId();
		
		FoodCart cart = cartService.addItemToCart(customerId, cartItem.getItemId(), cartItem.getRestaurantId());
		
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeItem")
	private ResponseEntity<FoodCart> removeItemFromCart(@RequestParam long cartItemId) {
		User customer = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		long customerId = customerRepository.findByMobileNumber(customer.getUsername()).get().getId();
		
		FoodCart cart = cartService.removeItemFromCart(customerId, cartItemRepository.findById(cartItemId));
		
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/checkout")
	private ResponseEntity<Order> placeOrder(@RequestParam long cartId) {
		return ResponseEntity.ok(orderService.checkout(cartId));
	}
}
