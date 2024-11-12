Feature: Member Login

  Description:

  As a library member
  I want to log in to the library management system
  So that I can access restricted features like borrowing and returning books.

  Background:
    Given the login API is available

  Scenario: Successful login
    Given the member provides valid login credentials
    When a POST request is sent to the login API
    Then the request should return status 200
    And the response should contain a token and memberAccountExpirationDate

  Scenario Outline: Unsuccessful login attempt with invalid credentials
    Given the member provides invalid login credentials "<username>" and "<password>"
    When a POST request is sent to the login API
    Then the request should return status <status_code>
    And the error message should indicate "<error_message>"

    Examples:
      | username    | password        | status_code | error_message       |
      | invalidUser | Secpass         | 401         | Invalid credentials |
      | FatihC      | invalidPassword | 401         | Invalid credentials |
      |             | Secpass         | 401         | Invalid credentials |
      | FatihC      |                 | 401         | Invalid credentials |
