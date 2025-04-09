package tqs_hw1.meals.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs_hw1.meals.dto.RestaurantDTO;
import tqs_hw1.meals.service.RestaurantService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "http://localhost:8090", allowedHeaders = "*")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        log.info("GET /restaurants - Retrieving all restaurants");
        List<RestaurantDTO> restaurants = restaurantService.getAllRestaurants()
            .stream()
            .map(RestaurantDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        log.info("GET /restaurants/{} - Retrieving restaurant by ID", id);
        return restaurantService.getRestaurantById(id)
                .map(RestaurantDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/meals")
    public ResponseEntity<RestaurantDTO> getRestaurantMeals(@PathVariable Long id) {
        log.info("GET /restaurants/{}/meals - Retrieving meals for restaurant", id);
        return restaurantService.getRestaurantById(id)
                .map(RestaurantDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 