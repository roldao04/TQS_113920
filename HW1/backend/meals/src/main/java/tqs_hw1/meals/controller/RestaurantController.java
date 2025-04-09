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
import tqs_hw1.meals.dto.RestaurantDTO;
import tqs_hw1.meals.service.RestaurantService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/restaurants")
@CrossOrigin(origins = "http://localhost:8090", allowedHeaders = "*")
@Tag(name = "Restaurant", description = "Restaurant management APIs")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @Operation(summary = "Get all restaurants", description = "Retrieves a list of all available restaurants")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurants",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = RestaurantDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        log.info("GET /restaurants - Retrieving all restaurants");
        List<RestaurantDTO> restaurants = restaurantService.getAllRestaurants()
            .stream()
            .map(RestaurantDTO::fromEntity)
            .collect(Collectors.toList());
        return ResponseEntity.ok(restaurants);
    }

    @Operation(summary = "Get restaurant by ID", description = "Retrieves a specific restaurant by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurant",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = RestaurantDTO.class))),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(
            @Parameter(description = "ID of the restaurant to retrieve") @PathVariable Long id) {
        log.info("GET /restaurants/{} - Retrieving restaurant by ID", id);
        return restaurantService.getRestaurantById(id)
                .map(RestaurantDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get restaurant meals", description = "Retrieves all meals for a specific restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurant meals",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = RestaurantDTO.class))),
        @ApiResponse(responseCode = "404", description = "Restaurant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/meals")
    public ResponseEntity<RestaurantDTO> getRestaurantMeals(
            @Parameter(description = "ID of the restaurant to retrieve meals from") @PathVariable Long id) {
        log.info("GET /restaurants/{}/meals - Retrieving meals for restaurant", id);
        return restaurantService.getRestaurantById(id)
                .map(RestaurantDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 