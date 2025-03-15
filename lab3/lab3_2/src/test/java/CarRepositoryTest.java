import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

import tqs.Car;
import tqs.CarRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    void whenSaveCar_thenFindById() {
        Car car = new Car(null, "Toyota", "Corolla", "Sedan", "Gasoline");
        Car savedCar = carRepository.save(car);

        Optional<Car> foundCar = carRepository.findById(savedCar.getId());
        assertTrue(foundCar.isPresent());
        assertEquals("Toyota", foundCar.get().getMaker());
    }

    @Test
    void whenFindBySegmentAndMotorType_thenReturnCars() {
        Car car1 = new Car(null, "Toyota", "Corolla", "Sedan", "Gasoline");
        Car car2 = new Car(null, "Honda", "Civic", "Sedan", "Gasoline");
        carRepository.save(car1);
        carRepository.save(car2);

        List<Car> results = carRepository.findBySegmentAndMotorType("Sedan", "Gasoline");
        assertEquals(2, results.size());
    }
}
