package tqs_hw1.meals.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {
    private String address;
    private String timezone;
    private List<DayForecast> days = new ArrayList<>();
} 