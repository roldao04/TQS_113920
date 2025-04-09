package tqs_hw1.meals.dto;

import lombok.Data;
import tqs_hw1.meals.model.Restaurant;

@Data
public class RestaurantDTO {
    private Long id;
    private String name;
    private String address;
    private String description;
    private String phoneNumber;

    public static RestaurantDTO fromEntity(Restaurant restaurant) {
        RestaurantDTO dto = new RestaurantDTO();
        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setAddress(restaurant.getAddress());
        dto.setDescription(restaurant.getDescription());
        dto.setPhoneNumber(restaurant.getPhoneNumber());
        return dto;
    }
} 