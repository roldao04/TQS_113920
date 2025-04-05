package tqs_hw1.meals.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs_hw1.meals.model.Restaurant;
import tqs_hw1.meals.repository.RestaurantRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    
    @InjectMocks
    private RestaurantService restaurantService;
    
    private Restaurant restaurant1;
    private Restaurant restaurant2;
    
    @BeforeEach
    void setUp() {
        restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setName("Restaurant 1");
        restaurant1.setAddress("Address 1");
        
        restaurant2 = new Restaurant();
        restaurant2.setId(2L);
        restaurant2.setName("Restaurant 2");
        restaurant2.setAddress("Address 2");
    }
    
    @Test
    void testGetAllRestaurants() {
        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(restaurant1, restaurant2));
        
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        
        assertEquals(2, restaurants.size());
        assertEquals("Restaurant 1", restaurants.get(0).getName());
        assertEquals("Restaurant 2", restaurants.get(1).getName());
        
        verify(restaurantRepository, times(1)).findAll();
    }
    
    @Test
    void testGetRestaurantById_Found() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));
        
        Optional<Restaurant> result = restaurantService.getRestaurantById(1L);
        
        assertTrue(result.isPresent());
        assertEquals("Restaurant 1", result.get().getName());
        
        verify(restaurantRepository, times(1)).findById(1L);
    }
    
    @Test
    void testGetRestaurantById_NotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
        
        Optional<Restaurant> result = restaurantService.getRestaurantById(99L);
        
        assertFalse(result.isPresent());
        
        verify(restaurantRepository, times(1)).findById(99L);
    }
    
    @Test
    void testCreateRestaurant() {
        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName("New Restaurant");
        
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(restaurant1);
        
        Restaurant created = restaurantService.createRestaurant(newRestaurant);
        
        assertNotNull(created);
        assertEquals("Restaurant 1", created.getName());
        
        verify(restaurantRepository, times(1)).save(newRestaurant);
    }
    
    @Test
    void testUpdateRestaurant_Success() {
        Restaurant updatedData = new Restaurant();
        updatedData.setName("Updated Name");
        updatedData.setAddress("Updated Address");
        
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));
        
        Optional<Restaurant> result = restaurantService.updateRestaurant(1L, updatedData);
        
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
        assertEquals("Updated Address", result.get().getAddress());
        
        verify(restaurantRepository, times(1)).findById(1L);
        verify(restaurantRepository, times(1)).save(restaurant1);
    }
    
    @Test
    void testUpdateRestaurant_NotFound() {
        Restaurant updatedData = new Restaurant();
        
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());
        
        Optional<Restaurant> result = restaurantService.updateRestaurant(99L, updatedData);
        
        assertFalse(result.isPresent());
        
        verify(restaurantRepository, times(1)).findById(99L);
        verify(restaurantRepository, never()).save(any());
    }
    
    @Test
    void testDeleteRestaurant() {
        doNothing().when(restaurantRepository).deleteById(1L);
        
        restaurantService.deleteRestaurant(1L);
        
        verify(restaurantRepository, times(1)).deleteById(1L);
    }
} 