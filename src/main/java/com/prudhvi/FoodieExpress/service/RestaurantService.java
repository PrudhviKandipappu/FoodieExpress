package com.prudhvi.FoodieExpress.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prudhvi.FoodieExpress.entity.*;
import com.prudhvi.FoodieExpress.repository.*;

import io.jsonwebtoken.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RestaurantItemRepository restaurantItemRepository;

    @Transactional
    public ResponseEntity<Restaurant> registerRestaurant(Restaurant restaurant, List<MultipartFile> itemImages) {
        HttpHeaders headers = new HttpHeaders();
        try {
            // Check if a restaurant with the same mobile number already exists
            if (restaurantRepository.existsByMobileNumber(restaurant.getMobileNumber())) {
                headers.add("Registration-Status", "Mobile number already exists!");
                return new ResponseEntity<>(null, headers, HttpStatus.CONFLICT);
            }

            // Check and encode the password
            if (restaurant.getPassword() == null || restaurant.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }

            // Handle address
            Address address = handleAddress(restaurant.getAddress());
            restaurant.setAddress(address);

            // Handle and save restaurant items
            List<RestaurantItem> restaurantItems = handleRestaurantItems(restaurant, itemImages);
            
            // Save the restaurant last
            restaurant = restaurantRepository.save(restaurant);

            // Set the restaurant to its items and save items
            for (RestaurantItem restaurantItem : restaurantItems) {
                restaurantItem.setRestaurant(restaurant);
                restaurantItemRepository.save(restaurantItem);
            }

            restaurant.setRestaurantItems(restaurantItems);

            headers.add("Registration-Status", "Registration successful!");
            return new ResponseEntity<>(restaurant, headers, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            headers.add("Registration-Status", "Password cannot be null or empty.");
            System.err.println("Registration failed: " + e.getMessage());
            return new ResponseEntity<>(null, headers, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            headers.add("Registration-Status", "Registration failed! Please try again.");
            System.err.println("Registration failed: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(null, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    private Address handleAddress(Address address) {
        try {
            if (address != null) {
                Address existingAddress = addressRepository.findByBuildingNameAndStreetNoAndAreaAndCityAndStateAndCountryAndPinCode(
                        address.getBuildingName(), address.getStreetNo(), address.getArea(),
                        address.getCity(), address.getState(), address.getCountry(), address.getPinCode());

                if (existingAddress != null) {
                    return existingAddress;
                } else {
                    return addressRepository.save(address);
                }
            }
        } catch (Exception e) {
            System.err.println("Error handling address: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to mark the transaction as rollback-only
        }
        return null;
    }

    @Transactional
    private List<RestaurantItem> handleRestaurantItems(Restaurant restaurant, List<MultipartFile> itemImages) throws Exception {
        List<RestaurantItem> restaurantItems = new ArrayList<>();
        try {
        	for (int i = 0; i < restaurant.getRestaurantItems().size(); i++) {
                RestaurantItem restaurantItem = restaurant.getRestaurantItems().get(i);
                
                Item item = restaurantItem.getItem();
                Category category = item.getCategory();

                // Ensure the category is saved before assigning it to the item
                if (category != null) {
                    category = handleCategory(category);
                    item.setCategory(category);
                }

                // Ensure the item is saved before assigning it to the restaurant item
                item = handleItem(item);


                MultipartFile imageFile = itemImages.get(i);
                String imagePath = saveImage(imageFile);
                restaurantItem.setImagePath(imagePath); 
                
                // Save the restaurant item
                restaurantItem.setItem(item);
                restaurantItem.setRestaurant(restaurant);
                restaurantItems.add(restaurantItem);
            }
        } catch (Exception e) {
            System.err.println("Error handling restaurant items: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to mark the transaction as rollback-only
        }
        return restaurantItems;
    }

    @Transactional
    private synchronized Category handleCategory(Category category) {
        try {
            if (category != null) {
                System.out.println("Handling category: " + category.getName());
                Optional<Category> existingCategory = categoryRepository.findByName(category.getName());
                if (existingCategory.isPresent()) {
                    System.out.println("Existing category found: " + existingCategory.get().getName());
                    return existingCategory.get();
                } else {
                    System.out.println("Saving new category: " + category.getName());
                    return categoryRepository.save(category);
                }
            }
        } catch (Exception e) {
            System.err.println("Error handling category: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to mark the transaction as rollback-only
        }
        return null;
    }

    @Transactional
    private Item handleItem(Item item) {
        try {
            System.out.println("Handling item: " + item.getName());
            Optional<Item> existingItem = itemRepository.findByName(item.getName());
            if (existingItem.isPresent()) {
                System.out.println("Existing item found: " + existingItem.get().getName());
                return existingItem.get();
            } else {
                System.out.println("Saving new item: " + item.getName());
                return itemRepository.save(item);
            }
        } catch (Exception e) {
            System.err.println("Error handling item: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to mark the transaction as rollback-only
        }
    }
    
    public String saveImage(MultipartFile file) throws java.io.IOException {
        try {
            // Define the directory to save the images
            String uploadDir = "src/main/resources/static/images/itemImages/";
            
            // Create a unique file name using the current timestamp
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            
            // Resolve the path for saving the file
            java.nio.file.Path filePath = Paths.get(uploadDir + fileName);
            
            // Ensure the directory exists
            Files.createDirectories(filePath.getParent());
            System.out.println(filePath.getParent());
            // Save the file content
            Files.write(filePath, file.getBytes());
            
            // Return the relative file path
            return "/images/itemImages/" + fileName;
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
            throw new RuntimeException("Failed to save image", e);
        }
    }

	public List<Restaurant> viewAllRestaurants() {
		return restaurantRepository.findAll();
	}
}
