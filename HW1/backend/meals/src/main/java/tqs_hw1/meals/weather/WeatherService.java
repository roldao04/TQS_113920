package tqs_hw1.meals.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
public class WeatherService {

    private final WebClient.Builder webClientBuilder;
    private final WeatherCacheService weatherCacheService;
    
    @Value("${weather.api.key}")
    private String apiKey;
    
    @Value("${weather.api.base-url}")
    private String baseUrl;
    
    @Autowired
    public WeatherService(WebClient.Builder webClientBuilder, WeatherCacheService weatherCacheService) {
        this.webClientBuilder = webClientBuilder;
        this.weatherCacheService = weatherCacheService;
    }
    
    /**
     * Gets the weather forecast for a specific location and date
     * First tries to retrieve from cache, if not available fetches from API
     *
     * @param location The location for which to get the forecast
     * @param date The date for which to get the forecast
     * @return WeatherData containing the forecast
     */
    public WeatherData getWeatherForecast(String location, LocalDate date) {
        log.info("Getting weather forecast for location: {} and date: {}", location, date);
        
        // Try to get from cache first
        WeatherData cachedData = weatherCacheService.getWeather(location, date);
        if (cachedData != null) {
            log.debug("Cache hit for location: {} and date: {}", location, date);
            return cachedData;
        }
        
        log.debug("Cache miss for location: {} and date: {}, fetching from API", location, date);
        
        // If not in cache, fetch from API
        String url = String.format("%s/%s/%s?key=%s&unitGroup=metric&include=days", 
                baseUrl, location, date.toString(), apiKey);
        
        try {
            WeatherResponse response = webClientBuilder.build()
                    .get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(WeatherResponse.class)
                    .block();
            
            if (response != null && !response.getDays().isEmpty()) {
                DayForecast dayForecast = response.getDays().get(0);
                
                WeatherData weatherData = new WeatherData();
                weatherData.setLocation(location);
                weatherData.setDate(date);
                weatherData.setTemperature(dayForecast.getTemp());
                weatherData.setMaxTemperature(dayForecast.getTempmax());
                weatherData.setMinTemperature(dayForecast.getTempmin());
                weatherData.setCondition(dayForecast.getConditions());
                weatherData.setIcon(dayForecast.getIcon());
                weatherData.setPrecipitation(dayForecast.getPrecip());
                weatherData.setHumidity(dayForecast.getHumidity());
                weatherData.setWindSpeed(dayForecast.getWindspeed());
                weatherData.setTimestamp(LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC));
                
                // Cache the result
                weatherCacheService.cacheWeather(weatherData);
                
                return weatherData;
            } else {
                throw new RuntimeException("No weather data found for " + location + " on " + date);
            }
        } catch (Exception e) {
            log.error("Error fetching weather data: {}", e.getMessage(), e);
            throw new RuntimeException("Error fetching weather data: " + e.getMessage(), e);
        }
    }
    
    // Setters for testing purposes
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
} 