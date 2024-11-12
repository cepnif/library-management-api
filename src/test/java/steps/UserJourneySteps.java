package steps;

import config.ConfigLoader;
import helpers.TokenManager;
import helpers.LibraryHelper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import java.util.List;

public class UserJourneySteps {

    private static final Logger logger = LogManager.getLogger(UserJourneySteps.class);
    private Response response;
    private final LibraryHelper libraryHelper = new LibraryHelper();

    @Given("the member logs in to the library system")
    public void the_member_logs_in_to_the_library_system() {
        logger.info("Ensuring valid login credentials are configured.");
        String token = TokenManager.getToken();
        logger.info("Member logged in successfully.");
        Assertions.assertNotNull(token, "Authorization token is null!");
        logger.info("Authorization token received.");
    }

    @Given("the member checks the availability of a book titled {string}")
    public void the_member_checks_the_availability_of_a_book_title_a_book (String title) {
        String token = TokenManager.getToken();
        response = libraryHelper.checkBookAvailability(token, title);
        logger.info("Searching for book titled: {}", title);
        Assertions.assertEquals(200, response.getStatusCode(), "Failed to check book availability!");
    }

    @Given("the book is available for borrowing")
    public void the_book_is_available_for_borrowing() {
        long availableCopies = response.jsonPath().getLong("availableCopies");
        String title = response.jsonPath().getString("title");
        Assertions.assertTrue(availableCopies > 0, "Book is not available for borrowing!");
        logger.info("Book titled {} is available for borrowing", title);
    }

    @When("the member borrows the book titled {string}")
    public void the_member_borrows_the_book(String title) {
        String token = TokenManager.getToken();
        response = libraryHelper.borrowBook(token, title);
        Assertions.assertEquals(201, response.getStatusCode(), "Failed to borrow the book!");
    }

    @Then("the book is successfully borrowed")
    public void the_book_is_successfully_borrowed_with_a_due_date() {
        String message = response.jsonPath().getString("message");
        String username = response.jsonPath().getString("newTransactionJson.username");
        String title = response.jsonPath().getString("newTransactionJson.title");
        String dueDate = response.jsonPath().getString("newTransactionJson.dueDate");
        String borrowDate = response.jsonPath().getString("newTransactionJson.borrowDate");
        Assert.assertEquals(message, "Book borrowed successfully");
        Assert.assertEquals(username, ConfigLoader.getUsername());
        Assert.assertNotNull(response.jsonPath().getString("newTransactionJson.title"));
        Assert.assertNotNull(dueDate);
        Assert.assertNotNull(borrowDate);
        logger.info("Book titled {} borrowed successfully.", title);
    }

    @When("the member reviews their borrowing history")
    public void the_member_reviews_their_borrowing_history() {
        String token = TokenManager.getToken();
        response = libraryHelper.getBorrowingHistory(token);
        Assertions.assertEquals(200, response.getStatusCode(), "Failed to retrieve borrowing history!");
        logger.info("Borrowing history retrieved successfully.");
    }

    @Then("the system confirms the book {string} is listed in the borrowing history")
    public void the_system_confirms_the_book_is_listed_int_the_borrowing_history(String bookTitle) {
        List<String> unreturnedTitles = response.jsonPath()
                .getList("findAll { it.returnDate == null }.title");
        String dueDate = response.jsonPath().getString("dueDate");
        String borrowDate = response.jsonPath().getString("dueDate");
        String returnDate = response.jsonPath().getString("returnDate");

        Assertions.assertEquals(bookTitle, unreturnedTitles.getFirst(), "Book title did not match!");
        Assertions.assertNotNull(dueDate, "Due date is not provided for borrowed book!");
        Assertions.assertNotNull(borrowDate, "Borrow date is not provided for borrowed book!");
        Assertions.assertNotNull(returnDate, "Return date is not provided for borrowed book!");
        logger.info("Book titled {} is successfully listed in the borrowing history", bookTitle);
    }

    @When("the member returns the book titled {string}")
    public void the_member_returns_the_book_titled(String title) {
        String token = TokenManager.getToken();
        response = libraryHelper.returnBook(token, title);
        Assertions.assertEquals(200, response.getStatusCode(), "Failed to return the book!");
        logger.info("Book titled {} returned successfully.", title);
    }

    @Then("the book is successfully returned and marked as {string} in the library system")
    public void the_book_is_successfully_returned_and_marked_as_in_the_library_system(String message) {
        //response = libraryHelper.getBookStatus(token, bookTitle);
        String actualMessage = response.jsonPath().getString("message");
        String title = response.jsonPath().getString("transactionJson.title");
        Assertions.assertEquals(message, actualMessage, "Book status has not been updated!");
        logger.info("Returned book titled {} updated in the system successfully.", title);
    }

    @Then("the system shows the details of {string} with a borrowed and returned status, along with relevant dates")
    public void the_system_shows_the_details_of_with_a_borrowed_and_returned_status_along_with_relevant_dates(String title) {
        boolean bookFound = libraryHelper.verifyBookInHistory(response, title);
        Assertions.assertTrue(bookFound, "Borrowing history does not contain the expected book details.");
        logger.info("Borrowing history for {} displayed with borrowed and returned status.", title);
    }
}
