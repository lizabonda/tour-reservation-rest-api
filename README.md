# Tour Reservation REST API

REST API for managing tourist tours and reservations, developed as a personal study project using Java and Spring Boot.

## Technologies
- Java
- Spring Boot (Spring Web, Spring Data JPA, Spring Security)
- Hibernate (JPA)
- MapStruct
- JUnit 5, Mockito

## Features
- CRUD operations for reservations
- Layered architecture (Controller / Service / Repository)
- DTO mapping using MapStruct
- Role-based access control (USER, ADMIN)
- Data persistence with JPA/Hibernate
- Unit tests for service layer

## Architecture
The application follows a layered architecture:
- **Controller** 
- **Service** 
- **Repository**

## Security
- Implemented basic authentication and authorization using Spring Security
- Protected REST endpoints based on user roles

## Testing
- Unit tests written for service layer
- Mockito used for mocking repository dependencies
