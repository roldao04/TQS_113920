package tqs_hw1.meals.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs_hw1.meals.model.Restaurant;
import tqs_hw1.meals.repository.RestaurantRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    
    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }
    
    /**
     * Retrieves all restaurants
     *
     * @return List of all restaurants
     */
    public List<Restaurant> getAllRestaurants() {
        log.info("Retrieving all restaurants");
        return restaurantRepository.findAll();
    }
    
    /**
     * Retrieves a restaurant by ID
     *
     * @param id The ID of the restaurant to retrieve
     * @return Optional containing the restaurant if found, empty otherwise
     */
    public Optional<Restaurant> getRestaurantById(Long id) {
        log.info("Retrieving restaurant with ID: {}", id);
        return restaurantRepository.findById(id);
    }
    
    /**
     * Creates a new restaurant
     *
     * @param restaurant The restaurant to create
     * @return The created restaurant
     */
    public Restaurant createRestaurant(Restaurant restaurant) {
        log.info("Creating new restaurant: {}", restaurant.getName());
        return restaurantRepository.save(restaurant);
    }
    
    /**
     * Updates an existing restaurant
     *
     * @param id The ID of the restaurant to update
     * @param updatedRestaurant The updated restaurant data
     * @return The updated restaurant
     */
    public Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant) {
        Restaurant existingRestaurant = getRestaurantById(id).orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        
        if (updatedRestaurant.getName() != null) {
            existingRestaurant.setName(updatedRestaurant.getName());
        }
        if (updatedRestaurant.getAddress() != null) {
            existingRestaurant.setAddress(updatedRestaurant.getAddress());
        }
        if (updatedRestaurant.getDescription() != null) {
            existingRestaurant.setDescription(updatedRestaurant.getDescription());
        }
        
        return restaurantRepository.save(existingRestaurant);
    }
    
    /**
     * Deletes a restaurant by ID
     *
     * @param id The ID of the restaurant to delete
     */
    public void deleteRestaurant(Long id) {
        log.info("Deleting restaurant with ID: {}", id);
        restaurantRepository.deleteById(id);
    }

    public List<Restaurant> getRestaurantsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Restaurant> getRestaurantsByAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null or empty");
        }
        return restaurantRepository.findByAddressContainingIgnoreCase(address);
    }

    public void bulkDeleteRestaurants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("List of IDs cannot be null or empty");
        }
        restaurantRepository.deleteAllById(ids);
    }
} 