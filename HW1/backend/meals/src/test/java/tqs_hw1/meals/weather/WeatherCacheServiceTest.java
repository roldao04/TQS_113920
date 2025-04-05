package tqs_hw1.meals.weather;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.RedisCallback;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WeatherCacheServiceTest {

    @Mock
    private RedisTemplate<String, WeatherData> redisTemplate;
    
    @Mock
    private ValueOperations<String, WeatherData> valueOperations;
    
    @InjectMocks
    private WeatherCacheService weatherCacheService;
    
    private static final long CACHE_TTL = 3600L; // 1 hour in seconds
    
    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        weatherCacheService.setCacheTtl(CACHE_TTL);
    }
    
    @Test
    void testCacheWeather() {
        // Prepare test data
        String location = "Lisbon,PT";
        LocalDate date = LocalDate.now();
        WeatherData weatherData = new WeatherData();
        weatherData.setLocation(location);
        weatherData.setDate(date);
        weatherData.setTemperature(25.0);
        weatherData.setCondition("Sunny");
        
        // Define the expected cache key
        String expectedKey = "weather:" + location + ":" + date.toString();
        
        // Call the method to test
        weatherCacheService.cacheWeather(weatherData);
        
        // Verify that the value was cached with the expected key and TTL
        verify(valueOperations).set(eq(expectedKey), eq(weatherData));
        verify(redisTemplate).expire(eq(expectedKey), eq(CACHE_TTL), eq(TimeUnit.SECONDS));
    }
    
    @Test
    void testGetWeatherWhenCached() {
        // Prepare test data
        String location = "Lisbon,PT";
        LocalDate date = LocalDate.now();
        WeatherData expectedWeatherData = new WeatherData();
        expectedWeatherData.setLocation(location);
        expectedWeatherData.setDate(date);
        expectedWeatherData.setTemperature(25.0);
        expectedWeatherData.setCondition("Sunny");
        
        // Define the expected cache key
        String expectedKey = "weather:" + location + ":" + date.toString();
        
        // Mock the cache to return data
        when(valueOperations.get(expectedKey)).thenReturn(expectedWeatherData);
        
        // Call the method to test
        WeatherData result = weatherCacheService.getWeather(location, date);
        
        // Verify the result
        assertEquals(expectedWeatherData, result);
        verify(valueOperations).get(expectedKey);
    }
    
    @Test
    void testGetWeatherWhenNotCached() {
        // Prepare test data
        String location = "Lisbon,PT";
        LocalDate date = LocalDate.now();
        
        // Define the expected cache key
        String expectedKey = "weather:" + location + ":" + date.toString();
        
        // Mock the cache to return null (cache miss)
        when(valueOperations.get(expectedKey)).thenReturn(null);
        
        // Call the method to test
        WeatherData result = weatherCacheService.getWeather(location, date);
        
        // Verify the result
        assertNull(result);
        verify(valueOperations).get(expectedKey);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    void testClearCache() {
        // Call the method to test
        weatherCacheService.clearCache();
        
        // Verify that execute method was called instead of getConnectionFactory
        verify(redisTemplate).execute(any(RedisCallback.class));
    }
} 