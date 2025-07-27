package com.prudhvi.FoodieExpress.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.prudhvi.FoodieExpress.entity.RestaurantItem;
import com.prudhvi.FoodieExpress.entity.ItemDAO;

public interface RestaurantItemRepository extends JpaRepository<RestaurantItem, Long>{

	Optional<RestaurantItem> findByItemIdAndRestaurantId(long itemId, long restaurantId);

	Optional<RestaurantItem> findByIdAndRestaurantId(Long menuItemId, Long restaurantId);

	void deleteByRestaurantIdAndItemId(long id, long itemID);
	
	@Query("SELECT new com.prudhvi.FoodieExpress.entity.ItemDAO(i, c, ri.cost, ri.imagePath, r) " +
		       "FROM Restaurant r " +
		       "JOIN RestaurantItem ri ON r = ri.restaurant " +
		       "JOIN Item i ON ri.item = i " +
		       "JOIN Category c ON i.category= c")
	public List<ItemDAO> getAllItemDetails();

}
