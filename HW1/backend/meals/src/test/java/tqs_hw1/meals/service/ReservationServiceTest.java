package tqs_hw1.meals.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs_hw1.meals.model.Meal;
import tqs_hw1.meals.model.Reservation;
import tqs_hw1.meals.model.ReservationStatus;
import tqs_hw1.meals.model.Restaurant;
import tqs_hw1.meals.repository.MealRepository;
import tqs_hw1.meals.repository.ReservationRepository;
import tqs_hw1.meals.repository.RestaurantRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;
    
    @Mock
    private RestaurantRepository restaurantRepository;
    
    @Mock
    private MealRepository mealRepository;
    
    @InjectMocks
    private ReservationService reservationService;
    
    private Restaurant restaurant;
    private Meal meal1;
    private Meal meal2;
    private Reservation reservation;
    private LocalDateTime reservationTime;
    
    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        
        meal1 = new Meal();
        meal1.setId(1L);
        meal1.setName("Meal 1");
        meal1.setRestaurant(restaurant);
        
        meal2 = new Meal();
        meal2.setId(2L);
        meal2.setName("Meal 2");
        meal2.setRestaurant(restaurant);
        
        List<Meal> meals = new ArrayList<>();
        meals.add(meal1);
        meals.add(meal2);
        
        reservationTime = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(18, 0));
        
        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setRestaurant(restaurant);
        reservation.setMeals(meals);
        reservation.setReservationTime(reservationTime);
        reservation.setCustomerUsername("John Doe");
        reservation.setNumberOfPeople(2);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setAccessToken("ABC123");
    }
    
    @Test
    void testCreateReservation_Success() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal1));
        when(mealRepository.findById(2L)).thenReturn(Optional.of(meal2));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        
        Reservation newReservation = new Reservation();
        newReservation.setRestaurant(restaurant);
        newReservation.setReservationTime(reservationTime);
        newReservation.setCustomerUsername("John Doe");
        newReservation.setNumberOfPeople(2);
        
        List<Long> mealIds = Arrays.asList(1L, 2L);
        
        Reservation result = reservationService.createReservation(newReservation, 1L, mealIds);
        
        assertNotNull(result);
        assertEquals(ReservationStatus.CONFIRMED, result.getStatus());
        assertEquals(2, result.getMeals().size());
        assertNotNull(result.getAccessToken());
        
        verify(restaurantRepository, times(1)).findById(1L);
        verify(mealRepository, times(1)).findById(1L);
        verify(mealRepository, times(1)).findById(2L);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }
    
    @Test
    void testCreateReservation_RestaurantNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
        
        Reservation newReservation = new Reservation();
        newReservation.setReservationTime(reservationTime);
        
        List<Long> mealIds = Arrays.asList(1L, 2L);
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(newReservation, 99L, mealIds);
        });
        
        assertTrue(exception.getMessage().contains("Restaurant not found"));
        
        verify(restaurantRepository, times(1)).findById(99L);
        verify(mealRepository, never()).findById(anyLong());
        verify(reservationRepository, never()).save(any());
    }
    
    @Test
    void testGetReservationByAccessToken_Found() {
        when(reservationRepository.findByAccessToken("ABC123")).thenReturn(Optional.of(reservation));
        
        Optional<Reservation> result = reservationService.getReservationByAccessToken("ABC123");
        
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getCustomerUsername());
        
        verify(reservationRepository, times(1)).findByAccessToken("ABC123");
    }
    
    @Test
    void testGetReservationByAccessToken_NotFound() {
        when(reservationRepository.findByAccessToken("NONEXISTENT")).thenReturn(Optional.empty());
        
        Optional<Reservation> result = reservationService.getReservationByAccessToken("NONEXISTENT");
        
        assertFalse(result.isPresent());
        
        verify(reservationRepository, times(1)).findByAccessToken("NONEXISTENT");
    }
    
    @Test
    void testUpdateReservationStatus_Success() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        
        Optional<Reservation> result = reservationService.updateReservationStatus(1L, ReservationStatus.COMPLETED);
        
        assertTrue(result.isPresent());
        assertEquals(ReservationStatus.COMPLETED, result.get().getStatus());
        
        verify(reservationRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).save(reservation);
    }
    
    @Test
    void testUpdateReservationStatus_NotFound() {
        when(reservationRepository.findById(99L)).thenReturn(Optional.empty());
        
        Optional<Reservation> result = reservationService.updateReservationStatus(99L, ReservationStatus.COMPLETED);
        
        assertFalse(result.isPresent());
        
        verify(reservationRepository, times(1)).findById(99L);
        verify(reservationRepository, never()).save(any());
    }
    
    @Test
    void testGetReservationsForRestaurant() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(reservationRepository.findByRestaurant(restaurant)).thenReturn(Arrays.asList(reservation));
        
        List<Reservation> results = reservationService.getReservationsForRestaurant(1L);
        
        assertEquals(1, results.size());
        assertEquals("John Doe", results.get(0).getCustomerUsername());
        
        verify(restaurantRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).findByRestaurant(restaurant);
    }
    
    @Test
    void testGetReservationsForRestaurant_RestaurantNotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.getReservationsForRestaurant(99L);
        });
        
        assertTrue(exception.getMessage().contains("Restaurant not found"));
        
        verify(restaurantRepository, times(1)).findById(99L);
        verify(reservationRepository, never()).findByRestaurant(any());
    }
    
    @Test
    void testCheckInReservation_Success() {
        when(reservationRepository.findByAccessToken("ABC123")).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        
        Optional<Reservation> result = reservationService.checkInReservation("ABC123");
        
        assertTrue(result.isPresent());
        assertEquals(ReservationStatus.COMPLETED, result.get().getStatus());
        
        verify(reservationRepository, times(1)).findByAccessToken("ABC123");
        verify(reservationRepository, times(1)).save(reservation);
    }
    
    @Test
    void testCheckInReservation_NotFound() {
        when(reservationRepository.findByAccessToken("NONEXISTENT")).thenReturn(Optional.empty());
        
        Optional<Reservation> result = reservationService.checkInReservation("NONEXISTENT");
        
        assertFalse(result.isPresent());
        
        verify(reservationRepository, times(1)).findByAccessToken("NONEXISTENT");
        verify(reservationRepository, never()).save(any());
    }
    
    @Test
    void testCheckInReservation_InvalidStatus() {
        Reservation cancelledReservation = new Reservation();
        cancelledReservation.setStatus(ReservationStatus.CANCELLED);
        
        when(reservationRepository.findByAccessToken("CANCELLED")).thenReturn(Optional.of(cancelledReservation));
        
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            reservationService.checkInReservation("CANCELLED");
        });
        
        assertTrue(exception.getMessage().contains("Cannot check in a reservation that is not confirmed"));
        
        verify(reservationRepository, times(1)).findByAccessToken("CANCELLED");
        verify(reservationRepository, never()).save(any());
    }
} 