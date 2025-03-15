import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import tqs.Car;
import tqs.CarController;
import tqs.CarService;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarService carService;

    @InjectMocks
    private CarController carController;

    private Car car1, car2;

    @BeforeEach
    void setUp() {
        car1 = new Car(1L, "Toyota", "Corolla", "Sedan", "Gasoline");
        car2 = new Car(2L, "Honda", "Civic", "Sedan", "Gasoline");
    }

    @Test
    void whenGetCarById_thenReturnCar() {
        when(carService.getCarDetails(1L)).thenReturn(Optional.of(car1));

        ResponseEntity<Car> response = carController.getCarById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Toyota", response.getBody().getMaker());

        verify(carService, times(1)).getCarDetails(1L);
    }

    @Test
    void whenFindReplacementCar_thenReturnMatchingCars() {
        when(carService.findReplacementCar(1L)).thenReturn(List.of(car2));

        ResponseEntity<List<Car>> response = carController.findReplacement(1L);

        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().isEmpty());
        assertEquals("Honda", response.getBody().get(0).getMaker());

        verify(carService, times(1)).findReplacementCar(1L);
    }
}
