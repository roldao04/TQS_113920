package tqs_hw1.meals.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs_hw1.meals.model.Reservation;
import tqs_hw1.meals.model.Restaurant;
import tqs_hw1.meals.repository.ReservationRepository;
import tqs_hw1.meals.repository.RestaurantRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReservationValidator {

    private static final int MAX_PEOPLE_PER_RESERVATION = 20;
    private static final int RESTAURANT_MAX_CAPACITY = 30;
    
    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    
    @Autowired
    public ReservationValidator(ReservationRepository reservationRepository, 
                                RestaurantRepository restaurantRepository) {
        this.reservationRepository = reservationRepository;
        this.restaurantRepository = restaurantRepository;
    }
    
    /**
     * Validates a reservation request
     * 
     * @param reservation The reservation to validate
     * @param restaurantId The ID of the restaurant for the reservation
     * @return ValidationResult indicating whether the reservation is valid
     */
    public ValidationResult validateReservation(Reservation reservation, Long restaurantId) {
        log.info("Validating reservation for restaurant ID: {}", restaurantId);
        
        // Check if restaurant exists
        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            return ValidationResult.invalid("Restaurant not found with ID: " + restaurantId);
        }
        
        Restaurant restaurant = restaurantOpt.get();
        
        // Check reservation date/time
        LocalDateTime now = LocalDateTime.now();
        if (reservation.getReservationTime().isBefore(now)) {
            return ValidationResult.invalid("Reservation time cannot be in the past");
        }
        
        // Check customer details
        if (reservation.getCustomerUsername() == null || reservation.getCustomerUsername().trim().isEmpty()) {
            return ValidationResult.invalid("Customer username is required");
        }
        
        // Check number of people
        if (reservation.getNumberOfPeople() <= 0) {
            return ValidationResult.invalid("Number of people must be greater than 0");
        }
        
        if (reservation.getNumberOfPeople() > MAX_PEOPLE_PER_RESERVATION) {
            return ValidationResult.invalid("Maximum number of people per reservation is " + MAX_PEOPLE_PER_RESERVATION);
        }
        
        // Check restaurant capacity
        LocalDateTime startTime = LocalDateTime.of(
                reservation.getReservationTime().toLocalDate(), 
                LocalTime.of(0, 0));
        LocalDateTime endTime = LocalDateTime.of(
                reservation.getReservationTime().toLocalDate(), 
                LocalTime.of(23, 59, 59));
        
        List<Reservation> existingReservations = reservationRepository
                .findByRestaurantAndReservationTimeBetween(restaurant, startTime, endTime);
        
        int totalPeople = existingReservations.stream()
                .mapToInt(Reservation::getNumberOfPeople)
                .sum();
        
        if (totalPeople + reservation.getNumberOfPeople() > RESTAURANT_MAX_CAPACITY) {
            return ValidationResult.invalid("Restaurant has reached capacity for the selected date");
        }
        
        return ValidationResult.valid();
    }
} 