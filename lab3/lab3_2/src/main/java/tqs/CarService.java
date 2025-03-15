package tqs;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car save(Car car) {
        return carRepository.save(car);
    }

    public Optional<Car> getCarDetails(Long id) {
        return carRepository.findById(id);
    }

    public List<Car> findReplacementCar(Long id) {
        Optional<Car> originalCar = carRepository.findById(id);
        if (originalCar.isPresent()) {
            return carRepository.findBySegmentAndMotorType(
                originalCar.get().getSegment(),
                originalCar.get().getMotorType()
            );
        }
        return List.of();
    }
}
