package com.prudhvi.FoodieExpress.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prudhvi.FoodieExpress.entity.OrderItem;
import com.prudhvi.FoodieExpress.repository.OrderItemRepository;

@Service
public class OrderItemService {
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private OrderService orderService;
	
	public OrderItem deliverOrderItem(OrderItem orderItem) {
		orderItem.setStatus("Completed");
		orderItem = orderItemRepository.save(orderItem);
		
		orderService.setOrderStatus(orderItem.getOrder().getId());
		
		return orderItem;
	}

	public Optional<OrderItem> findById(Long orderItemId) {
		return orderItemRepository.findById(orderItemId);
	}
}
