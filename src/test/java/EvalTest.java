import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static java.util.Objects.nonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EvalTest {

    @DisplayName("Should evaluate expression")
    @ParameterizedTest(name = "[{index}] Should evaluate `{0}` to {1}")
    @CsvSource(delimiter = '|', value = {
        "' 3 + 4'|7",
        "' 5 + 2 * 6'|17",
        "' 10 * 7 + 5'|75",
        "' 111 * ( 2 + 3 )'|555",
        "' 112 * ( 2 + 3 )'|560",
        "' 222 * ( 2 + 5 ) / 14'|111",
        "' 222 * ( 12 + ( 1 - 3 ) * 2 ) / 8'|222",
        "'4+2*(5-2)'|10",
        "1(2+3|0",
    })
    void testEval(String expression, int expected) {
        assertEquals(expected, Eval.evaluate(expression), "Eval");
        //Отдельному классу - отдельный тест.
        //Но правильно было бы не создавать второй класс, а вносить правки в существующий - увидеть разницу нам поможет git.
        assertEquals(expected, EvalOptimisedAndRefactored.evaluate(expression), "EvalOptimisedAndRefactored");
    }

    @DisplayName("Should fail on evaluate expression: Eval")
    @ParameterizedTest(name = "[{index}] Expression `{0}` should fail: {1}({2})")
    @CsvSource(delimiter = '|', value = {
        "' 3 + '|java.util.EmptyStackException|",
        "' 3 + a'|java.util.EmptyStackException|",
        "' 3 ) 4'|java.util.EmptyStackException|",
        "' 2 + 3 ) 4'|java.util.EmptyStackException|",
        "'3++'|java.util.EmptyStackException|",
        "'3 / 0'|java.util.EmptyStackException|",
        "'1 2 3 + 4 )'|java.util.EmptyStackException|",
    })
    void testFailure_Eval(String expression, Class<? extends Throwable> exceptionType, String message) {
        Throwable throwable = assertThrows(exceptionType, () -> Eval.evaluate(expression));
        if (nonNull(message)) {
            assertEquals(message, throwable.getMessage());
        } else {
            assertNull(throwable.getMessage());
        }
    }

    @DisplayName("Should fail on evaluate expression: EvalOptimisedAndRefactored")
    @ParameterizedTest(name = "[{index}] Expression `{0}` should fail: {1}({2})")
    @CsvSource(delimiter = '|', value = {
        "' 3 + '|java.util.NoSuchElementException|",
        "' 3 + a'|java.util.NoSuchElementException|",
        "' 3 ) 4'|java.util.NoSuchElementException|",
        "' 2 + 3 ) 4'|java.util.NoSuchElementException|",
        "'3++'|java.util.NoSuchElementException|",
        "'3 / 0'|java.lang.UnsupportedOperationException|Cannot divide by zero",
        "'1 2 3 + 4 )'|java.util.NoSuchElementException|",
    })
    void testFailure_EvalOptimisedAndRefactored(String expression, Class<? extends Throwable> exceptionType, String message) {
        Throwable throwable = assertThrows(exceptionType, () -> EvalOptimisedAndRefactored.evaluate(expression));
        if (nonNull(message)) {
            assertEquals(message, throwable.getMessage());
        } else {
            assertNull(throwable.getMessage());
        }
    }

}
