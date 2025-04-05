package tqs_hw1.meals.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WeatherCacheService {

    private final RedisTemplate<String, WeatherData> redisTemplate;
    
    @Value("${weather.cache.ttl:86400}")
    private long cacheTtl; // Default TTL: 24 hours in seconds
    
    @Autowired
    public WeatherCacheService(RedisTemplate<String, WeatherData> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    /**
     * Caches weather data for a specific location and date
     *
     * @param weatherData The weather data to cache
     */
    public void cacheWeather(WeatherData weatherData) {
        String key = createCacheKey(weatherData.getLocation(), weatherData.getDate());
        log.debug("Caching weather data for key: {}", key);
        redisTemplate.opsForValue().set(key, weatherData);
        redisTemplate.expire(key, cacheTtl, TimeUnit.SECONDS);
    }
    
    /**
     * Retrieves weather data from cache for a specific location and date
     *
     * @param location The location for which to retrieve weather data
     * @param date The date for which to retrieve weather data
     * @return WeatherData if found in cache, null otherwise
     */
    public WeatherData getWeather(String location, LocalDate date) {
        String key = createCacheKey(location, date);
        log.debug("Getting weather data from cache with key: {}", key);
        return redisTemplate.opsForValue().get(key);
    }
    
    /**
     * Clears all cache entries
     */
    public void clearCache() {
        log.info("Clearing all weather cache entries");
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            connection.serverCommands().flushAll();
            return null;
        });
    }
    
    /**
     * Creates a cache key from location and date
     *
     * @param location The location
     * @param date The date
     * @return The cache key
     */
    private String createCacheKey(String location, LocalDate date) {
        return "weather:" + location + ":" + date.toString();
    }
    
    /**
     * Sets the cache TTL (for testing purposes)
     *
     * @param cacheTtl The cache TTL in seconds
     */
    public void setCacheTtl(long cacheTtl) {
        this.cacheTtl = cacheTtl;
    }
} 