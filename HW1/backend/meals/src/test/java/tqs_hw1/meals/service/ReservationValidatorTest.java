package tqs_hw1.meals.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs_hw1.meals.model.Reservation;
import tqs_hw1.meals.model.Restaurant;
import tqs_hw1.meals.repository.ReservationRepository;
import tqs_hw1.meals.repository.RestaurantRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationValidatorTest {

    @Mock
    private ReservationRepository reservationRepository;
    
    @Mock
    private RestaurantRepository restaurantRepository;
    
    @InjectMocks
    private ReservationValidator reservationValidator;
    
    @Test
    void testValidateReservation_ValidRequest() {
        // Setup
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        
        LocalDateTime reservationTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0));
        
        Reservation reservation = new Reservation();
        reservation.setRestaurant(restaurant);
        reservation.setReservationTime(reservationTime);
        reservation.setCustomerName("John Doe");
        reservation.setCustomerEmail("john@example.com");
        reservation.setNumberOfPeople(2);
        
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(reservationRepository.findByRestaurantAndReservationTimeBetween(
                eq(restaurant), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Arrays.asList());
        
        // Execute
        ValidationResult result = reservationValidator.validateReservation(reservation, 1L);
        
        // Verify
        assertTrue(result.isValid());
        assertNull(result.getErrorMessage());
    }
    
    @Test
    void testValidateReservation_PastDate() {
        // Setup
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        
        LocalDateTime pastTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(18, 0));
        
        Reservation reservation = new Reservation();
        reservation.setRestaurant(restaurant);
        reservation.setReservationTime(pastTime);
        reservation.setCustomerName("John Doe");
        reservation.setNumberOfPeople(2);
        
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        
        // Execute
        ValidationResult result = reservationValidator.validateReservation(reservation, 1L);
        
        // Verify
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("past"));
    }
    
    @Test
    void testValidateReservation_RestaurantAtCapacity() {
        // Setup
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        
        LocalDateTime reservationTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0));
        
        Reservation reservation = new Reservation();
        reservation.setRestaurant(restaurant);
        reservation.setReservationTime(reservationTime);
        reservation.setCustomerName("John Doe");
        reservation.setNumberOfPeople(5); // Need 5 more people to exceed capacity of 30
        
        // Create existing reservations totaling 26 people
        Reservation existingReservation1 = new Reservation();
        existingReservation1.setNumberOfPeople(15);
        
        Reservation existingReservation2 = new Reservation();
        existingReservation2.setNumberOfPeople(11);
        
        List<Reservation> existingReservations = Arrays.asList(existingReservation1, existingReservation2);
        
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(reservationRepository.findByRestaurantAndReservationTimeBetween(
                eq(restaurant), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(existingReservations);
        
        // Execute
        ValidationResult result = reservationValidator.validateReservation(reservation, 1L);
        
        // Verify
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("capacity"));
    }
    
    @Test
    void testValidateReservation_InvalidCustomerDetails() {
        // Setup
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        
        LocalDateTime reservationTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0));
        
        Reservation reservation = new Reservation();
        reservation.setRestaurant(restaurant);
        reservation.setReservationTime(reservationTime);
        reservation.setCustomerName(""); // Empty name
        reservation.setNumberOfPeople(2);
        
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        
        // Execute
        ValidationResult result = reservationValidator.validateReservation(reservation, 1L);
        
        // Verify
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().contains("name"));
    }
    
    @Test
    void testValidateReservation_TooManyPeople() {
        // Setup
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        
        LocalDateTime reservationTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0));
        
        Reservation reservation = new Reservation();
        reservation.setRestaurant(restaurant);
        reservation.setReservationTime(reservationTime);
        reservation.setCustomerName("John Doe");
        reservation.setNumberOfPeople(21); // Too many people for one reservation
        
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        
        // Execute
        ValidationResult result = reservationValidator.validateReservation(reservation, 1L);
        
        // Verify
        assertFalse(result.isValid());
        assertTrue(result.getErrorMessage().toLowerCase().contains("maximum"));
    }
} 