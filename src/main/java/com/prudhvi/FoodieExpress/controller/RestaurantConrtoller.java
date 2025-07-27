package com.prudhvi.FoodieExpress.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prudhvi.FoodieExpress.entity.Restaurant;
import com.prudhvi.FoodieExpress.service.RestaurantService;

@RestController
@RequestMapping("/restaurants")
public class RestaurantConrtoller {
	
	@Autowired
	private RestaurantService resSer;
	
//	@PutMapping("/{id}")
//	public ResponseEntity<Restaurant> updateRestaurantHandler(@RequestBody Restaurant res) throws RestaurantException{
//		resSer.updateRestaurant(res);
//	
//		return new ResponseEntity<Restaurant>(HttpStatus.OK);		
//	}
//	
//	@GetMapping("/{id}")
//	public ResponseEntity<Restaurant> viewRestaurantHandler(@PathVariable Long id){
//		
//		resSer.viewRestaurant(id);
//		
//		return new ResponseEntity<Restaurant>(HttpStatus.OK);
//	}
//	
	@GetMapping
	public List<Restaurant> viewAllRetaurants() {
		return resSer.viewAllRestaurants();
	}
//	
//	@GetMapping("/restaurant")
//	public ResponseEntity<List<Restaurant>> viewNearByRestaurantHandler(@PathVariable Address address) throws RestaurantException{
//		
//		List<Restaurant>  rest =resSer.viewNearByRestaurant(address);
//		
//		return new ResponseEntity<>(rest,HttpStatus.OK);
//		
//	}
//	
//	@GetMapping("/restaurant/{itemName}")
//	public ResponseEntity<List<Restaurant>> viweRestaurantByItemNameHandler(@PathVariable String itemName) throws RestaurantException{
//		
//		List<Restaurant> rest = resSer.viweRestaurantByItemName(itemName);
//		
//		return new ResponseEntity<>(rest,HttpStatus.OK);
//	}
}
