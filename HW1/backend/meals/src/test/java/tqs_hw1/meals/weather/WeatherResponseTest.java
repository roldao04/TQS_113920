package tqs_hw1.meals.weather;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WeatherResponseTest {

    @Test
    void testWeatherResponseCreationAndGetters() {
        // Arrange
        WeatherResponse response = new WeatherResponse();
        response.setAddress("Aveiro, Portugal");
        response.setTimezone("Europe/Lisbon");
        
        DayForecast day1 = new DayForecast();
        day1.setDatetime("2023-04-05");
        day1.setTemp(20.5);
        
        DayForecast day2 = new DayForecast();
        day2.setDatetime("2023-04-06");
        day2.setTemp(21.0);
        
        List<DayForecast> days = Arrays.asList(day1, day2);
        response.setDays(days);
        
        // Assert
        assertEquals("Aveiro, Portugal", response.getAddress());
        assertEquals("Europe/Lisbon", response.getTimezone());
        assertEquals(2, response.getDays().size());
        assertEquals("2023-04-05", response.getDays().get(0).getDatetime());
        assertEquals(20.5, response.getDays().get(0).getTemp());
        assertEquals("2023-04-06", response.getDays().get(1).getDatetime());
        assertEquals(21.0, response.getDays().get(1).getTemp());
    }
    
    @Test
    void testEqualsAndHashCode() {
        // Arrange
        WeatherResponse response1 = new WeatherResponse();
        response1.setAddress("Aveiro, Portugal");
        response1.setTimezone("Europe/Lisbon");
        
        WeatherResponse response2 = new WeatherResponse();
        response2.setAddress("Aveiro, Portugal");
        response2.setTimezone("Europe/Lisbon");
        
        WeatherResponse response3 = new WeatherResponse();
        response3.setAddress("Porto, Portugal");
        response3.setTimezone("Europe/Lisbon");
        
        // Assert
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
        assertNotEquals(response1, response3);
        assertNotEquals(response1.hashCode(), response3.hashCode());
    }
}