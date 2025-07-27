package com.prudhvi.FoodieExpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prudhvi.FoodieExpress.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

	public Address findByBuildingNameAndStreetNoAndAreaAndCityAndStateAndCountryAndPinCode(String buildingName,
			String streetNo, 
			String area, 
			String city, 
			String state, 
			String country, 
			String pinCode
	);
}
