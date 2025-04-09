package tqs_hw1.meals.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tqs_hw1.meals.model.Meal;
import tqs_hw1.meals.model.Reservation;
import tqs_hw1.meals.model.ReservationStatus;
import tqs_hw1.meals.model.Restaurant;
import tqs_hw1.meals.repository.MealRepository;
import tqs_hw1.meals.repository.ReservationRepository;
import tqs_hw1.meals.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final MealRepository mealRepository;
    
    @Autowired
    public ReservationService(ReservationRepository reservationRepository, 
                              RestaurantRepository restaurantRepository,
                              MealRepository mealRepository) {
        this.reservationRepository = reservationRepository;
        this.restaurantRepository = restaurantRepository;
        this.mealRepository = mealRepository;
    }
    
    /**
     * Creates a new reservation
     *
     * @param reservation The reservation details
     * @param restaurantId The ID of the restaurant for the reservation
     * @param mealIds The list of meal IDs to include in the reservation
     * @return The created reservation
     * @throws IllegalArgumentException if the restaurant or any meal is not found
     */
    @Transactional
    public Reservation createReservation(Reservation reservation, Long restaurantId, List<Long> mealIds) {
        log.info("Creating reservation for restaurant ID: {} with {} meals", restaurantId, mealIds.size());
        
        // Set the restaurant
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + restaurantId));
        reservation.setRestaurant(restaurant);
        
        // Set the meals
        List<Meal> meals = new ArrayList<>();
        for (Long mealId : mealIds) {
            Meal meal = mealRepository.findById(mealId)
                    .orElseThrow(() -> new IllegalArgumentException("Meal not found with ID: " + mealId));
            meals.add(meal);
        }
        reservation.setMeals(meals);
        
        // Generate access token
        reservation.setAccessToken(generateAccessToken());
        
        // Set default status if not provided
        if (reservation.getStatus() == null) {
            reservation.setStatus(ReservationStatus.CONFIRMED);
        }
        
        return reservationRepository.save(reservation);
    }
    
    /**
     * Retrieves a reservation by its access token
     *
     * @param accessToken The access token of the reservation
     * @return Optional containing the reservation if found, empty otherwise
     */
    public Optional<Reservation> getReservationByAccessToken(String accessToken) {
        log.info("Retrieving reservation with access token: {}", accessToken);
        return reservationRepository.findByAccessToken(accessToken);
    }
    
    /**
     * Updates the status of a reservation
     *
     * @param id The ID of the reservation to update
     * @param status The new status
     * @return Optional containing the updated reservation if found, empty otherwise
     */
    @Transactional
    public Optional<Reservation> updateReservationStatus(Long id, ReservationStatus status) {
        log.info("Updating reservation status for ID: {} to {}", id, status);
        
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    existingReservation.setStatus(status);
                    return reservationRepository.save(existingReservation);
                });
    }
    
    /**
     * Retrieves all reservations for a specific restaurant
     *
     * @param restaurantId The ID of the restaurant
     * @return List of reservations for the restaurant
     * @throws IllegalArgumentException if the restaurant is not found
     */
    public List<Reservation> getReservationsForRestaurant(Long restaurantId) {
        log.info("Retrieving reservations for restaurant ID: {}", restaurantId);
        
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + restaurantId));
        
        return reservationRepository.findByRestaurant(restaurant);
    }
    
    /**
     * Retrieves reservations for a specific restaurant and time period
     *
     * @param restaurantId The ID of the restaurant
     * @param startDateTime The start datetime of the period
     * @param endDateTime The end datetime of the period
     * @return List of reservations for the restaurant and time period
     * @throws IllegalArgumentException if the restaurant is not found
     */
    public List<Reservation> getReservationsForRestaurantAndPeriod(Long restaurantId, 
                                                                   LocalDateTime startDateTime, 
                                                                   LocalDateTime endDateTime) {
        log.info("Retrieving reservations for restaurant ID: {} between {} and {}", 
                restaurantId, startDateTime, endDateTime);
        
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + restaurantId));
        
        return reservationRepository.findByRestaurantAndReservationTimeBetween(
                restaurant, startDateTime, endDateTime);
    }
    
    /**
     * Check in a reservation by marking it as COMPLETED
     *
     * @param accessToken The access token of the reservation to check in
     * @return Optional containing the updated reservation if found, empty otherwise
     */
    @Transactional
    public Optional<Reservation> checkInReservation(String accessToken) {
        log.info("Checking in reservation with access token: {}", accessToken);
        
        return reservationRepository.findByAccessToken(accessToken)
                .map(reservation -> {
                    if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
                        throw new IllegalStateException("Cannot check in a reservation that is not confirmed. Current status: " + reservation.getStatus());
                    }
                    reservation.setStatus(ReservationStatus.COMPLETED);
                    return reservationRepository.save(reservation);
                });
    }
    
    /**
     * Generates a unique access token for a reservation
     *
     * @return The generated access token
     */
    private String generateAccessToken() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }   
} 