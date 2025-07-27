package com.prudhvi.FoodieExpress.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class FoodCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(cascade = CascadeType.PERSIST) // Restrict cascading to persist only
    private Customer customer;

    @OneToMany(mappedBy = "foodCart", cascade = CascadeType.ALL, orphanRemoval = true) // Enable orphan removal
    private List<CartItem> cartItems = new ArrayList<>();
    
    private double totalItemsCost = 0;

    public double getTotalItemsCost() {
		return totalItemsCost;
	}

	public void setTotalItemsCost(double totalItemsCost) {
		this.totalItemsCost = totalItemsCost;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.add(cartItem);
        cartItem.setFoodCart(this); // Ensure bidirectional consistency
    }

    public void removeCartItem(CartItem cartItem) {
        cartItems.remove(cartItem);
        cartItem.setFoodCart(null); // Ensure bidirectional consistency
    }

    @Override
    public String toString() {
        return "FoodCart{" +
                "id=" + id +
                ", customer=" + (customer != null ? customer.getId() : "null") +
                ", cartItems=" + cartItems.size() +
                '}';
    }
}
