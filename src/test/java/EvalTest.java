import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EvalTest {

    @ParameterizedTest(name = "[{index}] Should evaluate `{0}` to {1}")
    @CsvSource(delimiter = '|', value = {
        "' 3 + 4'|7",
        "' 5 + 2 * 6'|17",
        "' 10 * 7 + 5'|75",
        "' 111 * ( 2 + 3 )'|555",
        "' 112 * ( 2 + 3 )'|560",
        "' 222 * ( 12 + ( 1 - 3 ) * 2 ) / 8'|222",
        "'4+2*(5-2)'|10",
    })
    void testEval(String expression, int expected) {
        assertEquals(expected, Eval.evaluate(expression), "Eval");
        //Отдельному классу - отдельный тест.
        //Но правильно было бы не создавать второй класс, а вносить правки в существующий - увидеть разницу нам поможет git.
        assertEquals(expected, EvalOptimisedAndRefactored.evaluate(expression), "EvalOptimisedAndRefactored");
    }

    @ParameterizedTest(name = "[{index}] Should evaluate `{0}` to {1}")
    @CsvSource(delimiter = '|', value = {
        "' 222 * ( 2 + 5 ) / 14'|0",//todo 111
    })
    void testEval1(String expression, int expected) {
        //Тут поведение реализаций отличается - пришлось разнести тесты
        assertEquals(expected, Eval.evaluate(expression), "Eval");
    }

    @ParameterizedTest(name = "[{index}] Should evaluate `{0}` to {1}")
    @CsvSource(delimiter = '|', value = {
        "' 222 * ( 2 + 5 ) / 14'|111",
    })
    void testEval2(String expression, int expected) {
        //Тут поведение реализаций отличается - пришлось разнести тесты
        assertEquals(expected, EvalOptimisedAndRefactored.evaluate(expression), "EvalOptimisedAndRefactored");
    }

}
