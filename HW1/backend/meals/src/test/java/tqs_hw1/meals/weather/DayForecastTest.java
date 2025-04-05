package tqs_hw1.meals.weather;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DayForecastTest {

    @Test
    void testDayForecastCreationAndGetters() {
        // Arrange & Act
        DayForecast forecast = new DayForecast();
        forecast.setDatetime("2023-04-05");
        forecast.setTemp(20.5);
        forecast.setTempmax(25.0);
        forecast.setTempmin(15.0);
        forecast.setConditions("Sunny");
        forecast.setIcon("clear-day");
        forecast.setPrecip(0.0);
        forecast.setHumidity(65.0);
        forecast.setWindspeed(10.5);
        
        // Assert
        assertEquals("2023-04-05", forecast.getDatetime());
        assertEquals(20.5, forecast.getTemp());
        assertEquals(25.0, forecast.getTempmax());
        assertEquals(15.0, forecast.getTempmin());
        assertEquals("Sunny", forecast.getConditions());
        assertEquals("clear-day", forecast.getIcon());
        assertEquals(0.0, forecast.getPrecip());
        assertEquals(65.0, forecast.getHumidity());
        assertEquals(10.5, forecast.getWindspeed());
    }
    
    @Test
    void testEqualsAndHashCode() {
        // Arrange
        DayForecast forecast1 = new DayForecast();
        forecast1.setDatetime("2023-04-05");
        forecast1.setTemp(20.5);
        forecast1.setConditions("Sunny");
        
        DayForecast forecast2 = new DayForecast();
        forecast2.setDatetime("2023-04-05");
        forecast2.setTemp(20.5);
        forecast2.setConditions("Sunny");
        
        DayForecast forecast3 = new DayForecast();
        forecast3.setDatetime("2023-04-06");
        forecast3.setTemp(18.0);
        forecast3.setConditions("Cloudy");
        
        // Assert
        assertEquals(forecast1, forecast2);
        assertEquals(forecast1.hashCode(), forecast2.hashCode());
        assertNotEquals(forecast1, forecast3);
        assertNotEquals(forecast1.hashCode(), forecast3.hashCode());
    }
}