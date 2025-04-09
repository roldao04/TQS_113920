package tqs_hw1.meals.dto;

import lombok.Data;
import tqs_hw1.meals.model.Meal;
import java.math.BigDecimal;

@Data
public class MealDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private Long restaurantId;

    public static MealDTO fromEntity(Meal meal) {
        MealDTO dto = new MealDTO();
        dto.setId(meal.getId());
        dto.setName(meal.getName());
        dto.setDescription(meal.getDescription());
        dto.setPrice(meal.getPrice());
        dto.setCategory(meal.getCategory());
        if (meal.getRestaurant() != null) {
            dto.setRestaurantId(meal.getRestaurant().getId());
        }
        return dto;
    }
} 