# Stock & Stock Exchange Management Application

## Overview

This Java based Spring Boot backend application is designed to manage stocks and stock exchanges, providing functionalities to create, update, delete, and retrieve stocks and stock exchanges. 
It uses H2 memory database for development and testing purposes and ensures secure access to endpoints using Spring Security. 
The application can handle multiple users accessing the system simultaneously. Database transactions are managed with Spring's @Transactional annotation to ensure data integrity. 
Session management is implemented with Spring Security, allowing for multiple concurrent user sessions. Extensive testing has been conducted to ensure the robustness and reliability of the application.
Instructions for building and running the solution, with initialization resources such as scripts, are provided to make setup and deployment easier and to clarify the work done during the process.

## Development Tools
* Java Development Kit (JDK): JDK 17 or later
* Maven: 3.9.8 
* Spring Boot: 3.3.2 
* H2 Database: In-memory database for development and testing purposes
* IDE: IntelliJ IDEA, or any other preferred Java IDE

The application will start and listen on http://localhost:8080.

## Dependencies
* Spring Boot Starter Web
* Spring Boot Starter Data JPA
* Spring Boot Starter Security
* H2 Database
* Spring Boot Starter Test (for testing purposes)
* Mockito Core
* Mockito Junit Jupiter

## Tools and Plugins
* Git: Version control system to clone the repository
* Postman or cURL: For testing API endpoints
* Apache JMeter 5.6.3: For load testing

## Installation

* **Clone Repository**
* *IntelliJ IDEA*
  - Go to File > New > Project from Version Control
  - Enter the repository URL
  - Click Clone
    
* *Terminal*

        git clone https://github.com/busrayesilbas1/StockApplication.git
        cd StockApplication
        mvn install

* **Build**
* *IntelliJ IDEA*
  - Go to Build > Build Project or press Ctrl + F9. 
* *Terminal*
  
        mvn clean install

* **Run**
* *IntelliJ IDEA*
  - Right-click on the main class file containing the main method. Select Run 'StockApplication.main()'. 
* *Terminal*
  
        mvn exec:java -Dexec.mainClass="org.casestudy.stockapplication.StockApplication"

## Endpoints
* **List a stock exchange along with all its stocks**
  - GET /api/v1/stock-exchange/{name}
  - Roles: ADMIN

* **Add the stock to the stock exchange**
  - POST /api/v1/stock-exchange/{name}/add-stock/{stockId}
  - Roles: ADMIN

* **Remove the stock from the stock exchange**
  - DELETE /api/v1/stock-exchange/{name}/remove-stock/{stockId}
  - Roles: ADMIN

* **Create a new stock**
  - POST /api/v1/stock
  - Roles: ADMIN
  - Request Body:

        {
          "name": "AAPL",
          "description": "Apple",
          "currentPrice": "150.00"
        }

* **Update the price of a stock**
  - PUT /api/v1/stock/{id}/update-price
  - Roles: ADMIN, USER
  - Request Body:
    
          800.00
        
* **Remove stock from the system**
  - DELETE /api/v1/stock/{id}
  - Roles: ADMIN

## Database
* The application uses an H2 in-memory database. You can access the H2 console at:
    
          http://localhost:8080/h2-console

  - JDBC URL: jdbc:h2:mem: stockexchangedb 
  - Username: sa
  - Password: password
    
## Security
* **Authentication**
  - The application uses Spring Security to handle authentication. Users must log in with valid credentials to access the system. User credentials are defined in the SecurityConfig class.
  - ROLE: ADMIN
    - username: admin
    - password: adminpassword
  - ROLE: USER
    - username: user1
    - password: password1
  - 
    - username: user2
    - password: password2
    
    
* **Authorization**
  - Users with the ADMIN role have full access to all endpoints. Users with the USER role are authorized only to update stock prices. They do not have access to other administrative functions.

## Testing
* **Unit and Integration Tests**
  - Both unit tests and integration tests have been developed and run locally to ensure the correctness and robustness of the application.
  - To run tests from terminal:

        mvn test
        
* **API Endpoint Testing with Postman**
  - In addition to unit and integration tests, all API endpoints have been tested using Postman to ensure they behave as expected with different scenarios.

* **Load Testing with Apache JMeter** 
  - Multiple users can use the system simultaneously. To ensure the system can handle concurrent users, load testing was performed using Apache JMeter. The results confirm that the system supports multiple simultaneous users effectively.

## Troubleshooting
* **401 Unauthorized Error:** The request has not been applied because it lacks valid authentication credentials. Ensure you are logged in with valid credentials. 
* **403 Forbidden Error:** The server understood the request, but refuses to authorize it. The client does not have the necessary permissions for the resource. Ensure you have the correct role (ADMIN or USER) when accessing endpoints.
* **404 Not Found Error:** The server cannot find what you are looking for. The URL might be wrong or the resource might not exist. Make sure the URL is correct or the resource exists.
* **500 Internal Server Error:** The server has a problem and cannot complete the request. Check the application logs for more details.
