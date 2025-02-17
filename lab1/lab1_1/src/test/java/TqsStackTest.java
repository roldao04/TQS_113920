import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tqs.TqsStack;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TqsStackTest {

    private TqsStack stack;
    
    @BeforeEach
    public void setUp() {
        stack = new TqsStack();  // Default to unbounded stack
    }

    @AfterEach
    public void tearDown() {
        stack = null;
    }

    // Test: A stack is empty on construction
    @Test
    public void testIsEmpty() {
        assertTrue(stack.isEmpty());
    }

    // Test: A stack has size 0 on construction
    @Test
    public void testSizeOnConstruction() {
        assertEquals(0, stack.size());
    }

    // Test: Pushing elements increases size
    @Test
    public void testPushIncreasesSize() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.size());
        assertFalse(stack.isEmpty());
    }

    // Test: Pushing and popping maintains LIFO order
    @Test
    public void testPushPop() {
        stack.push(10);
        stack.push(20);
        assertEquals(20, stack.pop());  // LIFO order
        assertEquals(10, stack.pop());
        assertTrue(stack.isEmpty());   // Stack should be empty after all pops
    }

    // Test: Pushing and peeking does not change the size
    @Test
    public void testPushPeek() {
        stack.push("A");
        stack.push("B");
        assertEquals("B", stack.peek()); // Peek returns last pushed element
        assertEquals(2, stack.size());   // Size remains unchanged
    }

    // Test: Pop removes all elements, making stack empty
    @Test
    public void testPopUntilEmpty() {
        stack.push(100);
        stack.push(200);
        stack.push(300);
        stack.pop();
        stack.pop();
        stack.pop();
        assertTrue(stack.isEmpty());
        assertEquals(0, stack.size());
    }

    // Test: Popping from an empty stack throws NoSuchElementException
    @Test
    public void testPopOnEmptyStack() {
        assertThrows(NoSuchElementException.class, () -> stack.pop());
    }

    // Test: Peeking into an empty stack throws NoSuchElementException
    @Test
    public void testPeekOnEmptyStack() {
        assertThrows(NoSuchElementException.class, () -> stack.peek());
    }

    // **Tests for bounded stack (capacity-restricted)**
    
    // Test: Pushing onto a full stack throws IllegalStateException
    @Test
    public void testPushOnFullBoundedStack() {
        TqsStack boundedStack = new TqsStack(2); // Capacity = 2
        boundedStack.push(1);
        boundedStack.push(2);
        assertThrows(IllegalStateException.class, () -> boundedStack.push(3));
    }

    // Test: Pushing up to the limit should work without error
    @Test
    public void testPushUpToCapacity() {
        TqsStack boundedStack = new TqsStack(2);
        boundedStack.push("X");
        boundedStack.push("Y");
        assertEquals(2, boundedStack.size()); // Should reach max size
    }

    // Test: Popping from a full stack works correctly
    @Test
    public void testPopFromFullStack() {
        TqsStack boundedStack = new TqsStack(2);
        boundedStack.push(5);
        boundedStack.push(10);
        assertEquals(10, boundedStack.pop()); // LIFO order
        assertEquals(5, boundedStack.pop());
        assertTrue(boundedStack.isEmpty());
    }

    // Test for popTopN method
    @Test
    public void testPopTopN() {
        TqsStack stack = new TqsStack();
        stack.push(10);
        stack.push(20);
        stack.push(30);
        stack.push(40);

        assertEquals(30, stack.popTopN(2)); // Removes 40, then returns 30
        assertEquals(2, stack.size());      // Stack should now contain [10, 20]
    }

    // Test for popTopN with invalid input (negative or zero)
    @Test
    public void testPopTopNInvalid() {
        TqsStack stack = new TqsStack();
        stack.push(1);
        assertThrows(IllegalArgumentException.class, () -> stack.popTopN(0));
        assertThrows(IllegalArgumentException.class, () -> stack.popTopN(-1));
        assertThrows(IllegalArgumentException.class, () -> stack.popTopN(2)); // Stack only has 1 element
    }

    // Test for invalid capacity (<= 0)
    @Test
    public void testInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new TqsStack(0));
        assertThrows(IllegalArgumentException.class, () -> new TqsStack(-1));
    }
}
