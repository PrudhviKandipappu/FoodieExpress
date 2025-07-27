package com.prudhvi.FoodieExpress.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prudhvi.FoodieExpress.entity.Item;
import com.prudhvi.FoodieExpress.repository.ItemRepository;

@Service
public class ItemService {

	@Autowired
	private ItemRepository itemRepository;
	
	public Item addItem(Item item) {
		return itemRepository.save(item);
	}

	public Optional<Item> updateItem(Item updatedItem) {
		
		return itemRepository.findById(updatedItem.getId())
				.map(item -> {
					item.setName(updatedItem.getName());
					item.setCategory(updatedItem.getCategory());
					
					return itemRepository.save(item);
				});
		
	}

	public void removeItem(Long itemId) {	  
	    itemRepository.deleteById(itemId);
	    	  
	}

	public Optional<Item> getItem(Long itemId) {
		
		 return itemRepository.findById(itemId);
	}

	public List<Item> getTotalItems() {
		return itemRepository.findAll();	
	}
	
//	public List<Restaurant> getRestaurantsById(Long itemId) {
//		return itemRepository.getRestaurantsByItemId(itemId);
//	}

	public void deleteItem(Long itemId) {
		itemRepository.deleteById(itemId);
		
	}
}
