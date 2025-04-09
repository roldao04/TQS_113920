package tqs_hw1.meals.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs_hw1.meals.dto.MealDTO;
import tqs_hw1.meals.repository.MealRepository;
import tqs_hw1.meals.service.RestaurantService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/meals")
@CrossOrigin(origins = "http://localhost:8090", allowedHeaders = "*")
public class MealController {

    private final MealRepository mealRepository;
    private final RestaurantService restaurantService;

    @Autowired
    public MealController(MealRepository mealRepository, RestaurantService restaurantService) {
        this.mealRepository = mealRepository;
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<MealDTO>> getAllMeals() {
        log.info("GET /meals - Retrieving all meals");
        List<MealDTO> meals = mealRepository.findAll().stream()
            .map(MealDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MealDTO>> getMealsByRestaurant(@PathVariable Long restaurantId) {
        log.info("GET /meals/restaurant/{} - Retrieving meals for restaurant", restaurantId);
        return restaurantService.getRestaurantById(restaurantId)
                .map(restaurant -> {
                    List<MealDTO> meals = mealRepository.findByRestaurant(restaurant).stream()
                        .map(MealDTO::fromEntity)
                        .collect(Collectors.toList());
                    return ResponseEntity.ok(meals);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MealDTO>> getMealsByCategory(@PathVariable String category) {
        log.info("GET /meals/category/{} - Retrieving meals by category", category);
        List<MealDTO> meals = mealRepository.findByCategory(category).stream()
            .map(MealDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/restaurant/{restaurantId}/category/{category}")
    public ResponseEntity<List<MealDTO>> getMealsByRestaurantAndCategory(
            @PathVariable Long restaurantId,
            @PathVariable String category) {
        log.info("GET /meals/restaurant/{}/category/{} - Retrieving meals by restaurant and category", 
                restaurantId, category);
        return restaurantService.getRestaurantById(restaurantId)
                .map(restaurant -> {
                    List<MealDTO> meals = mealRepository.findByRestaurantAndCategory(restaurant, category).stream()
                        .map(MealDTO::fromEntity)
                        .collect(Collectors.toList());
                    return ResponseEntity.ok(meals);
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 