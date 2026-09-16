# OrderFlow

OrderFlow is a backend order management service built with Java and Spring Boot.

The service provides REST APIs to create, retrieve, update, and delete customer orders. It uses MySQL for persistence and includes request validation, centralized exception handling, and automated tests.

## Tech Stack

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Maven
- JUnit
- Mockito
- MockMvc
- Git / GitHub

## Current Features

- Create an order
- Get all orders
- Get an order by ID
- Update an order
- Delete an order
- Request validation
- Centralized exception handling
- Custom 404 handling for missing orders
- Service-layer unit tests
- Controller-layer tests
- Spring Boot Actuator health check

## Project Structure

```text
orderflow/
├── README.md
└── order-service/
    ├── src/
    │   ├── main/
    │   │   ├── java/com/orderflow/order_service/
    │   │   │   ├── controller/
    │   │   │   ├── dto/
    │   │   │   ├── entity/
    │   │   │   ├── exception/
    │   │   │   ├── repository/
    │   │   │   └── service/
    │   │   └── resources/
    │   └── test/
    │       └── java/com/orderflow/order_service/
    ├── pom.xml
    └── ...

## Architecture

The current version of OrderFlow follows a simple layered architecture:

```text
Client
  |
  | HTTP Request
  v
OrderController
  |
  v
OrderService
  |
  v
OrderRepository
  |
  v
MySQL

The controller handles HTTP requests, the service layer contains the application logic, and the repository handles database operations using Spring Data JPA.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/orders` | Create a new order |
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{id}` | Get an order by ID |
| PUT | `/api/orders/{id}` | Update an existing order |
| DELETE | `/api/orders/{id}` | Delete an order |
| GET | `/actuator/health` | Check application health |

## Example Request

### Create Order

```http
POST /api/orders
Content-Type: application/json

{
  "productName": "Laptop",
  "quantity": 2,
  "price": 1299.99
}

Example response:
{
  "id": 1,
  "productName": "Laptop",
  "quantity": 2,
  "price": 1299.99
}


---

# Step 4 — Validation

Immediately after the Example Request section.

### Copy-paste:

```markdown
## Validation

The API validates incoming order requests before processing them.

For example:

```json
{
  "productName": "",
  "quantity": 0,
  "price": -10
}
returns a 400 Bad Request response with validation messages.

Example response:
{
  "quantity": "Quantity must be at least 1",
  "price": "Price must be greater than 0",
  "productName": "Product name is required"
}


This is based directly on the validation you already tested with Postman.

---

# Step 5 — Error Handling

Immediately after Validation.

### Copy-paste:

```markdown
## Error Handling

OrderFlow uses centralized exception handling for API errors.

For example, requesting an order that does not exist:

```http
GET /api/orders/19

returns:

{
  "message": "Order not found with id: 19"
}  

The API returns 404 Not Found when the requested order does not exist.

## Database Configuration

OrderFlow uses MySQL for persistence.

Create the database:

```sql
CREATE DATABASE orderflow;

The application connects to MySQL using the following local configuration:

Database: orderflow
Port: 3306
Username: orderflow_user

A sample configuration is provided in:
order-service/src/main/resources/application-example.properties


---

# Step 7 — Running the Application

Immediately after the Database Configuration section.

### Copy-paste:

```markdown
## Running the Application

Make sure Java 21, Maven, and MySQL are installed and running.

Navigate to the `order-service` directory:

```bash
cd order-service

Start the application:
mvn spring-boot:run

The application runs on:
http://localhost:8080

You can verify the application health using:
http://localhost:8080/actuator/health

{
  "status": "UP"
}


---

# Step 8 — Running Tests

Immediately after the Running the Application section.

### Copy-paste:

```markdown
## Running Tests

Run the complete test suite with:

```bash
mvn test

The project includes tests for:

Order service operations
Order controller endpoints
Request validation
Order not found scenarios
Update operations
Delete operations

The current test suite contains 16 tests across the application context, service layer, and controller layer.

## Build

To build the project and run all tests:

```bash
mvn clean install

The command compiles the application, runs the test suite, and creates the Spring Boot JAR file under:
order-service/target/


### Step 10 — Add Future Enhancements

Below the Build section, add:

```markdown
## Future Enhancements

Planned improvements for future versions include:

- Kafka-based order events
- Inventory service
- Notification service
- Event-driven communication between services
- Improved application logging and monitoring
- AWS deployment
- Additional integration tests
- API documentation with OpenAPI/Swagger