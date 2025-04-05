package tqs_hw1.meals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs_hw1.meals.model.Reservation;
import tqs_hw1.meals.model.Restaurant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRestaurant(Restaurant restaurant);
    
    List<Reservation> findByRestaurantAndReservationTimeBetween(
            Restaurant restaurant, 
            LocalDateTime startDateTime, 
            LocalDateTime endDateTime);
    
    Optional<Reservation> findByAccessToken(String accessToken);
    
    List<Reservation> findByCustomerEmail(String email);
    
    List<Reservation> findByStatus(String status);
} 