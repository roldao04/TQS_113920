package tqs_hw1.meals.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    
    @ManyToMany
    @JoinTable(
        name = "reservation_meals",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "meal_id")
    )
    private List<Meal> meals;
    
    @Column(name = "reservation_time", nullable = false)
    private LocalDateTime reservationTime;
    
    @Column(name = "customer_username", nullable = false)
    private String customerUsername;
    
    @Column(name = "number_of_people", nullable = false)
    private Integer numberOfPeople;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // PENDING, CONFIRMED, CANCELLED, COMPLETED
    
    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;
} 