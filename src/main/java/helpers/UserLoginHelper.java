package helpers;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * UserLoginHelper provides methods to facilitate login functionality,
 * including constructing credentials, sending login requests, and validating responses.
 */
public class UserLoginHelper {

    private static final Logger logger = LogManager.getLogger(UserLoginHelper.class);
    private Response response;

    /**
     * Constructs a JSON object with login credentials.
     * @param username The username for login; if null, it is set as an empty string.
     * @param password The password for login; if null, it is set as an empty string.
     * @return A JSONObject containing the username and password.
     */
    public JSONObject constructCredentials(String username, String password) {
        JSONObject credentials = new JSONObject();
        credentials.put("username", username != null ? username : "");
        credentials.put("password", password != null ? password : "");
        return credentials;
    }

    /**
     * Sends a POST request with the given credentials to the login endpoint.
     * @param credentials The JSON object containing username and password for login.
     * @throws IllegalStateException if the credentials are null.
     */
    public void sendPostRequest(JSONObject credentials) {
        if (credentials == null) {
            logger.error("Credentials are null. Cannot send POST request.");
            throw new IllegalStateException("Credentials must be set before sending a request.");
        }

        String requestBody = credentials.toString();
        logger.info("Sending POST request with payload: {}", requestBody);
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post();

        logger.info("Received response with Status Code: {}", response.getStatusCode());
        logger.debug("Response Body: {}", response.getBody().asString());
    }

    /**
     * Validates the status code of the received response against the expected status code.
     * @param expectedStatusCode The expected HTTP status code.
     * @throws IllegalStateException if no response is available for validation.
     */
    public void validateStatusCode(int expectedStatusCode) {
        if (response == null) {
            logger.error("No response available to validate status code.");
            throw new IllegalStateException("Response must be available for validation.");
        }
        logger.info("Asserting Status Code: {}", expectedStatusCode);
        Assertions.assertEquals(expectedStatusCode, response.getStatusCode(), "Expected status code did not match");
        logger.info("Status Code matched successfully");
    }

    /**
     * Validates that the response body contains a token and an expiration date.
     * @throws IllegalStateException if no response is available for validation.
     */
    public void validateResponseBody() {
        if (response == null) {
            logger.error("No response available to validate body content.");
            throw new IllegalStateException("Response must be available for validation.");
        }
        logger.info("Validating response contains token and expiration date");
        response.then()
                .body("token", notNullValue())
                .body("memberAccountExpirationDate", notNullValue());
        logger.debug("Token: {}", response.jsonPath().getString("token"));
        logger.debug("Member Account Expiration Date: {}", response.jsonPath().getString("memberAccountExpirationDate"));
        logger.info("Token and expiration date validated successfully");
    }

    /**
     * Validates the error message in the response body against the expected error message.
     * @param expectedErrorMessage The expected error message.
     * @throws IllegalStateException if no response is available for validation.
     */
    public void validateErrorMessage(String expectedErrorMessage) {
        if (response == null) {
            logger.error("No response available to validate error message.");
            throw new IllegalStateException("Response must be available for validation.");
        }
        logger.info("Validating error message");
        String actualErrorMessage = response.jsonPath().getString("error");
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage, "Expected error message did not match");
        logger.debug("Error Message: {}", actualErrorMessage);
        logger.info("Error message validated successfully");
    }
}
