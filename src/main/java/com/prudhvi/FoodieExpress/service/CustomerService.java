package com.prudhvi.FoodieExpress.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prudhvi.FoodieExpress.entity.Address;
import com.prudhvi.FoodieExpress.entity.Customer;
import com.prudhvi.FoodieExpress.repository.AddressRepository;
import com.prudhvi.FoodieExpress.repository.CustomerRepository;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	
	public 	ResponseEntity<Customer> addCustomer(Customer customer) {
        HttpHeaders headers = new HttpHeaders();
        try {
            // Check if a restaurant with the same mobile number already exists
            if (customerRepository.existsByMobileNumber(customer.getMobileNumber())) {
                headers.add("Registration-Status", "Mobile number already exists!");
                return new ResponseEntity<>(null, headers, HttpStatus.CONFLICT);
            }

            // Check and encode the password
            if (customer.getPassword() == null || customer.getPassword().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be null or empty");
            }

            // Handle address
            Address address = handleAddress(customer.getAddress());
            customer.setAddress(address);
            
            customer = customerRepository.save(customer);
            headers.add("Registration-Status", "Registration successful!");
            return new ResponseEntity<>(customer, headers, HttpStatus.CREATED);
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
	
//	public String getNameByMobileNo(String mobileNumber) {
//		return customerRepo.findNameByMobileNumber(mobileNumber);
//	}

	public Optional<Customer> updateCustomer(Customer updatedCustomer) {
		
		return customerRepository.findById(updatedCustomer.getId())
				.map(customer -> {
					customer.setFirstName(updatedCustomer.getFirstName());
					customer.setLastname(updatedCustomer.getLastname());
					customer.setAge(updatedCustomer.getAge());
					customer.setGender(updatedCustomer.getGender());
					customer.setMobileNumber(updatedCustomer.getMobileNumber());
					customer.setEmail(updatedCustomer.getEmail());
					customer.setAddress(updatedCustomer.getAddress());
					
					return customerRepository.save(customer);
				});
		
	}

	public void removeCustomer(Long customerId) {
	    	  
	    customerRepository.deleteById(customerId);
	    	  
	}

	public Optional<Customer> viewCustomer(Long customerId) {
		
		 return customerRepository.findById(customerId);
	}

	public List<Customer> viewAllCustomer() {
		return customerRepository.findAll();	
	}

	public void deletePlayer(Long customerId) {
		customerRepository.deleteById(customerId);
		
	}
}
