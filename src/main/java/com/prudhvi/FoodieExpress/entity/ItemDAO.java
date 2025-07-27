package com.prudhvi.FoodieExpress.entity;

public class ItemDAO {

    private Item item;
    private Category category;
    private Double price;
    private String imagePath;
    private Restaurant restaurant;

    // Constructor
    public ItemDAO(Item item, Category category, Double price, String imagePath, Restaurant restaurant) {
        this.item = item;
        this.category = category;
        this.price = price;
        this.imagePath = imagePath;
        this.restaurant = restaurant;
    }

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}
}