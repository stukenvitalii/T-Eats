# Eats: Food Delivery Web Application

Eats is a food delivery web application designed to connect users with local restaurants and enable seamless ordering. This project utilizes a microservices architecture, making it scalable and maintainable.

## Features

- User registration and login
- Restaurant listing with menus
- Order placement and management
- User profile management

## Architecture

The application follows a microservices architecture with the following services:
- **API Gateway**: Handles jwt tokens, registration and rouiting.
- **User Service**: Handles user information and profile management.
- **Restaurant Service**: Manages restaurant listings, menus, and related data.
- **Order Service**: Responsible for order placement, tracking, and management.

### Database

All data (users, restaurants, menus, and orders) is stored in a single PostgreSQL database.

### Technologies Used

- Java
- Spring Boot
- Spring Data JPA
- Liquibase
- PostgreSQL
- Maven
- Docker (for containerization)\
- Prometheus
- Grafana
