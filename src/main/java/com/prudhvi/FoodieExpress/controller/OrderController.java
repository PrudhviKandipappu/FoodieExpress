package com.prudhvi.FoodieExpress.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prudhvi.FoodieExpress.entity.OrderItem;
import com.prudhvi.FoodieExpress.service.OrderItemService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderItemService orderItemService;
	
	@PostMapping("/complete")
	public ResponseEntity<String> completeOrder(@RequestBody List<Long> orderItemIds) {
	    for (Long orderItemId : orderItemIds) {
	        Optional<OrderItem> orderItem = orderItemService.findById(orderItemId);
	        orderItemService.deliverOrderItem(orderItem.get());
	    }
	    return ResponseEntity.ok("Delivered");
	}
}
