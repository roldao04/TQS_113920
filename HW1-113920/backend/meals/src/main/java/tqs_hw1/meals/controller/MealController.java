package tqs_hw1.meals.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Meals", description = "Meal management APIs")
public class MealController {

    private final MealRepository mealRepository;
    private final RestaurantService restaurantService;

    @Autowired
    public MealController(MealRepository mealRepository, RestaurantService restaurantService) {
        this.mealRepository = mealRepository;
        this.restaurantService = restaurantService;
    }

    @Operation(summary = "Get all meals", description = "Retrieves a list of all available meals")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meals",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = MealDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<MealDTO>> getAllMeals() {
        log.info("GET /meals - Retrieving all meals");
        List<MealDTO> meals = mealRepository.findAll().stream()
            .map(MealDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(meals);
    }

    @Operation(summary = "Get meals by restaurant", description = "Retrieves all meals for a specific restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meals",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = MealDTO.class))),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MealDTO>> getMealsByRestaurant(
            @Parameter(description = "ID of the restaurant to retrieve meals from") @PathVariable Long restaurantId) {
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

    @Operation(summary = "Get meals by category", description = "Retrieves all meals of a specific category")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meals",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = MealDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MealDTO>> getMealsByCategory(
            @Parameter(description = "Category of meals to retrieve") @PathVariable String category) {
        log.info("GET /meals/category/{} - Retrieving meals by category", category);
        List<MealDTO> meals = mealRepository.findByCategory(category).stream()
            .map(MealDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(meals);
    }

    @Operation(summary = "Get meals by restaurant and category", 
              description = "Retrieves all meals of a specific category from a specific restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved meals",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = MealDTO.class))),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/restaurant/{restaurantId}/category/{category}")
    public ResponseEntity<List<MealDTO>> getMealsByRestaurantAndCategory(
            @Parameter(description = "ID of the restaurant") @PathVariable Long restaurantId,
            @Parameter(description = "Category of meals to retrieve") @PathVariable String category) {
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