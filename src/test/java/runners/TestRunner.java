package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

// The TestRunner class is used to run Cucumber tests with JUnit
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",       // Path to the feature files
    glue = "stepdefinitions",                       // Path to the step definitions
    plugin = {                                      // Reporting configuration
        "pretty",                                   // Prints the results to the console in a readable format
        "html:target/cucumber-reports.html",         // HTML report
        "json:target/cucumber-reports.json"          // JSON report (useful for CI tools)
    }
)
public class TestRunner {
    // This class doesn't need any code, as the @RunWith(Cucumber.class) 
    // and @CucumberOptions annotations handle the test execution.
}
