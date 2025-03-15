import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import tqs.Car;
import tqs.CarRepository;
import tqs.CarService;

class CarServiceTest {

    private CarService carService;

    @Mock
    private CarRepository carRepository;

    private Car car1, car2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        carService = new CarService(carRepository);

        car1 = new Car(1L, "Toyota", "Corolla", "Sedan", "Gasoline");
        car2 = new Car(2L, "Honda", "Civic", "Sedan", "Gasoline");
    }

    @Test
    void whenFindReplacementCar_thenReturnMatchingCars() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(car1));
        when(carRepository.findBySegmentAndMotorType("Sedan", "Gasoline"))
                .thenReturn(List.of(car2));

        List<Car> replacements = carService.findReplacementCar(1L);

        assertFalse(replacements.isEmpty());
        assertEquals("Honda", replacements.get(0).getMaker());

        verify(carRepository, times(1)).findBySegmentAndMotorType("Sedan", "Gasoline");
    }
}
