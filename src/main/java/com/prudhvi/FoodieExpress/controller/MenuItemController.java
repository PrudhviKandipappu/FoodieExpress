package com.prudhvi.FoodieExpress.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.prudhvi.FoodieExpress.entity.MenuItemRequest;
import com.prudhvi.FoodieExpress.entity.RestaurantItem;
import com.prudhvi.FoodieExpress.service.MenuItemService;

@Controller
public class MenuItemController {
	
	@Autowired
	private MenuItemService menuItemService;

	@PostMapping("/items/update")
	public ResponseEntity<String> updateMenuItem(@ModelAttribute MenuItemRequest menuItem) {
		try {

	        ResponseEntity<RestaurantItem> response = menuItemService.updateMenuItem(menuItem);
	        String registrationStatus = response.getHeaders().getFirst("Updation-Status");
	        if ("Updation successful!".equals(registrationStatus)) {
	            return ResponseEntity.ok("Update successful!");
	        } else {
	            return ResponseEntity.status(500).body("Updation failed. Please try again later!");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
	    }
	}
	
	@PostMapping("/items/add")
	public ResponseEntity<String> addMenuItem(@ModelAttribute MenuItemRequest menuItem) {
		try {

	        ResponseEntity<RestaurantItem> response = menuItemService.addMenuItem(menuItem);
	        String registrationStatus = response.getHeaders().getFirst("Add-Status");
	        if ("Added successful!".equals(registrationStatus)) {
	            return ResponseEntity.ok("Added successful!");
	        } else {
	            return ResponseEntity.status(500).body("Opeartion failed. Please try again later!");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
	    }
	}
	
	@DeleteMapping("/items/delete/{itemID}")
	public ResponseEntity<String> deleteMenuItem(@PathVariable long itemID) {
		try {
	        ResponseEntity<String> response = menuItemService.deleteMenuItem(itemID);
	        String registrationStatus = response.getHeaders().getFirst("Remove-Status");
	        if ("Remove successful!".equals(registrationStatus)) {
	            return ResponseEntity.ok("Remove successful!");
	        } else {
	            return ResponseEntity.status(500).body("Opeartion failed. Please try again later!");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
	    }
	}
}
