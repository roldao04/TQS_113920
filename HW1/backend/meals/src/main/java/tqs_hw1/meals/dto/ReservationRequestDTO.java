package tqs_hw1.meals.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationRequestDTO {
    private Long restaurantId;
    private Long mealId;
    private LocalDateTime reservationTime;
    private Integer numberOfPeople;
    private String customerUsername;
    private String accessToken;
} 