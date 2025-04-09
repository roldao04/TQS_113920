package tqs_hw1.meals.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WebClient.Builder webClientBuilder;
    
    @Mock
    private WebClient webClient;
    
    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    
    @Mock
    @SuppressWarnings("rawtypes")
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    
    @Mock
    private WebClient.ResponseSpec responseSpec;
    
    @Mock
    private WeatherCacheService weatherCacheService;
    
    @InjectMocks
    private WeatherService weatherService;
    
    private static final String WEATHER_API_KEY = "test-api-key";
    private static final String WEATHER_API_BASE_URL = "https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline";
    
    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        // Use lenient() to avoid UnnecessaryStubbingException when these mocks aren't used
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
        lenient().when(webClient.get()).thenReturn(requestHeadersUriSpec);
        lenient().when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        lenient().when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        
        weatherService.setApiKey(WEATHER_API_KEY);
        weatherService.setBaseUrl(WEATHER_API_BASE_URL);
    }
    
    @Test
    void testGetWeatherForecastFromCache() {
        LocalDate date = LocalDate.now();
        String location = "Lisbon,PT";
        WeatherData mockedWeatherData = new WeatherData();
        mockedWeatherData.setLocation(location);
        mockedWeatherData.setDate(date);
        mockedWeatherData.setTemperature(25.0);
        mockedWeatherData.setCondition("Sunny");
        
        // Mock the cache to return data
        when(weatherCacheService.getWeather(location, date)).thenReturn(mockedWeatherData);
        
        // Call the service
        WeatherData result = weatherService.getWeatherForecast(location, date);
        
        // Verify the result and cache interaction
        assertNotNull(result);
        assertEquals(location, result.getLocation());
        assertEquals(date, result.getDate());
        assertEquals(25.0, result.getTemperature());
        assertEquals("Sunny", result.getCondition());
        
        verify(weatherCacheService, times(1)).getWeather(location, date);
        verify(webClient, never()).get();
    }
    
    @Test
    void testGetWeatherForecastFromApi() {
        LocalDate date = LocalDate.now();
        String location = "Lisbon,PT";
        
        // Mock the cache to return null (cache miss)
        when(weatherCacheService.getWeather(location, date)).thenReturn(null);
        
        // Mock API response
        WeatherResponse mockResponse = new WeatherResponse();
        DayForecast forecast = new DayForecast();
        forecast.setDatetime(date.toString());
        forecast.setTemp(25.0);
        forecast.setConditions("Sunny");
        mockResponse.getDays().add(forecast);
        
        when(responseSpec.bodyToMono(WeatherResponse.class)).thenReturn(Mono.just(mockResponse));
        
        // Call the service
        WeatherData result = weatherService.getWeatherForecast(location, date);
        
        // Verify the result, cache and API interactions
        assertNotNull(result);
        assertEquals(location, result.getLocation());
        assertEquals(date, result.getDate());
        assertEquals(25.0, result.getTemperature());
        assertEquals("Sunny", result.getCondition());
        
        verify(weatherCacheService, times(1)).getWeather(location, date);
        verify(weatherCacheService, times(1)).cacheWeather(any(WeatherData.class));
        verify(webClient, times(1)).get();
    }
    
    @Test
    void testGetWeatherForecastApiError() {
        LocalDate date = LocalDate.now();
        String location = "Lisbon,PT";
        
        // Mock the cache to return null (cache miss)
        when(weatherCacheService.getWeather(location, date)).thenReturn(null);
        
        // Mock API to throw exception
        when(responseSpec.bodyToMono(WeatherResponse.class)).thenReturn(Mono.error(new RuntimeException("API Error")));
        
        // Call the service and expect exception
        Exception exception = assertThrows(RuntimeException.class, () -> {
            weatherService.getWeatherForecast(location, date);
        });
        
        // Verify the exception message
        assertTrue(exception.getMessage().contains("API Error"));
        
        // Verify interactions
        verify(weatherCacheService, times(1)).getWeather(location, date);
        verify(weatherCacheService, never()).cacheWeather(any(WeatherData.class));
        verify(webClient, times(1)).get();
    }
} 