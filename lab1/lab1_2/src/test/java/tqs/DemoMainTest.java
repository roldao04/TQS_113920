package tqs;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class DemoMainTest {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void testMainExecution() {
        DemoMain.main(new String[]{});

        String consoleOutput = outputStreamCaptor.toString();
        assertTrue(consoleOutput.contains("Betting with three random bets"));
        assertTrue(consoleOutput.contains("Draw results:"));
        assertTrue(consoleOutput.contains("You scored (matches):"));
    }
}
