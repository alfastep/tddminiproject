# TDD Mini Project

This is a simple Spring Boot application that demonstrates Test-Driven Development (TDD) principles. It includes CRUD functionality for managing orders.

## Classes

### Order
- Represents an order entity.
- Contains properties such as `id`, `customerName`, `orderDate`, `shippingAddress`, and `total`.
- Includes getter and setter methods for accessing and modifying the properties.

### OrderRepository
- Repository interface for managing Order entities.
- Extends the Spring Data JPA `CrudRepository` interface to inherit common CRUD operations.

### OrderController
- RESTful controller for handling HTTP requests related to orders.
- Includes methods for creating, reading, updating, and deleting orders.
- Uses the `OrderRepository` to interact with the underlying data source.

### OrderControllerTests
- Test class for testing the `OrderController`.
- Includes unit tests for each CRUD operation and error handling.
- Uses Mockito to mock dependencies and verify the behavior of the controller.

### H2 Database Configuration
- The application uses an H2 in-memory database for data storage.
- Connection details and database configuration can be found in the `application.properties` file.
- The H2 console is enabled and can be accessed at `http://localhost:8080/h2-console`.

## Building and Running the Application

To build the application and create the JAR file, run the following command: `./mvnw clean package`

This command will compile the source code, run the tests, and package the application into a JAR file named `tddminiproject-0.0.1-SNAPSHOT.jar` in the `target` directory.

To run the application, use the following command: `java -jar target/tddminiproject-0.0.1-SNAPSHOT.jar`

The application will start and be accessible at `http://localhost:8080`.

## Testing the Application

You can use tools like Postman or browser devtools to test the CRUD functionality of the application.
- Use HTTP GET requests to retrieve orders.
- Use HTTP POST requests to create new orders.
- Use HTTP PUT requests to update existing orders.
- Use HTTP DELETE requests to delete orders.

Additionally, you can run the unit tests included in the `OrderControllerTests` class to verify the behavior of the controller.

## Dependencies
- Spring Boot 2.5.4
- Spring Data JPA 2.5.4
- H2 Database
- JUnit 5
- Mockito




