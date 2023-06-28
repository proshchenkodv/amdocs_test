import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Predicate;

public class EvalOptimisedAndRefactored {

    public static int evaluate(String expression) {
        char[] tokens = expression.toCharArray();
        Deque<Integer> values = new ArrayDeque<>();
        Deque<Character> operators = new ArrayDeque<>();

        for (int i = 0; i < tokens.length; i++) {
            if (Character.isDigit(tokens[i])) {
                int start = i;
                do {
                    i++;
                } while (i < tokens.length && Character.isDigit(tokens[i]));
                values.push(Integer.valueOf(String.valueOf(tokens, start, i-- - start)));
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

    public static boolean shouldBeApplyBefore(char operator2) {
        return operator2 == '*' || operator2 == '/';
    }

    private static void applyOperators(Deque<Character> ops, Deque<Integer> values, Predicate<Character> opTest) {
        while (!ops.isEmpty() && opTest.test(ops.peek())) {
            values.push(applyOperator(ops.pop(), values.pop(), values.pop()));
        }
    }

    public static int applyOperator(char operator, int operand2, int operand1) {
        switch (operator) {
            case '-':
                return operand1 - operand2;
            case '+':
                return operand1 + operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return operand1 / operand2;
            default:
                throw new UnsupportedOperationException("Invalid operator: " + operator);
        }
    }

    public static boolean isOperator(char token) {
        return token == '+' || token == '-' || token == '*' || token == '/';
    }

}
