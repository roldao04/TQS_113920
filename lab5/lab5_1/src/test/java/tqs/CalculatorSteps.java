package tqs;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CalculatorSteps {

    static final Logger log = getLogger(lookup().lookupClass());

    private Calculator calc;

    @Given("a calculator I just turned on")
    public void setup() {
        calc = new Calculator();
    }

    @When("I add {int} and {int}")
    public void add(int arg1, int arg2) {
        log.debug("Adding {} and {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("+");
    }

    @When("I substract {int} to {int}")
    public void substract(int arg1, int arg2) {
        log.debug("Substracting {} to {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("-");
    }

    @When("I multiply {int} and {int}")
    public void multiply(int arg1, int arg2) {
        log.debug("Multiplying {} and {}", arg1, arg2);
        calc.push(arg1);
        calc.push(arg2);
        calc.push("*");
    }

    @When("I perform an invalid operation {string}")
    public void invalidOperation(String operation) {
        log.debug("Performing invalid operation: {}", operation);
        try {
            calc.push(operation);  // Pass invalid operator
        } catch (UnsupportedOperationException | IllegalStateException | IllegalArgumentException e) {
            log.debug("Caught exception: {}", e.getMessage());
        }
    }

    @Then("the result is {int}")
    public void the_result_is(int expected) {
        Integer value = calc.value();
        log.debug("Result: {} (expected {})", value, expected);
        assertEquals(expected, value);
    }

    @Then("an error occurs")
    public void errorOccurs() {
        try {
            calc.value();  // Trigger evaluation
        } catch (UnsupportedOperationException e) {
            log.debug("Error message: {}", e.getMessage());
            assertEquals("Unsupported operator", e.getMessage());  // Check for expected exception message
        } catch (IllegalStateException e) {
            log.debug("Error message: {}", e.getMessage());
            assertEquals("Stack is empty, no result to return", e.getMessage());  // Check for stack size error
        }
    }
}
