package tqs_hw1.meals.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tqs_hw1.meals.model.Meal;
import tqs_hw1.meals.model.Restaurant;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByRestaurant(Restaurant restaurant);
    List<Meal> findByCategory(String category);
    List<Meal> findByRestaurantAndCategory(Restaurant restaurant, String category);
} 