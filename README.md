
# Library Management API Test Framework

## Overview

This project is a test automation framework designed to verify the functionality of a Library Management API system. Built using Behavior Driven Development (BDD), it focuses solely on **functional testing** for endpoints related to user login, book borrowing, and transaction history.

## Key Technologies

- **Java 21** - Core programming language.
- **Maven** - Dependency management and build tool.
- **Cucumber 7.10.0** - For defining test scenarios in BDD format.
- **Rest-Assured 5.0.1** - Simplifies RESTful API testing.
- **JUnit 5.8.2** - Testing framework.
- **Log4j 2.17.1** - For logging activities and results.

## Project Structure

- **`src/main/java`**: Contains the main code, including:
  - **`config` package**: Manages configurations for endpoints and application properties.
  - **`helpers` package**: Utility classes to manage common actions in tests (explained below).

- **`src/test/java`**: Contains test-specific code:
  - **`steps` package**: Holds step definitions used by Cucumber to map feature steps to Java code.
  - **`runners` package**: Configures and initiates test execution (see `AllFeaturesRunner` below).

- **`src/test/resources/features`**: Stores all test scenarios written in Gherkin syntax.

### `helpers` Package

The `helpers` package includes utility classes to handle essential test operations, keeping test cases streamlined and organized:

- **`UserLoginHelper`**: Manages login actions, constructing credentials and validating login responses.
- **`TokenManager`**: Handles token management, ensuring a valid token is available for authenticated requests.
- **`LibraryHelper`**: Provides methods to interact with library functions like checking book availability, borrowing, and returning books.

### `runners` Package

- **`AllFeaturesRunner`**: A JUnit runner configured to execute all Cucumber feature files. It specifies:
  - Path to feature files (`features`).
  - Glue path for step definitions (`steps`).
  - Reporting plugins (HTML and JSON reports).
  - Console output formatting for readability.

## Getting Started

### Prerequisites

- **Java 21** and **Maven** installed on your system.
- **Git** for cloning the repository.

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd library-management-api-system
   ```

2. Install dependencies:
   ```bash
   mvn clean install
   ```

### Running Tests

Execute all tests and generate reports with:
```bash
mvn test
```

## Reports & Logging

- **Reports**: Located in `target/cucumber-reports`, available in both HTML and JSON formats.
- **Logs**: Stored in the `logs` directory, detailing test execution and results.

## Note

This framework is designed exclusively for **functional testing** and does not cover non-functional aspects such as performance or security.

## Contributing

Contributions are welcome! Submit issues or pull requests to help improve this framework.
