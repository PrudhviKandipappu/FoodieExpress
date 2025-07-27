package com.prudhvi.FoodieExpress.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.prudhvi.FoodieExpress.entity.Customer;
import com.prudhvi.FoodieExpress.entity.FoodCart;
import com.prudhvi.FoodieExpress.entity.Order;
import com.prudhvi.FoodieExpress.entity.OrderItem;
import com.prudhvi.FoodieExpress.repository.CustomerRepository;
import com.prudhvi.FoodieExpress.repository.FoodCartRepository;
import com.prudhvi.FoodieExpress.repository.OrderRepository;

@Service
public class OrderService {
	
	@Autowired
	private FoodCartRepository cartRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	public Order checkout(Long cartId) {
		FoodCart cart = cartRepository.findById(cartId)
	            .orElseThrow(() -> new RuntimeException("Cart not found"));
		
	    if (cart.getCartItems().isEmpty()) {
	        throw new RuntimeException("Cart is empty, cannot checkout");
	    }
	    
	    User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    Optional<Customer> customer = customerRepository.findByMobileNumber(user.getUsername());
	    
	    Order order = new Order();
	    order.setCustomer(customer.get());
	    order.setOrderDate(LocalDateTime.now());
	    order.setTotalAmount(cart.getTotalItemsCost());
	    
	    List<OrderItem> orderItems = cart.getCartItems().stream()
	    		.map(cartItem -> {
	    			OrderItem orderItem = new OrderItem();
	    			orderItem.setQuantity(cartItem.getQuantity());
	    			orderItem.setOrder(order);
	    			orderItem.setRestaurantItem(cartItem.getRestaurantItem());
	    			
	    			return orderItem;
	    		})
	    		.collect(Collectors.toList());
	    
	    order.setOrderItems(orderItems);
	    
	    Order placedOrder = orderRepository.save(order);
	    
	    cart.getCartItems().clear();
	    cart.setTotalItemsCost(0.0);
	    cartRepository.save(cart);
	    
	    return placedOrder;
		
	}
	
	public List<Order> getOrdersForRestaurant(Long restaurantId) {
        return orderRepository.findOrdersByRestaurantId(restaurantId);
    }

	public List<Order> getOrdersForCustomer(Long customerId) {
		return orderRepository.findOrdersByCustomerId(customerId);
	}
	
	public void setOrderStatus(long orderId) {
		Order order = orderRepository.findById(orderId).get();
		List<OrderItem> orderItems = order.getOrderItems();
		
		boolean flag = true;
		for (OrderItem item : orderItems) {
			if (item.getStatus() == "Pending") {
				flag = false;
				break;
			}
		}
		
		if (flag == true) {
			order.setStatus("Completed");
			orderRepository.save(order);
		}
	}

	public void cancelOrder(long orderId) {
		orderRepository.deleteById(orderId);
	}
}
