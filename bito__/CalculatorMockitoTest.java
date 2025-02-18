import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

public class CalculatorMockitoTest {

    @Test
    public void testMockAddition() {
        try (MockedStatic<Calculator> mockedCalculator = mockStatic(Calculator.class)) {
            // Set up a stub: regardless of the inputs provided to add(), return 100.0
            mockedCalculator.when(() -> Calculator.add(anyDouble(), anyDouble()))
                            .thenReturn(100.0);
            
            double result = Calculator.add(50, 25);
            // Verify that our stub returns the mocked value
            assertEquals(100.0, result);

            // Verify that the add() method was called exactly once
            mockedCalculator.verify(() -> Calculator.add(anyDouble(), anyDouble()), times(1));
        }
    }

    @Test
    public void testMockMultiplication() {
        try (MockedStatic<Calculator> mockedCalculator = mockStatic(Calculator.class)) {
            // Stub multiply() method to always return 200.0 regardless of the input values.
            mockedCalculator.when(() -> Calculator.multiply(anyDouble(), anyDouble()))
                            .thenReturn(200.0);
            
            double result = Calculator.multiply(20, 10);
            assertEquals(200.0, result);
            
            mockedCalculator.verify(() -> Calculator.multiply(anyDouble(), anyDouble()), times(1));
        }
    }
}