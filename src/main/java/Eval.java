import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Stack;
import java.util.function.Predicate;

import static java.math.BigDecimal.ZERO;

public class Eval {

    public static BigDecimal evaluate(String expression) {
        char[] tokens = expression.toCharArray();

        // Stack for numbers: 'values'
        Stack<BigDecimal> values = new Stack<>();
        // Stack for Operators: 'ops'
        Stack<Character> ops = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (isDigit(tokens[i])) {
                // Current token is a number, push it to stack for numbers
                i += readValue(expression, tokens, values, i);
            } else if (tokens[i] == '(') {
                // Current token is an opening brace, push it to 'ops'
                ops.push(tokens[i]);
            } else if (tokens[i] == ')') {
                // Closing brace encountered, solve entire brace
                applyOps(ops, values, op -> op != '(');
                ops.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                // Current token is an operator.
                applyOps(ops, values, Eval::hasPrecedence);
                ops.push(tokens[i]);
            }
        }

        // Entire expression has been parsed at this point, apply remaining ops to remaining values
        applyOps(ops, values, __ -> true);

        return values.pop();
    }

    private static boolean isDigit(char token) {
        return token == '.' || token >= '0' && token <= '9';
    }

    private static int readValue(String expression, char[] tokens, Stack<BigDecimal> values, int start) {
        int end = start;
        do {
            end++;
        } while (end < tokens.length && isDigit(tokens[end]));
        values.push(new BigDecimal(expression.substring(start, end)));
        return end - start - 1;
    }

    // Returns true if 'op2' has higher or same precedence as 'op1', otherwise returns false.
    public static boolean hasPrecedence(char op2) {
        return op2 == '*' || op2 == '/';
    }

    private static void applyOps(Stack<Character> ops, Stack<BigDecimal> values, Predicate<Character> opTest) {
        while (!ops.isEmpty() && opTest.test(ops.peek())) {
            values.push(applyOp(ops.pop(), values.pop(), values.pop()));
        }
    }

    // A utility method to apply an operator 'op' on operands 'a' and 'b'. Return the result.
    public static BigDecimal applyOp(char op, BigDecimal b, BigDecimal a) {
        switch (op) {
            case '-':
                return a.subtract(b);
            case '+':
                return a.add(b);
            case '*':
                return a.multiply(b);
            case '/':
                if (b.equals(ZERO)) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return a.divide(b, MathContext.DECIMAL64);
        }
        throw new UnsupportedOperationException("Invalid operator: " + op);
    }

}
