package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/successful_user_journey.feature", // Path to the specific feature file
        glue = {"steps", "hooks"},                                             // Path to the step definitions
        plugin = {"pretty", "html:target/cucumber-reports/login-report.html",
                "json:target/cucumber-reports/login-report.json"},    // Plugins for HTML and JSON reports
        monochrome = true                                              // Makes the console output more readable
)
public class SuccessfulUserJourneyRunner {
}
