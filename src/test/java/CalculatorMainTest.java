import com.candidate.vishal.calculator.exception.DivideByZeroException;
import com.candidate.vishal.calculator.exception.InvalidExpressionException;
import com.candidate.vishal.calculator.exception.InvalidOperationException;
import com.candidate.vishal.calculator.exception.ParanthesesMismatchException;
import com.candidate.vishal.calculator.helper.EvaluateExpression;
import com.candidate.vishal.calculator.helper.ValidateExpression;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculatorMainTest {
    private static ValidateExpression validateExpression;
    private static EvaluateExpression evaluateExpression;

    @BeforeClass
    public static void setUp() {
         validateExpression = new ValidateExpression();
         evaluateExpression = new EvaluateExpression();
    }

    // TEST CASES FOR ALL INVALID OPERATIONS
    @Test(expected = InvalidExpressionException.class)
    public void testEmptyExpression() throws ParanthesesMismatchException, InvalidExpressionException, InvalidOperationException {
        String expr = "";
        validateExpression.checkExpression(expr);
    }

    @Test(expected = InvalidExpressionException.class)
    public void testNullExpression() throws ParanthesesMismatchException, InvalidExpressionException, InvalidOperationException {
        String expr = null;
        validateExpression.checkExpression(expr);
    }

    @Test(expected = InvalidExpressionException.class)
    public void testInvalidExpression() throws ParanthesesMismatchException, InvalidExpressionException, InvalidOperationException {
        String expr = "add)5,5(";
        validateExpression.checkExpression(expr);
    }

    @Test(expected = ParanthesesMismatchException.class)
    public void testInvalidParanthesesExpression() throws ParanthesesMismatchException, InvalidExpressionException, InvalidOperationException {
        String expr = "add(5,5";
        validateExpression.checkExpression(expr);
    }

    @Test(expected = InvalidOperationException.class)
    public void testInvalidOperationExpression() throws ParanthesesMismatchException, InvalidExpressionException, InvalidOperationException {
        String expr = "haha(1,2";
        validateExpression.checkExpression(expr);
    }

    // DIVIDE BY ZERO TEST CASE
    @Test(expected = DivideByZeroException.class)
    public void testDivideByZeroExpression() throws DivideByZeroException, InvalidOperationException, InvalidExpressionException, ParanthesesMismatchException {
        String expr = "DIV(9, 0)";
        validateExpression.checkExpression(expr);
        evaluateExpression.evaluate(expr);
    }

    // TEST ALL VALID CASES
    @Test
    public void testSimpleAdd() throws ParanthesesMismatchException, InvalidExpressionException, InvalidOperationException, DivideByZeroException {
        String expr = "add(1, 2)";
        long expectedResult = 3; // 1 + 2
        validateExpression.checkExpression(expr);
        long actualResult = evaluateExpression.evaluate(expr);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAddAndMult() throws ParanthesesMismatchException, InvalidExpressionException, InvalidOperationException, DivideByZeroException {
        String expr = "ADD(1, mult(2, 3))";
        long expectedResult = 7; // 1 + 2 * 3
        validateExpression.checkExpression(expr);
        long actualResult = evaluateExpression.evaluate(expr);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testMultAddAndDiv() throws ParanthesesMismatchException, InvalidExpressionException, InvalidOperationException, DivideByZeroException {
        String expr = "mult(add(2, 2), div(9, 3))";
        long expectedResult = 12; // (2 + 2) * (9 / 3)
        validateExpression.checkExpression(expr);
        long actualResult = evaluateExpression.evaluate(expr);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLetAndAdd() throws ParanthesesMismatchException, InvalidExpressionException, InvalidOperationException, DivideByZeroException {
        String expr = "let(a, 5, add(a, a))";
        int expectedResult = 10; // a = 5, a + a
        validateExpression.checkExpression(expr);
        long actualResult = evaluateExpression.evaluate(expr);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testComplexLet1() throws ParanthesesMismatchException, InvalidExpressionException, InvalidOperationException, DivideByZeroException {
        String expr = "let(a, 5, let(b, mult(a, 10), add(b, a)))";
        long expectedResult = 55; // a = 5, b = 5 * 10, a + b
        validateExpression.checkExpression(expr);
        long actualResult = evaluateExpression.evaluate(expr);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testComplexLet2() throws ParanthesesMismatchException, InvalidExpressionException, InvalidOperationException, DivideByZeroException {
        String expr = "let(a, let(b, 10, add(b, b)), let(b, 20, add(a, b)))";
        long expectedResult = 40; // a = 20 (b = 10, b + b), b = 20, a + b
        validateExpression.checkExpression(expr);
        long actualResult = evaluateExpression.evaluate(expr);
        assertEquals(expectedResult, actualResult);
    }
}
