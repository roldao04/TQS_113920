import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SeleniumJupiter.class)
class SeleniumJupiterTest {

    @Test
    void testNavigateToSlowCalculator(WebDriver driver) {
        driver.get("https://example.com");

        // Find and click the "Slow calculator" link
        WebElement slowCalculatorLink = driver.findElement(By.linkText("Slow calculator"));
        slowCalculatorLink.click();

        // Assert that we reached the correct URL
        assertTrue(driver.getCurrentUrl().contains("slow-calculator"));
    }
}
