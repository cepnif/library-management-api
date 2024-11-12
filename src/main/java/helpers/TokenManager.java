package helpers;

import config.ConfigLoader;
import config.Endpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;

import java.time.LocalDateTime;

/**
 * TokenManager handles authentication for the application, providing a valid token
 * for API requests and refreshing it as needed when the token expires.
 */
public class TokenManager {

    private static String token;
    private static LocalDateTime tokenExpiryTime;

    /**
     * Retrieves a valid authentication token. If the token is null, expired, or not yet generated,
     * it initiates a new authentication request to refresh the token.
     *
     * @return A valid token as a String for authorization purposes.
     */
    public static String getToken() {
        if (token == null || tokenExpiryTime == null || tokenExpiryTime.isBefore(LocalDateTime.now())) {
            authenticate();
        }
        return token;
    }

    /**
     * Authenticates the user by sending login credentials to the login API endpoint.
     * If authentication is successful, it updates the token and token expiry time.
     * The token expiry time is determined by the token expiration configuration property.
     * @throws RuntimeException if the authentication request fails.
     */
    private static void authenticate() {
        // Build login payload
        JSONObject credentials = new JSONObject();
        credentials.put("username", ConfigLoader.getUsername());
        credentials.put("password", ConfigLoader.getPassword());

        // Send the login request
        Response response = RestAssured.given()
                .baseUri(ConfigLoader.getBaseUrl())
                .header("Content-Type", "application/json")
                .body(credentials.toString())
                .post(Endpoints.LOGIN.getPath());

        if (response.getStatusCode() == 200) {
            token = response.jsonPath().getString("token"); // Adjust this based on actual token field
            tokenExpiryTime = LocalDateTime.now().plusMinutes(Long.parseLong(ConfigLoader.getTokenExpiryTime()));
        } else {
            throw new RuntimeException("Failed to authenticate: " + response.getStatusLine());
        }
    }
}
