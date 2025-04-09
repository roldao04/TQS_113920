package tqs_hw1.meals.weather;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class WeatherDataTest {

    @Test
    void testWeatherDataCreationAndGetters() {
        // Arrange
        String location = "Aveiro";
        LocalDate date = LocalDate.now();
        Double temperature = 20.5;
        String condition = "Sunny";
        Double maxTemp = 25.0;
        Double minTemp = 15.0;
        String icon = "clear-day";
        Double precipitation = 0.0;
        Double humidity = 65.0;
        Double windSpeed = 10.5;
        Long timestamp = System.currentTimeMillis() / 1000;
        
        // Act
        WeatherData weatherData = new WeatherData(
            location, date, temperature, condition, maxTemp, minTemp,
            icon, precipitation, humidity, windSpeed, timestamp
        );
        
        // Assert
        assertEquals(location, weatherData.getLocation());
        assertEquals(date, weatherData.getDate());
        assertEquals(temperature, weatherData.getTemperature());
        assertEquals(condition, weatherData.getCondition());
        assertEquals(maxTemp, weatherData.getMaxTemperature());
        assertEquals(minTemp, weatherData.getMinTemperature());
        assertEquals(icon, weatherData.getIcon());
        assertEquals(precipitation, weatherData.getPrecipitation());
        assertEquals(humidity, weatherData.getHumidity());
        assertEquals(windSpeed, weatherData.getWindSpeed());
        assertEquals(timestamp, weatherData.getTimestamp());
    }
    
    @Test
    void testWeatherDataSetters() {
        // Arrange
        WeatherData weatherData = new WeatherData();
        
        // Act
        weatherData.setLocation("Aveiro");
        weatherData.setDate(LocalDate.now());
        weatherData.setTemperature(20.5);
        weatherData.setCondition("Sunny");
        weatherData.setMaxTemperature(25.0);
        weatherData.setMinTemperature(15.0);
        weatherData.setIcon("clear-day");
        weatherData.setPrecipitation(0.0);
        weatherData.setHumidity(65.0);
        weatherData.setWindSpeed(10.5);
        weatherData.setTimestamp(1617567600L);
        
        // Assert
        assertEquals("Aveiro", weatherData.getLocation());
        assertEquals(LocalDate.now(), weatherData.getDate());
        assertEquals(20.5, weatherData.getTemperature());
        assertEquals("Sunny", weatherData.getCondition());
        assertEquals(25.0, weatherData.getMaxTemperature());
        assertEquals(15.0, weatherData.getMinTemperature());
        assertEquals("clear-day", weatherData.getIcon());
        assertEquals(0.0, weatherData.getPrecipitation());
        assertEquals(65.0, weatherData.getHumidity());
        assertEquals(10.5, weatherData.getWindSpeed());
        assertEquals(1617567600L, weatherData.getTimestamp());
    }
    
    @Test
    void testEqualsAndHashCode() {
        // Arrange
        WeatherData weatherData1 = new WeatherData(
            "Aveiro", LocalDate.now(), 20.5, "Sunny", 25.0, 15.0,
            "clear-day", 0.0, 65.0, 10.5, 1617567600L
        );
        
        WeatherData weatherData2 = new WeatherData(
            "Aveiro", LocalDate.now(), 20.5, "Sunny", 25.0, 15.0,
            "clear-day", 0.0, 65.0, 10.5, 1617567600L
        );
        
        WeatherData weatherData3 = new WeatherData(
            "Porto", LocalDate.now(), 18.0, "Cloudy", 22.0, 14.0,
            "cloudy", 0.2, 70.0, 12.0, 1617567700L
        );
        
        // Assert
        assertEquals(weatherData1, weatherData2);
        assertEquals(weatherData1.hashCode(), weatherData2.hashCode());
        assertNotEquals(weatherData1, weatherData3);
        assertNotEquals(weatherData1.hashCode(), weatherData3.hashCode());
    }
}