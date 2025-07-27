package com.prudhvi.FoodieExpress.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	
	private String manager;
	
	private String mobileNumber;
	
	private String password;
	
	@Transient
	private final String role = "RESTAURANT";
	
	public String getRole() {
		return role;
	}

	@JsonBackReference("restaurant-address")
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Address address;
	
	@JsonManagedReference("restaurant-items")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RestaurantItem> restaurantItems = new ArrayList<>();

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

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<RestaurantItem> getRestaurantItems() {
		return restaurantItems;
	}

	public void setRestaurantItems(List<RestaurantItem> restaurantItems) {
		this.restaurantItems = restaurantItems;
	}
}
