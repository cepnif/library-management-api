package hooks;

import config.ConfigLoader;
import helpers.BookHelper;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestHooks {
    private static final Logger logger = LogManager.getLogger(TestHooks.class);

    @Before
    public void setUpScenario() {
        // Set up actions before each scenario, such as logging the start of the scenario
        logger.info("*** Starting scenario setup ***");

        // Set RestAssured base URI here directly
        RestAssured.baseURI = ConfigLoader.getBaseUrl();
        logger.info("Base URI set to: {}", RestAssured.baseURI);
    }

    @Before
    public void returnBooks(){
        logger.info("*** Returning all books to library ***");
        BookHelper.returnAllBorrowedBooks();
        logger.info("*** All books returned successfully ***");
    }

    @After
    public void tearDownScenario() {
        // Clean up actions after each scenario, like logging scenario completion
        logger.info("*** Scenario completed, performing tear-down ***");

        RestAssured.reset();
    }
}
