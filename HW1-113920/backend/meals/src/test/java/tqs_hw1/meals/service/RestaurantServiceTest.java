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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        restaurant1.setName("Moliceiro Restaurant");
        restaurant1.setDescription("Traditional Portuguese cuisine");
        restaurant1.setAddress("Aveiro");
        restaurant1.setPhoneNumber("+351123456789");

        restaurant2 = new Restaurant();
        restaurant2.setId(2L);
        restaurant2.setName("Campus Cafe");
        restaurant2.setDescription("Quick meals and snacks");
        restaurant2.setAddress("University Campus");
        restaurant2.setPhoneNumber("+351987654321");
    }

    @Test
    void whenGetAllRestaurants_thenReturnAllRestaurants() {
        // Arrange
        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(restaurant1, restaurant2));

        // Act
        List<Restaurant> found = restaurantService.getAllRestaurants();

        // Assert
        assertThat(found)
            .hasSize(2)
            .containsExactly(restaurant1, restaurant2);
        verify(restaurantRepository).findAll();
    }

    @Test
    void whenGetRestaurantById_withValidId_thenReturnRestaurant() {
        // Arrange
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));

        // Act
        Optional<Restaurant> found = restaurantService.getRestaurantById(1L);

        // Assert
        assertThat(found)
            .isPresent()
            .hasValueSatisfying(r -> {
                assertThat(r.getId()).isEqualTo(1L);
                assertThat(r.getName()).isEqualTo("Moliceiro Restaurant");
                assertThat(r.getDescription()).isEqualTo("Traditional Portuguese cuisine");
            });
        verify(restaurantRepository).findById(1L);
    }

    @Test
    void whenGetRestaurantById_withInvalidId_thenReturnEmpty() {
        // Arrange
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Restaurant> found = restaurantService.getRestaurantById(99L);

        // Assert
        assertThat(found).isEmpty();
        verify(restaurantRepository).findById(99L);
    }

    @Test
    void whenCreateRestaurant_thenReturnCreatedRestaurant() {
        // Arrange
        when(restaurantRepository.save(restaurant1)).thenReturn(restaurant1);

        // Act
        Restaurant created = restaurantService.createRestaurant(restaurant1);

        // Assert
        assertThat(created)
            .isNotNull()
            .isEqualTo(restaurant1);
        verify(restaurantRepository).save(restaurant1);
    }

    @Test
    void whenCreateRestaurant_withNullName_thenThrowException() {
        // Arrange
        Restaurant invalidRestaurant = new Restaurant();
        invalidRestaurant.setDescription("Test Description");
        invalidRestaurant.setAddress("Test Address");
        invalidRestaurant.setPhoneNumber("+351123456789");

        when(restaurantRepository.save(any(Restaurant.class)))
            .thenThrow(new IllegalArgumentException("Restaurant name cannot be null"));

        // Act & Assert
        assertThatThrownBy(() -> restaurantService.createRestaurant(invalidRestaurant))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Restaurant name cannot be null");
    }

    @Test
    void whenUpdateRestaurant_withExistingId_thenReturnUpdatedRestaurant() {
        // Arrange
        Restaurant updatedData = new Restaurant();
        updatedData.setName("Updated Name");
        updatedData.setAddress("Updated Address");
        updatedData.setDescription("Updated Description");

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Restaurant result = restaurantService.updateRestaurant(1L, updatedData);

        // Assert
        assertThat(result)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.getName()).isEqualTo("Updated Name");
                assertThat(r.getAddress()).isEqualTo("Updated Address");
                assertThat(r.getDescription()).isEqualTo("Updated Description");
            });
        verify(restaurantRepository).findById(1L);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void whenUpdateRestaurant_withNonExistingId_thenThrowException() {
        // Arrange
        Restaurant updatedData = new Restaurant();
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> restaurantService.updateRestaurant(99L, updatedData))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Restaurant not found");
        verify(restaurantRepository).findById(99L);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void whenUpdateRestaurant_withPartialData_thenUpdateOnlyProvidedFields() {
        // Arrange
        Restaurant updatedData = new Restaurant();
        updatedData.setName("Updated Name");
        // Only updating name, other fields should remain unchanged

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Restaurant result = restaurantService.updateRestaurant(1L, updatedData);

        // Assert
        assertThat(result)
            .isNotNull()
            .satisfies(r -> {
                assertThat(r.getName()).isEqualTo("Updated Name");
                assertThat(r.getDescription()).isEqualTo(restaurant1.getDescription());
                assertThat(r.getAddress()).isEqualTo(restaurant1.getAddress());
            });
    }

    @Test
    void whenDeleteRestaurant_thenRepositoryMethodCalled() {
        // Arrange
        doNothing().when(restaurantRepository).deleteById(1L);

        // Act
        restaurantService.deleteRestaurant(1L);

        // Assert
        verify(restaurantRepository).deleteById(1L);
    }

    @Test
    void whenGetRestaurantsByName_thenReturnMatchingRestaurants() {
        // Arrange
        when(restaurantRepository.findByNameContainingIgnoreCase("Moliceiro"))
            .thenReturn(Arrays.asList(restaurant1));

        // Act
        List<Restaurant> found = restaurantService.getRestaurantsByName("Moliceiro");

        // Assert
        assertThat(found)
            .hasSize(1)
            .containsExactly(restaurant1);
        verify(restaurantRepository).findByNameContainingIgnoreCase("Moliceiro");
    }

    @Test
    void whenGetRestaurantsByAddress_thenReturnMatchingRestaurants() {
        // Arrange
        when(restaurantRepository.findByAddressContainingIgnoreCase("Aveiro"))
            .thenReturn(Arrays.asList(restaurant1));

        // Act
        List<Restaurant> found = restaurantService.getRestaurantsByAddress("Aveiro");

        // Assert
        assertThat(found)
            .hasSize(1)
            .containsExactly(restaurant1);
        verify(restaurantRepository).findByAddressContainingIgnoreCase("Aveiro");
    }

    @Test
    void whenBulkDeleteRestaurants_thenDeleteAll() {
        // Arrange
        List<Long> ids = Arrays.asList(1L, 2L);
        doNothing().when(restaurantRepository).deleteAllById(ids);

        // Act
        restaurantService.bulkDeleteRestaurants(ids);

        // Assert
        verify(restaurantRepository).deleteAllById(ids);
    }
} 