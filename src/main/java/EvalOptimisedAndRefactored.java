import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

import static java.math.BigDecimal.ZERO;

public class EvalOptimisedAndRefactored {

    public static BigDecimal evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        Deque<BigDecimal> values = new ArrayDeque<>();
        Deque<Character> operators = new ArrayDeque<>();

        for (int i = 0; i < tokens.length; i++) {
            if (Character.isDigit(tokens[i])) {
                i += readValue(tokens, values, i);
            } else if (tokens[i] == '(') {
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                applyOperators(operators, values, operator -> operator != '(');
                operators.pop();
            } else if (isOperator(tokens[i])) {
                applyOperators(operators, values, EvalOptimisedAndRefactored::shouldBeApplyBefore);
                operators.push(tokens[i]);
            }
        }

        applyOperators(operators, values, __ -> true);

        return values.pop();
    }

    private static int readValue(char[] tokens, Deque<BigDecimal> values, int start) {
        int count = 1;
        while (start + count < tokens.length && Character.isDigit(tokens[start + count])) {
            count++;
        }
        values.push(new BigDecimal(String.valueOf(tokens, start, count--)));
        return count;
    }

    public static boolean shouldBeApplyBefore(char operator2) {
        return operator2 == '*' || operator2 == '/';
    }

    private static void applyOperators(Deque<Character> ops, Deque<BigDecimal> values, Predicate<Character> opTest) {
        while (!ops.isEmpty() && opTest.test(ops.peek())) {
            values.push(applyOperator(ops.pop(), values.pop(), values.pop()));
        }
    }

    public static BigDecimal applyOperator(char operator, BigDecimal operand2, BigDecimal operand1) {
        switch (operator) {
            case '-':
                return operand1.subtract(operand2);
            case '+':
                return operand1.add(operand2);
            case '*':
                return operand1.multiply(operand2);
            case '/':
                if (operand2.equals(ZERO)) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return operand1.divide(operand2, MathContext.DECIMAL64);
            default:
                throw new UnsupportedOperationException("Invalid operator: " + operator);
        }
    }

    public static boolean isOperator(char token) {
        return token == '+' || token == '-' || token == '*' || token == '/';
    }

}
