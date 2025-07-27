package com.prudhvi.FoodieExpress.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @OneToOne
    @JoinColumn(name = "restuarant_item_id", unique = true)
    private RestaurantItem restaurantItem;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private FoodCart foodCart;
    
    private int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RestaurantItem getRestaurantItem() {
		return restaurantItem;
	}

	public void setRestaurantItem(RestaurantItem restaurantItem) {
		this.restaurantItem = restaurantItem;
	}

	public FoodCart getFoodCart() {
		return foodCart;
	}

	public void setFoodCart(FoodCart foodCart) {
		this.foodCart = foodCart;
	}
}
