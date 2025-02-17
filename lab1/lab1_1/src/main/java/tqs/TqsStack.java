package tqs;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class TqsStack {
    
    // If capacity < 0, we treat the stack as unbounded
    private int capacity = -1;
    private LinkedList<Object> elements;

    // Unbounded constructor
    public TqsStack() {
        this.elements = new LinkedList<>();
    }

    // Bounded constructor
    public TqsStack(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.elements = new LinkedList<>();
    }

    public boolean isEmpty() {
        return elements.isEmpty();
    }

    public int size() {
        return elements.size();
    }

    public void push(Object o) {
        if (capacity > 0 && size() >= capacity) {
            throw new IllegalStateException("Stack is full");
        }
        elements.push(o);
    }

    public Object pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return elements.pop();
    }

    public Object peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Stack is empty");
        }
        return elements.peek();
    }

    public Object popTopN(int n) {
        if (n <= 0 || n > size()) {
            throw new IllegalArgumentException("Invalid n: must be between 1 and the stack size");
        }

        Object top = null;
        for (int i = 0; i < n; i++) {
            top = elements.pop();
        }
        return top;
    }
}
