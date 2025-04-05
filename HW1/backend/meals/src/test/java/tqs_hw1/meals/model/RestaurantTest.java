package tqs_hw1.meals.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    void testRestaurantCreation() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        restaurant.setAddress("123 Test Street");
        restaurant.setDescription("A test restaurant");
        restaurant.setPhoneNumber("123-456-7890");
        
        assertEquals(1L, restaurant.getId());
        assertEquals("Test Restaurant", restaurant.getName());
        assertEquals("123 Test Street", restaurant.getAddress());
        assertEquals("A test restaurant", restaurant.getDescription());
        assertEquals("123-456-7890", restaurant.getPhoneNumber());
    }
    
    @Test
    void testRestaurantEquality() {
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setName("Test Restaurant");
        
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setId(1L);
        restaurant2.setName("Test Restaurant");
        
        assertEquals(restaurant1, restaurant2);
        assertEquals(restaurant1.hashCode(), restaurant2.hashCode());
    }
} 