package com.prudhvi.FoodieExpress.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.prudhvi.FoodieExpress.entity.Customer;
import com.prudhvi.FoodieExpress.service.CustomerService;
import com.prudhvi.FoodieExpress.service.OrderService;

@RestController
@RequestMapping("/customers") // Plural for consistency
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private OrderService orderService;

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable("id") Long customerId, @RequestBody Customer customer) {
        return customerService.updateCustomer(customer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCustomer(@PathVariable("id") Long customerId) {
        customerService.removeCustomer(customerId);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> viewCustomer(@PathVariable("id") Long customerId) {
        return customerService.viewCustomer(customerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Customer>> viewAllCustomers() {
        List<Customer> customers = customerService.viewAllCustomer();
        return ResponseEntity.ok(customers);
    }
    
    @PostMapping("/orders/cancel")
    public ResponseEntity<String> cancelOrder(@RequestParam long orderId) {
    	orderService.cancelOrder(orderId);
    	
    	return ResponseEntity.ok("Order has been canceled successfully.");
    }
}
