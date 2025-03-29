package tqs;

import java.util.Stack;

public class Calculator {

    private Stack<Integer> stack;

    public Calculator() {
        stack = new Stack<>();
    }

    public void push(Object value) {
        if (value instanceof String) {
            // If the value is an operator (+, -, *, etc.), perform the operation
            String operator = (String) value;
            if (stack.size() < 2) {
                throw new IllegalStateException("Not enough operands in stack for operation");
            }
            int b = stack.pop();
            int a = stack.pop();
            switch (operator) {
                case "+":
                    stack.push(a + b);
                    break;
                case "-":
                    stack.push(a - b);
                    break;
                case "*":
                    stack.push(a * b);
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported operator");
            }
        } else if (value instanceof Integer) {
            // If it's a number, push it to the stack
            stack.push((Integer) value);
        } else {
            throw new IllegalArgumentException("Invalid input type: " + value.getClass().getName());
        }
    }

    public Integer value() {
        if (stack.isEmpty()) {
            throw new IllegalStateException("Stack is empty, no result to return");
        }
        return stack.peek();
    }
}
