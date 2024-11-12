package helpers;

import config.ConfigLoader;
import config.Endpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * BookHelper provides methods for managing book transactions,
 * including marking all borrowed books as returned.
 */
public class BookHelper {

    private static final Logger logger = LogManager.getLogger(BookHelper.class);
    /**
     * Iterates through borrowing transactions to ensure each borrowed book
     * is marked as returned by updating transactions with a return date.
     * This method fetches the latest list of transactions, checks if each
     * transaction has a return date, and if not, sends a request to mark
     * it as returned. It continues this process until all transactions
     * contain a return date, indicating all books have been returned.
     */
    public static void returnAllBorrowedBooks() {
        boolean allReturned = false;
        String token = TokenManager.getToken();
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", ConfigLoader.getUsername());

        while (!allReturned) {
            logger.info("Sending POST request to borrowing history endpoint: {}",RestAssured.baseURI + Endpoints.BORROWING_HISTORY.getPath());
            Response response = RestAssured
                    .given()
                    .baseUri(RestAssured.baseURI)
                    .basePath(Endpoints.BORROWING_HISTORY.getPath())
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .body(requestBody.toString())
                    .post();

            String responseBody = response.getBody().asString();
            JSONArray transactions;

            try {
                // Attempt to parse response as JSONArray
                transactions = new JSONArray(responseBody);
            } catch (JSONException e) {
                // If parsing fails, log the response and stop execution
                System.err.println("Expected a JSON array but received: " + responseBody);
                return;
            }

            allReturned = true;

            for (int i = 0; i < transactions.length(); i++) {
                JSONObject transaction = transactions.getJSONObject(i);

                if (!transaction.has("returnDate")) {
                    logger.info("Sending POST request to return book endpoint: {}",RestAssured.baseURI + Endpoints.RETURN_BOOK.getPath());
                    RestAssured
                            .given()
                            .baseUri(RestAssured.baseURI)
                            .basePath(Endpoints.RETURN_BOOK.getPath())
                            .header("Authorization", "Bearer " + token)
                            .header("Content-Type", "application/json")
                            .body(transaction.toString())
                            .post();

                    allReturned = false; // There are still items to return
                }
            }

            // Add a small delay to avoid rapid, repeated requests
            try {
                Thread.sleep(100); // Adjust delay as needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All transactions have been marked with a return date.");
    }
}
