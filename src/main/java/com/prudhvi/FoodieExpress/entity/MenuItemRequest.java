package com.prudhvi.FoodieExpress.entity;

import org.springframework.web.multipart.MultipartFile;

public class MenuItemRequest {
	
	private long restaurantId;
	
	private long id;
	
	private String name;
	
	private String category;
	
	private double price;
	
	private MultipartFile image;
	
	public long getRestaurantId() {
		return restaurantId;
	}
	
	public void setRestaurantId(long restaurantId) {
		this.restaurantId = restaurantId;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public MultipartFile getImage() {
		return image;
	}
	
	public void setImage(MultipartFile image) {
		this.image = image;
	}
}
