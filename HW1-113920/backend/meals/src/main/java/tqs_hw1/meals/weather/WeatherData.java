package tqs_hw1.meals.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherData implements Serializable {
    
    private String location;
    private LocalDate date;
    private Double temperature;
    private String condition;
    private Double maxTemperature;
    private Double minTemperature;
    private String icon;
    private Double precipitation;
    private Double humidity;
    private Double windSpeed;
    
    // For caching purposes
    private Long timestamp;
} 