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
}