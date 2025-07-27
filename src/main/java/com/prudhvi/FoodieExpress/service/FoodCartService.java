package com.prudhvi.FoodieExpress.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prudhvi.FoodieExpress.entity.CartItem;
import com.prudhvi.FoodieExpress.entity.Customer;
import com.prudhvi.FoodieExpress.entity.FoodCart;
import com.prudhvi.FoodieExpress.entity.RestaurantItem;
import com.prudhvi.FoodieExpress.repository.CustomerRepository;
import com.prudhvi.FoodieExpress.repository.FoodCartRepository;
import com.prudhvi.FoodieExpress.repository.RestaurantItemRepository;

@Service
public class FoodCartService {

    @Autowired
    private FoodCartRepository cartRepository;

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private RestaurantItemRepository restaurantItemRepository;

    public FoodCart addItemToCart(Long customerId, Long itemId, Long restaurantId) {
        // Find the customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Check if the customer already has a cart, otherwise create one
        FoodCart cart = customer.getCart();
        if (cart == null) {
            cart = new FoodCart();
            cart.setCustomer(customer);  // Link the cart to the customer
            customer.setCart(cart);      // Link the customer to the cart
            cartRepository.save(cart); // Save the cart to the database
        }
        
        Optional<RestaurantItem> restaurantItem = restaurantItemRepository.findByItemIdAndRestaurantId(itemId, restaurantId);

        // Check if the item already exists in the cart
        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(cartItem -> String.valueOf(cartItem.getRestaurantItem().getId()).equals(String.valueOf(restaurantItem.get().getId()))) // Compare using Long objects
                .findFirst();

        if (existingCartItem.isPresent()) {
            // Item already exists, update the quantity
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + 1);  // Increase quantity
        } else {
            // Item doesn't exist in cart, create a new CartItem
            CartItem cartItem = new CartItem();
            cartItem.setRestaurantItem(restaurantItem.get());
            cartItem.setQuantity(1);
            cartItem.setFoodCart(cart);  // Link to the Cart
            cart.addCartItem(cartItem); // Add CartItem to Cart
        }
        
        cart.setTotalItemsCost(cart.getTotalItemsCost() + restaurantItem.get().getCost());
        
        cartRepository.save(cart); 
        return cart;
    }
    
    public FoodCart removeItemFromCart(Long customerId, Optional<CartItem> optional) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        FoodCart cart = customer.getCart();

        // Check if the cart contains the item
        if (cart.getCartItems().removeIf(cartItem -> cartItem.equals(optional.get()))) {
            optional.get().setFoodCart(null); // Unlink the item from cart (if needed)
            cart.setTotalItemsCost(cart.getTotalItemsCost() - (optional.get().getQuantity() * optional.get().getRestaurantItem().getCost()));
            return cartRepository.save(cart);
        } else {
            throw new RuntimeException("Item not found in cart");
        }
    }
}