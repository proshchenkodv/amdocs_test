import java.util.ArrayDeque;
import java.util.Deque;

public class EvalOptimisedAndRefactored {

    public static void main(String[] args) {
        test(" 3 + 4", 7);
        test(" 5 + 2 * 6", 17);
        test(" 10 * 7 + 5", 75);
        test(" 111 * ( 2 + 3 )", 555);
        test(" 112 * ( 2 + 3 )", 560);
        test(" 222 * ( 2 + 5 ) / 14", 111);
        test(" 222 * ( 12 + ( 1 - 3 ) * 2 ) / 8", 222);
        test("4+2*(5-2)", 10);
    }

    private static void test(String str, int expect) {
        int result = evaluate(str);
        if (result == expect)
            System.out.println("CORRECT!");
        else
            System.out.println(str + " should be evaluated to " + expect + ", but was " + result);
    }

    public static int evaluate(String expression) {
        expression.replace(" ", "");
        char[] tokens = expression.toCharArray();
        Deque<Integer> values = new ArrayDeque<>();
        Deque<Character> operators = new ArrayDeque<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ') continue;

            if (Character.isDigit(tokens[i])) {
                StringBuilder sbuf = new StringBuilder();
                while (i < tokens.length && Character.isDigit(tokens[i])) {
                    sbuf.append(tokens[i++]);
                }
                i--;
                values.push(Integer.parseInt(sbuf.toString()));
            } else if (tokens[i] == '(') {
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop();
            } else if (isOperator(tokens[i])) {
                while (!operators.isEmpty() && shouldBeApplyBefore(tokens[i], operators.peek())) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(tokens[i]);
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    public static boolean shouldBeApplyBefore(char operator1, char operator2) {
        switch (operator2) {
            case '(':
                return false;
            case '+':
            case '-':
                return operator1 != '*' && operator1 != '/';
            case '*':
            case '/':
                return operator1 != '*';
            default:
                return false;
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
                if (operand2 == 0) throw new UnsupportedOperationException("Cannot divide by zero");
                return operand1 / operand2;
            default:
                return 0;
        }
    }

    public static boolean isOperator(char token) {
        return token == '+' || token == '-' || token == '*' || token == '/';
    }

}
