Feature: Library Management - Borrowing, Returning, and Reviewing Book History

  Description:

  As a library member
  I want to search for a book, borrow it, review my borrowing history, and return the book
  So that I can manage my borrowed books effectively and maintain a record of my transactions.

  Background:
    Given the member logs in to the library system
    And the member checks the availability of a book titled "Little Blue Truck"
    And the book is available for borrowing

  Scenario: Successful user journey for checking availability, borrowing, returning, and reviewing book history
    When the member borrows the book titled "Little Blue Truck"
    Then the book is successfully borrowed

    When the member reviews their borrowing history
    Then the system confirms the book "Little Blue Truck" is listed in the borrowing history

    When the member returns the book titled "Little Blue Truck"
    Then the book is successfully returned and marked as "Book returned successfully" in the library system