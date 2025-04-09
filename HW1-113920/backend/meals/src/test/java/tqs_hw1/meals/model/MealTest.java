package tqs_hw1.meals.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MealTest {

    @Test
    void testMealCreation() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
        
        Meal meal = new Meal();
        meal.setId(1L);
        meal.setName("Test Meal");
        meal.setDescription("A test meal");
        meal.setPrice(new BigDecimal("15.99"));
        meal.setCategory("Main Course");
        meal.setRestaurant(restaurant);
        
        assertEquals(1L, meal.getId());
        assertEquals("Test Meal", meal.getName());
        assertEquals("A test meal", meal.getDescription());
        assertEquals(new BigDecimal("15.99"), meal.getPrice());
        assertEquals("Main Course", meal.getCategory());
        assertEquals(restaurant, meal.getRestaurant());
    }
    
    @Test
    void testMealEquality() {
        Meal meal1 = new Meal();
        meal1.setId(1L);
        meal1.setName("Test Meal");
        
        Meal meal2 = new Meal();
        meal2.setId(1L);
        meal2.setName("Test Meal");
        
        assertEquals(meal1, meal2);
        assertEquals(meal1.hashCode(), meal2.hashCode());
    }
} 