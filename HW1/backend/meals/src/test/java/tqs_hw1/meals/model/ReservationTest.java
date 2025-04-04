package tqs_hw1.meals.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    void testReservationCreation() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        
        Meal meal1 = new Meal();
        meal1.setId(1L);
        meal1.setName("Dish 1");
        
        Meal meal2 = new Meal();
        meal2.setId(2L);
        meal2.setName("Dish 2");
        
        List<Meal> meals = new ArrayList<>();
        meals.add(meal1);
        meals.add(meal2);
        
        LocalDateTime reservationTime = LocalDateTime.now().plusDays(1);
        
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setRestaurant(restaurant);
        reservation.setMeals(meals);
        reservation.setReservationTime(reservationTime);
        reservation.setCustomerName("John Doe");
        reservation.setCustomerEmail("john@example.com");
        reservation.setCustomerPhone("123-456-7890");
        reservation.setNumberOfPeople(2);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setAccessToken("ABC123");
        
        assertEquals(1L, reservation.getId());
        assertEquals(restaurant, reservation.getRestaurant());
        assertEquals(meals, reservation.getMeals());
        assertEquals(reservationTime, reservation.getReservationTime());
        assertEquals("John Doe", reservation.getCustomerName());
        assertEquals("john@example.com", reservation.getCustomerEmail());
        assertEquals("123-456-7890", reservation.getCustomerPhone());
        assertEquals(2, reservation.getNumberOfPeople());
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus());
        assertEquals("ABC123", reservation.getAccessToken());
    }
    
    @Test
    void testReservationEquality() {
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setAccessToken("ABC123");
        
        Reservation reservation2 = new Reservation();
        reservation2.setId(1L);
        reservation2.setAccessToken("ABC123");
        
        assertEquals(reservation1, reservation2);
        assertEquals(reservation1.hashCode(), reservation2.hashCode());
    }

    @Test
    void testReservationNullFields() {
        Reservation reservation = new Reservation();
        assertNull(reservation.getId());
        assertNull(reservation.getRestaurant());
        assertNull(reservation.getMeals());
        assertNull(reservation.getReservationTime());
        assertNull(reservation.getCustomerName());
        assertNull(reservation.getCustomerEmail());
        assertNull(reservation.getCustomerPhone());
        assertNull(reservation.getNumberOfPeople());
        assertNull(reservation.getStatus());
        assertNull(reservation.getAccessToken());
    }

    @Test
    void testReservationWithDifferentFields() {
        // Create two reservations with different fields
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setCustomerName("John Doe");
        reservation1.setStatus(ReservationStatus.CONFIRMED);
        
        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setCustomerName("Jane Smith");
        reservation2.setStatus(ReservationStatus.PENDING);
        
        // They should not be equal
        assertNotEquals(reservation1, reservation2);
        assertNotEquals(reservation1.hashCode(), reservation2.hashCode());
        
        // Update reservation2 to match reservation1
        reservation2.setId(1L);
        reservation2.setCustomerName("John Doe");
        reservation2.setStatus(ReservationStatus.CONFIRMED);
        
        // Now they should be equal
        assertEquals(reservation1, reservation2);
        assertEquals(reservation1.hashCode(), reservation2.hashCode());
    }
} 