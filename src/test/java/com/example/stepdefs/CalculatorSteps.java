package com.example.stepdefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.junit.Assert.*;

public class CalculatorSteps {

    private int num1;
    private int num2;
    private int result;

    @Given("I have two numbers {int} and {int}")
    public void givenTwoNumbers(int num1, int num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    @When("I add the numbers")
    public void addNumbers() {
        result = num1 + num2;
    }

    @Then("the result should be {int}")
    public void checkResult(int expectedResult) {
        assertEquals(expectedResult, result);
    }
}
