package steps;

import config.ConfigLoader;
import config.Endpoints;
import helpers.UserLoginHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

public class UserLoginSteps {

    private static final Logger logger = LogManager.getLogger(UserLoginSteps.class);
    private final UserLoginHelper loginHelper = new UserLoginHelper();

    private JSONObject credentials;

    @Given("the login API is available")
    public void the_login_api_is_available() {
        RestAssured.baseURI = ConfigLoader.getBaseUrl() + Endpoints.LOGIN.getPath();
        logger.info("Base URI set to: {}", RestAssured.baseURI);
        logger.info("Base URI setup completed");
    }

    @Given("the member provides valid login credentials")
    public void the_member_provides_valid_login_credentials (){
        credentials = loginHelper.constructCredentials(ConfigLoader.getUsername(), ConfigLoader.getPassword());
        logger.debug("Constructed Valid Credentials: {}", credentials);
        logger.info("Valid Credential setup completed");
    }

    @Given("the member provides invalid login credentials {string} and {string}")
    public void the_member_provides_invalid_login_credentials (String username, String password){
        credentials = loginHelper.constructCredentials(username, password);
        logger.debug("Constructed Invalid Credentials: {}", credentials);
        logger.info("Invalid Credential setup completed");
    }

    @When("a POST request is sent to the login API")
    public void a_post_request_is_sent_to_the_login_api() {
        loginHelper.sendPostRequest(credentials);
        logger.info("POST request completed");
    }

    @Then("the request should return status {int}")
    public void the_request_should_return_status(int statusCode) {
        loginHelper.validateStatusCode(statusCode);
        logger.info("Status Code assertion completed");
    }

    @Then("the response should contain a token and memberAccountExpirationDate")
    public void the_response_should_contain_token_and_memberAccountExpirationDate() {
        loginHelper.validateResponseBody();
        logger.info("Token and expiration date validation completed");
    }

    @Then("the error message should indicate {string}")
    public void the_error_message_should_indicate(String expectedErrorMessage) {
        loginHelper.validateErrorMessage(expectedErrorMessage);
        logger.info("Error message validation completed");
    }
}
