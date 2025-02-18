import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    @Test
    public void testAddition_PositiveNumbers() {
        double result = Calculator.add(10, 5);
        assertEquals(15, result, "10 + 5 should equal 15");
    }

    @Test
    public void testAddition_NegativeNumbers() {
        double result = Calculator.add(-10, -5);
        assertEquals(-15, result, "-10 + (-5) should equal -15");
    }

    @Test
    public void testAddition_MixedNumbers() {
        double result = Calculator.add(-10, 5);
        assertEquals(-5, result, "-10 + 5 should equal -5");
    }
    
    @Test
    public void testSubtraction_PositiveNumbers() {
        double result = Calculator.subtract(10, 5);
        assertEquals(5, result, "10 - 5 should equal 5");
    }

    @Test
    public void testSubtraction_NegativeNumbers() {
        double result = Calculator.subtract(-10, -5);
        assertEquals(-5, result, "-10 - (-5) should equal -5");
    }
    
    @Test
    public void testMultiplication() {
        double result = Calculator.multiply(10, 5);
        assertEquals(50, result, "10 * 5 should equal 50");
    }

    @Test
    public void testMultiplication_WithZero() {
        double result = Calculator.multiply(10, 0);
        assertEquals(0, result, "10 * 0 should equal 0");
    }

    @Test
    public void testDivision() {
        double result = Calculator.divide(10, 2);
        assertEquals(5, result, "10 / 2 should equal 5");
    }

    @Test
    public void testDivision_NegativeResult() {
        double result = Calculator.divide(-10, 2);
        assertEquals(-5, result, "-10 / 2 should equal -5");
    }

    @Test
    public void testDivision_Decimals() {
        double result = Calculator.divide(7, 2);
        assertEquals(3.5, result, "7 / 2 should equal 3.5");
    }

    @Test
    public void testDivision_DivisionByZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            Calculator.divide(10, 0);
        });
        String expectedMessage = "Division by zero is not allowed.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    // New tests added to cover more cases

    @Test
    public void testAddition_WithZero() {
        double result1 = Calculator.add(0, 5);
        double result2 = Calculator.add(5, 0);
        assertEquals(5, result1, "0 + 5 should equal 5");
        assertEquals(5, result2, "5 + 0 should equal 5");
    }
    
    @Test
    public void testSubtraction_WithZero() {
        double result1 = Calculator.subtract(0, 5);
        double result2 = Calculator.subtract(5, 0);
        assertEquals(-5, result1, "0 - 5 should equal -5");
        assertEquals(5, result2, "5 - 0 should equal 5");
    }
    
    @Test
    public void testMultiplication_NegativeAndPositive() {
        double result = Calculator.multiply(-10, 5);
        assertEquals(-50, result, "-10 * 5 should equal -50");
    }
    
    @Test
    public void testMultiplication_BothNegative() {
        double result = Calculator.multiply(-10, -5);
        assertEquals(50, result, "-10 * -5 should equal 50");
    }
    
    @Test
    public void testDivision_WithNegativeDivisor() {
        double result = Calculator.divide(10, -2);
        assertEquals(-5, result, "10 / -2 should equal -5");
    }
    
    @Test
    public void testAddition_BoundaryValues() {
        // Using some extreme values
        double a = Double.MAX_VALUE / 2;
        double b = Double.MAX_VALUE / 2;
        double expected = Double.MAX_VALUE;
        double result = Calculator.add(a, b);
        assertEquals(expected, result, "Summing two halves of Double.MAX_VALUE should be Double.MAX_VALUE");
    }
    
    @Test
    public void testSubtraction_BoundaryValues() {
        // Subtracting equal numbers should yield zero even with extreme values
        double a = Double.MAX_VALUE;
        double result = Calculator.subtract(a, a);
        assertEquals(0, result, "Subtracting a number from itself should equal 0");
    }
    
    @Test
    public void testDivision_FractionalResultHighPrecision() {
        double result = Calculator.divide(1, 3);
        // expected roughly 0.33333
        assertEquals(0.33333, result, 0.00001, "1 / 3 should be approximately 0.33333");
    }
}