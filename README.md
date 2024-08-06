# README: STOCK & STOCK EXCHANGE MANAGEMENT APPLICATION

## Overview

This Spring Boot backend application is designed to manage stocks and stock exchanges, providing functionalities to create, update, delete, and retrieve stocks and stock exchanges. 
It uses H2 memory database for development and testing purposes and ensures secure access to endpoints using Spring Security. 
The application can handle multiple users accessing the system simultaneously. Database transactions are managed with Spring's @Transactional annotation to ensure data integrity. 
Session management is implemented with Spring Security, allowing for multiple concurrent user sessions. The service layer and other components are designed to be thread-safe. 
Detailed instructions on building and running the solution, along with initialization resources such as scripts, are provided to facilitate easy setup and deployment.

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

## Endpoints
* **List a Stock Exchange Along With All Its Listed Stocks**
  - GET /api/v1/stock-exchange/{name}
  - Roles: ADMIN

* **Add the Stock to the Stock Exchange**
  - POST /api/v1/stock-exchange/{name}/add-stock/{stockId}
  - Roles: ADMIN

* **Remove the Stock from the Stock Exchange**
  - DELETE /api/v1/stock-exchange/{name}/remove-stock/{stockId}
  - Roles: ADMIN

* **Create a New Stock**
  - POST /api/v1/stock
  - Roles: ADMIN
  - Request Body:

        {
          "name": "AAPL",
          "description": "Apple",
          "currentPrice": "150.00"
        }

* **Update the Price of a Stock**
  - PUT /api/v1/stock/{id}/update-price
  - Roles: ADMIN, USER
  - Request Body:
    
          800.00
        
* **Remove Stock from the System**
  - DELETE /api/v1/stock/{id}
  - Roles: ADMIN

## Security
* **Authentication**
  - The application uses Spring Security to handle authentication. Users must log in with valid credentials to access the system.
* **Authorization**
  - Users with the ADMIN role have full access to all endpoints. Users with the USER role are authorized only to update stock prices. They do not have access to other administrative functions.

## Database
* The application uses an H2 in-memory database. You can access the H2 console at:
    
          http://localhost:8080/h2-console

  - JDBC URL: jdbc:h2:mem: stockexchangedb 
  - Username: sa
  - Password: password

## Troubleshooting
* **401 Unauthorized Error:** The application uses Spring Security to handle authentication. Users must log in with valid credentials to access the system.
* **403 Forbidden Error:** The application uses Spring Security to handle authentication. Users must log in with valid credentials to access the system.
