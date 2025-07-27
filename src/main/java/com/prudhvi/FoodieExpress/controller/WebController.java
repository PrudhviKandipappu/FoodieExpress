package com.prudhvi.FoodieExpress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.prudhvi.FoodieExpress.entity.Customer;
import com.prudhvi.FoodieExpress.entity.FoodCart;
import com.prudhvi.FoodieExpress.entity.Restaurant;
import com.prudhvi.FoodieExpress.entity.RestaurantItem;
import com.prudhvi.FoodieExpress.repository.CategoryRepository;
import com.prudhvi.FoodieExpress.repository.CustomerRepository;
import com.prudhvi.FoodieExpress.repository.RestaurantRepository;
import com.prudhvi.FoodieExpress.service.MenuItemService;
import com.prudhvi.FoodieExpress.service.OrderService;

@Controller
public class WebController {
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MenuItemService menuItemService;
	
	@GetMapping("/restaurants/dashboard")
	public String restaurantDashboard(Model model) {
		User res = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Restaurant restaurant = restaurantRepository.findByMobileNumber(res.getUsername()).get();
		
		model.addAttribute("restaurantId", restaurant.getId());
		model.addAttribute("menuItems", restaurant.getRestaurantItems());
		
		for (RestaurantItem ri : restaurant.getRestaurantItems()) {
			System.out.println(ri.getItem().getName());
		}
		
		return "restaurant/dashboard";
	}
	
	@GetMapping("/signup/restaurant")
	public String restaurantSignupPage() {
		return "restaurantSignUp";
	}
	
	@GetMapping("/signup/customer")
	public String customerSignupPage() {
		return "customerSignUp";
	}
	
	@GetMapping("/customers/dashboard")
	public String customerDashboard(Model model) {
		int count = 0;
		
		User res = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		FoodCart cart = customerRepository.getFoodCartByMobileNumber(res.getUsername());
		
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("customerName", getCurrentCustomer());
		
		model.addAttribute("dishes", menuItemService.getAllPossibleItems());
		
		if (cart != null) count = cart.getCartItems().size();

		model.addAttribute("cartItemCount", count);
		return "/customer/dashboard";
	}
	
	@GetMapping("/login")
	public String login() {
		return "custom_login";
	}
	
	private String getCurrentCustomer() {
		User res = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Customer customer = customerRepository.findByMobileNumber(res.getUsername()).get();
		
		return (customer.getFirstName() + ' ' + customer.getLastname());
	}
	
	@GetMapping("/customer/cart")
	private String foodCart(Model model) {
		
		User res = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		FoodCart cart = customerRepository.getFoodCartByMobileNumber(res.getUsername());

		model.addAttribute("cart", cart);
		return "/customer/foodcart";
	}
	
	@GetMapping("/restaurants/orders")
	private String resOrders(Model model) {
		User res = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long restaurantId = restaurantRepository.findByMobileNumber(res.getUsername()).get().getId();
		
		model.addAttribute("orders", orderService.getOrdersForRestaurant(restaurantId));
		model.addAttribute("restaurantId", restaurantId);
		return "/restaurant/orders";
	}
	
	@GetMapping("/customers/orders")
	private String custOrders(Model model) {
		User res = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long customerId = customerRepository.findByMobileNumber(res.getUsername()).get().getId();
		
		model.addAttribute("orders", orderService.getOrdersForCustomer(customerId));
		return "/customer/orders";
	}
}
