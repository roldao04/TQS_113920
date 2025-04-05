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
     * @param updatedData The updated restaurant data
     * @return Optional containing the updated restaurant if found, empty otherwise
     */
    public Optional<Restaurant> updateRestaurant(Long id, Restaurant updatedData) {
        log.info("Updating restaurant with ID: {}", id);
        
        return restaurantRepository.findById(id)
                .map(existingRestaurant -> {
                    if (updatedData.getName() != null) {
                        existingRestaurant.setName(updatedData.getName());
                    }
                    if (updatedData.getAddress() != null) {
                        existingRestaurant.setAddress(updatedData.getAddress());
                    }
                    if (updatedData.getDescription() != null) {
                        existingRestaurant.setDescription(updatedData.getDescription());
                    }
                    if (updatedData.getPhoneNumber() != null) {
                        existingRestaurant.setPhoneNumber(updatedData.getPhoneNumber());
                    }
                    
                    return restaurantRepository.save(existingRestaurant);
                });
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
} 