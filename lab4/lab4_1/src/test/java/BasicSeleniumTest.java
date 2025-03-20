import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicSeleniumTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        // Set up WebDriver for Chrome (ensure you have chromedriver installed)
        driver = new ChromeDriver();
    }

    @Test
    void testOpenWebPage() {
        driver.get("https://www.example.com");
        String title = driver.getTitle();
        assertEquals("Example Domain", title);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
