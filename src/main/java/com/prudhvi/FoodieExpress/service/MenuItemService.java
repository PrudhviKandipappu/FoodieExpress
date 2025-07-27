package com.prudhvi.FoodieExpress.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prudhvi.FoodieExpress.entity.Category;
import com.prudhvi.FoodieExpress.entity.Item;
import com.prudhvi.FoodieExpress.entity.ItemDAO;
import com.prudhvi.FoodieExpress.entity.MenuItemRequest;
import com.prudhvi.FoodieExpress.entity.Restaurant;
import com.prudhvi.FoodieExpress.entity.RestaurantItem;
import com.prudhvi.FoodieExpress.repository.CategoryRepository;
import com.prudhvi.FoodieExpress.repository.ItemRepository;
import com.prudhvi.FoodieExpress.repository.RestaurantItemRepository;
import com.prudhvi.FoodieExpress.repository.RestaurantRepository;

@Service
public class MenuItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private RestaurantItemRepository restaurantItemRepository;
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Transactional
	public ResponseEntity<RestaurantItem> updateMenuItem(MenuItemRequest menuItem) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        
		Optional<RestaurantItem> restaurantItem = restaurantItemRepository.findByItemIdAndRestaurantId(menuItem.getId(), menuItem.getRestaurantId());
		
		if (restaurantItem.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
		
		RestaurantItem restItem = restaurantItem.get();
		
		Item item = getOrCreateItem(menuItem.getName());
		item.setCategory(getOrCreateCategory(menuItem.getCategory()));
		
		restItem.setItem(item);
		restItem.setCost(menuItem.getPrice());
		
		 if (menuItem.getImage() != null && !menuItem.getImage().isEmpty()) {
             String oldImagePath = restItem.getImagePath();
             String newImagePath = restaurantService.saveImage(menuItem.getImage());

             // Update image path in the entity
             restItem.setImagePath(newImagePath);

             // Delete the old image if it exists
             if (oldImagePath != null) {
                 deleteImage(oldImagePath);
             }
         }
		 
		 restItem = restaurantItemRepository.save(restItem);
		 
		 headers.add("Updation-Status", "Updation successful!");
         return new ResponseEntity<>(restItem, headers, HttpStatus.CREATED);
	}
	
	@Transactional
	public ResponseEntity<RestaurantItem> addMenuItem(MenuItemRequest menuItem) throws IOException {
		HttpHeaders headers = new HttpHeaders();
		
		RestaurantItem restaurantItem = new RestaurantItem();
		
		Optional<Restaurant> restaurantOpt = restaurantRepository.findById(menuItem.getRestaurantId());
		Restaurant restaurant = restaurantOpt.get();
		
		restaurantItem.setRestaurant(restaurant);
		
		restaurantItem.setCost(menuItem.getPrice());
		
		Item item = getOrCreateItem(menuItem.getName());
		item.setCategory(getOrCreateCategory(menuItem.getCategory()));
		
		restaurantItem.setItem(item);
		restaurantItem.setImagePath(restaurantService.saveImage(menuItem.getImage()));
		
		restaurantItem = restaurantItemRepository.save(restaurantItem);
		
		List<RestaurantItem> restaurantItems = restaurant.getRestaurantItems();
		restaurantItems.add(restaurantItem);
		
		restaurant.setRestaurantItems(restaurantItems);
		
		restaurantRepository.save(restaurant);
	
		headers.add("Add-Status", "Added successful!");
        return new ResponseEntity<>(restaurantItem, headers, HttpStatus.CREATED);
	}
	
	@Transactional
	public ResponseEntity<String> deleteMenuItem(long itemID) {
		HttpHeaders headers = new HttpHeaders();
		
		User res = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Restaurant restaurant = restaurantRepository.findByMobileNumber(res.getUsername()).get();
		
		RestaurantItem restItem = restaurantItemRepository.findByItemIdAndRestaurantId(itemID, restaurant.getId()).get();
		deleteImage(restItem.getImagePath());
		
		List<RestaurantItem> restaurantItems = restaurant.getRestaurantItems();
		restaurantItems.removeIf(restaurantItem -> restaurantItem.getItem().getId() == itemID);
		
		restaurant.setRestaurantItems(restaurantItems);
		restaurantRepository.save(restaurant);
		
		headers.add("Remove-Status", "Remove successful!");
        return new ResponseEntity<>("", headers, HttpStatus.CREATED);
	}
	
	private void deleteImage(String imagePath) {
		try {
            String fullPath = "src/main/resources/static" + imagePath; // Adjust as per your project structure
            java.nio.file.Path path = Paths.get(fullPath);
            Files.deleteIfExists(path);
        } catch (Exception e) {
            System.err.println("Error deleting image: " + e.getMessage());
        }
	}

	public Category getOrCreateCategory(String categoryName) {
	    Optional<Category> existingCategory = categoryRepository.findByName(categoryName);
	    
	    if (existingCategory.isPresent()) {
	        // Category exists, return the existing category
	        return existingCategory.get();
	    } else {
	        // Category does not exist, create a new category
	        Category newCategory = new Category();
	        newCategory.setName(categoryName);
	        
	        // Save the new category
	        newCategory = categoryRepository.save(newCategory);
	        
	        return newCategory;
	    }
	}

	public Item getOrCreateItem(String itemName) {
	    // Check if item exists
	    Optional<Item> existingItem = itemRepository.findByName(itemName);
	    
	    if (existingItem.isPresent()) {
	        // Item exists, return the existing item
	        return existingItem.get();
	    } else {
	        // Item does not exist, create a new item
	        Item newItem = new Item();
	        newItem.setName(itemName);
	        // Set other fields as necessary
	        
	        // Save the new item
	        newItem = itemRepository.save(newItem);
	        
	        return newItem;
	    }
	}
	
	public List<ItemDAO> getAllPossibleItems() {
		return restaurantItemRepository.getAllItemDetails();
	}
}
