# Support Desk Management System API

A RESTful API built with Spring Boot, Spring Security (JWT), Spring Data JPA, and MySQL. Features full Role-Based Access Control (RBAC) across Customer, Admin, and Employee workflows, automated ticket assignment, and Postman API verification.

## Tech Stack
- **Backend:** Java, Spring Boot, Spring Data JPA, Spring Security
- **Authentication:** JSON Web Tokens (JWT)
- **Database:** MySQL Server (`supportDesk` schema)
- **Build Tool:** Maven

## Setup & Running Instructions

### 1. Database Configuration
Create a MySQL database named `supportDesk`. Update credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/supportDesk
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
