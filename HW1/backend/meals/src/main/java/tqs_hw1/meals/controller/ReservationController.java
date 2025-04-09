package tqs_hw1.meals.controller;

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

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationRequestDTO requestDTO) {
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

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        log.info("GET /reservations - Retrieving all reservations");
        List<ReservationDTO> reservations = reservationRepository.findAll().stream()
                .map(ReservationDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        log.info("GET /reservations/{} - Retrieving reservation by ID", id);
        return reservationRepository.findById(id)
                .map(ReservationDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<ReservationDTO> getReservationByToken(@PathVariable String token) {
        log.info("GET /reservations/token/{} - Retrieving reservation by token", token);
        return reservationRepository.findByAccessToken(token)
                .map(ReservationDTO::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ReservationDTO> updateReservationStatus(
            @PathVariable Long id,
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