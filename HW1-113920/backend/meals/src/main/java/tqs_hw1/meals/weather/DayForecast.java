package tqs_hw1.meals.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DayForecast {
    private String datetime;
    private Double temp;
    private Double tempmax;
    private Double tempmin;
    private String conditions;
    private String icon;
    private Double precip;
    private Double humidity;
    private Double windspeed;
} 