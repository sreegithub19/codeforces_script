Feature: Login functionality

  Scenario: User logs in with valid credentials
    Given the user is on the login page
    When the user enters valid credentials
    Then the user should be redirected to the dashboard
