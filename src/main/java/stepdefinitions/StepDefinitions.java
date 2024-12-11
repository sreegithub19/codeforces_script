package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

import static org.junit.Assert.assertTrue;

public class StepDefinitions {

    private boolean isUserOnLoginPage = false;
    private boolean isLoggedIn = false;

    @Given("the user is on the login page")
    public void theUserIsOnTheLoginPage() {
        // Simulate that user is on login page
        isUserOnLoginPage = true;
        System.out.println("User is on the login page.");
    }

    @When("the user enters valid credentials")
    public void theUserEntersValidCredentials() {
        // Simulate the action of entering valid credentials
        if (isUserOnLoginPage) {
            isLoggedIn = true;
            System.out.println("User enters valid credentials.");
        }
    }

    @Then("the user should be redirected to the dashboard")
    public void theUserShouldBeRedirectedToTheDashboard() {
        // Check if login was successful
        assertTrue(isLoggedIn);
        System.out.println("User is redirected to the dashboard.");
    }
}
