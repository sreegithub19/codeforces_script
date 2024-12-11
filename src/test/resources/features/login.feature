Feature: Basic Calculator

  Scenario: Add two numbers
    Given I have two numbers 5 and 3
    When I add the numbers
    Then the result should be 8
