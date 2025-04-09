package tqs_hw1.meals.dto;

import lombok.Data;
import tqs_hw1.meals.model.Reservation;
import tqs_hw1.meals.model.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ReservationDTO {
    private Long id;
    private Long restaurantId;
    private String restaurantName;
    private List<MealDTO> meals;
    private LocalDateTime reservationTime;
    private String customerUsername;
    private Integer numberOfPeople;
    private ReservationStatus status;
    private String accessToken;

    public static ReservationDTO fromEntity(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());
        dto.setRestaurantId(reservation.getRestaurant().getId());
        dto.setRestaurantName(reservation.getRestaurant().getName());
        dto.setMeals(reservation.getMeals().stream()
                .map(MealDTO::fromEntity)
                .collect(Collectors.toList()));
        dto.setReservationTime(reservation.getReservationTime());
        dto.setCustomerUsername(reservation.getCustomerUsername());
        dto.setNumberOfPeople(reservation.getNumberOfPeople());
        dto.setStatus(reservation.getStatus());
        dto.setAccessToken(reservation.getAccessToken());
        return dto;
    }
} 