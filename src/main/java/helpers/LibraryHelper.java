package helpers;

import config.ConfigLoader;
import config.Endpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * LibraryHelper provides utility methods to interact with the Library Management API,
 * allowing actions such as checking book availability, borrowing, returning books,
 * and retrieving borrowing history.
 */
public class LibraryHelper {

    private static final Logger logger = LogManager.getLogger(LibraryHelper.class);

    /**
     * Checks the availability of a book based on its title.
     * @param token The authorization token for API access.
     * @param title The title of the book to check for availability.
     * @return A Response object containing the API response for book availability.
     */
    public Response checkBookAvailability(String token, String title) {
        logger.info("Sending GET request to book availability endpoint: {}",RestAssured.baseURI + Endpoints.CHECK_BOOK_AVAILABILITY.getPath());
        return RestAssured.given()
                .baseUri(RestAssured.baseURI)
                .basePath(Endpoints.CHECK_BOOK_AVAILABILITY.getPath())
                .pathParam("title", title)
                .queryParam("title", title)
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .get();
    }

    /**
     * Borrows a book using its title.
     * @param token The authorization token for API access.
     * @param title The title of the book to borrow.
     * @return A Response object containing the API response for the borrow action.
     */
    public Response borrowBook(String token, String title) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", title);
        requestBody.put("username", ConfigLoader.getUsername());
        logger.info("Sending POST request to borrow book endpoint: {}",RestAssured.baseURI + Endpoints.BORROW_BOOK.getPath());
        return RestAssured.given()
                .baseUri(RestAssured.baseURI)
                .basePath(Endpoints.BORROW_BOOK.getPath())
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post();
    }

    /**
     * Returns a borrowed book based on its title.
     * @param token The authorization token for API access.
     * @param title The title of the book to return.
     * @return A Response object containing the API response for the return action.
     */
    public Response returnBook(String token, String title) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("title", title);
        requestBody.put("username", ConfigLoader.getUsername());
        logger.info("Sending POST request to return book endpoint: {}",RestAssured.baseURI + Endpoints.RETURN_BOOK.getPath());
        return RestAssured.given()
                .baseUri(RestAssured.baseURI)
                .basePath(Endpoints.RETURN_BOOK.getPath())
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post();
    }

    /**
     * Retrieves the borrowing history for the current user.
     * @param token The authorization token for API access.
     * @return A Response object containing the API response with the borrowing history.
     */
    public Response getBorrowingHistory(String token) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", ConfigLoader.getUsername());
        logger.info("Sending POST request to borrowing history endpoint: {}",RestAssured.baseURI + Endpoints.BORROWING_HISTORY.getPath());
        return RestAssured.given()
                .baseUri(RestAssured.baseURI)
                .basePath(Endpoints.BORROWING_HISTORY.getPath())
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .post();
    }

    /**
     * Verifies if a specific book title is present in the borrowing history.
     * @param response The Response object containing the borrowing history.
     * @param title    The title of the book to verify in the history.
     * @return true if the book title is found in the borrowing history, false otherwise.
     */
    public boolean verifyBookInHistory(Response response, String title) {
        return response.jsonPath().getList("history.title").contains(title);
    }
}
