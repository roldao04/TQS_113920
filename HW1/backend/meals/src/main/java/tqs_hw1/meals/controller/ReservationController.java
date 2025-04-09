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
import tqs_hw1.meals.dto.ReservationDTO;
import tqs_hw1.meals.dto.ReservationRequestDTO;
import tqs_hw1.meals.model.Meal;
import tqs_hw1.meals.model.Reservation;
import tqs_hw1.meals.model.ReservationStatus;
import tqs_hw1.meals.model.Restaurant;
import tqs_hw1.meals.repository.MealRepository;
import tqs_hw1.meals.repository.ReservationRepository;
import tqs_hw1.meals.service.RestaurantService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "http://localhost:8090", allowedHeaders = "*")
@Tag(name = "Reservations", description = "Reservation management APIs")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final RestaurantService restaurantService;
    private final MealRepository mealRepository;

    @Autowired
    public ReservationController(ReservationRepository reservationRepository,
                               RestaurantService restaurantService,
                               MealRepository mealRepository) {
        this.reservationRepository = reservationRepository;
        this.restaurantService = restaurantService;
        this.mealRepository = mealRepository;
    }

    @Operation(summary = "Create a new reservation", description = "Creates a new reservation for a meal at a restaurant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservation created successfully",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ReservationDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "Restaurant or meal not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Reservation details", required = true)
            @RequestBody ReservationRequestDTO requestDTO) {
        log.info("POST /reservations - Creating new reservation");
        
        // Get restaurant
        Restaurant restaurant = restaurantService.getRestaurantById(requestDTO.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        
        // Get meal
        Meal meal = mealRepository.findById(requestDTO.getMealId())
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));

        // Create reservation
        Reservation reservation = new Reservation();
        reservation.setRestaurant(restaurant);
        reservation.setMeals(Collections.singletonList(meal));
        reservation.setReservationTime(requestDTO.getReservationTime());
        reservation.setCustomerUsername(requestDTO.getCustomerUsername());
        reservation.setNumberOfPeople(requestDTO.getNumberOfPeople());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setAccessToken(requestDTO.getAccessToken());

        // Save reservation
        reservation = reservationRepository.save(reservation);
        
        return ResponseEntity.ok(ReservationDTO.fromEntity(reservation));
    }

    @Operation(summary = "Get all reservations", description = "Retrieves a list of all reservations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved reservations",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ReservationDTO.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        log.info("GET /reservations - Retrieving all reservations");
        List<ReservationDTO> reservations = reservationRepository.findAll().stream()
                .map(ReservationDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @Operation(summary = "Get reservation by ID", description = "Retrieves a specific reservation by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved reservation",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ReservationDTO.class))),
        @ApiResponse(responseCode = "404", description = "Reservation not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(
            @Parameter(description = "ID of the reservation to retrieve") @PathVariable Long id) {
        log.info("GET /reservations/{} - Retrieving reservation by ID", id);
        return reservationRepository.findById(id)
                .map(ReservationDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get reservation by token", description = "Retrieves a specific reservation by its access token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved reservation",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ReservationDTO.class))),
        @ApiResponse(responseCode = "404", description = "Reservation not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/token/{token}")
    public ResponseEntity<ReservationDTO> getReservationByToken(
            @Parameter(description = "Access token of the reservation") @PathVariable String token) {
        log.info("GET /reservations/token/{} - Retrieving reservation by token", token);
        return reservationRepository.findByAccessToken(token)
                .map(ReservationDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update reservation status", description = "Updates the status of a specific reservation")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated reservation status",
                    content = @Content(mediaType = "application/json", 
                    schema = @Schema(implementation = ReservationDTO.class))),
        @ApiResponse(responseCode = "404", description = "Reservation not found"),
        @ApiResponse(responseCode = "400", description = "Invalid status value"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}/status")
    public ResponseEntity<ReservationDTO> updateReservationStatus(
            @Parameter(description = "ID of the reservation to update") @PathVariable Long id,
            @Parameter(description = "New status for the reservation", 
                      schema = @Schema(implementation = ReservationStatus.class))
            @RequestParam ReservationStatus status) {
        log.info("PUT /reservations/{}/status - Updating reservation status to {}", id, status);
        
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservation.setStatus(status);
                    return ReservationDTO.fromEntity(reservationRepository.save(reservation));
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 