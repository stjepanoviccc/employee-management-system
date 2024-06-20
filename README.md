# Employee Management System API
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-black?logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring%20Security-black?logo=springsecurity)
![Swagger](https://img.shields.io/badge/Swagger-green?logo=swagger&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue?logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-red?logo=hibernate&logoColor=white)
![Liquibase](https://img.shields.io/badge/Liguibase-brightgreen?logo=junit&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-blue?logo=docker&logoColor=white)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-green?logo=rabbitmq&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-yellow?logo=mockito&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-brightgreen?logo=junit&logoColor=white)
![MockMVC](https://img.shields.io/badge/MockMVC-brightgreen?logo=mockmvc&logoColor=white)

## Technologies Used
Main technologies which are used in this project are **Spring Boot**, **Hibernate**, **Spring Security**, for database its **Postgres** with **Liquibase** for versioning. For message queue I used **RabbitMQ** and API is documented with **Swagger**. For testing I used **Mockito**, **Mock MVC** and **JUnit**  and for containerization is **Docker**

## Overview:  
The Employee Management System is a web api that allows users to perform CRUD operations (Create, Read, Update, Delete) on employee records.  
It includes user authentication and authorization, logging with AOP, message queue communication, advanced querying with Hibernate, and database versioning with Liquibase. The application will be containerized using Docker.

## Getting Started:  
To start this project you need to have installed **Docker** and **Docker Compose**.  
Navigate to root of your project and run **docker-compose up --build** and your app should start.  

**NOTE**: If you get problem with build, you may need to kill services on your local machine for rabbitmq and postgres.  
In order to do that you need next set of commands:  

**Linux**:  
1. sudo lsof -i :PORT_NUMBER
2. sudo kill -9 PID
   
**Windows**:
1. netstat -ano | findstr :PORT_NUMBER
2. tasklist /FI "PID eq PID_NUMBER"
3. taskkill /PID PID_NUMBER /F

## API Documentation
The API is documented using Swagger. Once the application is running, you can access the Swagger UI at: **http://localhost:8080/swagger-ui.html**  
Swagger UI provides a user-friendly interface for exploring and testing the API endpoints. To check details for API documentation you can access **swagger-ui**.

## Spring Security:  
In this project I implemented **JWT(Json Web Token)** for Authentication.  
There are two different roles in this app **(USER, ADMIN)**.  
You can check config file in **config/SecurityConfiguration.java**. As you can see, all requests for swagger and auth endpoints are allowed so there won't be Unauthorized exception.  
Unauthorized user can only login and register, USER role can get employee by id and get all employees, while ADMIN role can do everything.

## Error Handling:  
**Global Exception Handling**: Spring Boot's global exception handling mechanism is employed to manage and respond to exceptions effectively throughout the application. This ensures consistency and reliability in handling various types of errors.  

**NotFoundException**: For model-related errors, such as when a requested resource is not found, the application utilizes a custom NotFoundException. This exception is thrown when attempting to access a resource that does not exist, providing clear feedback to the client.  

**BadRequestException**: In cases of technical errors or invalid requests, the application employs BadRequestException. This exception is used to indicate problems with the client's request, such as malformed input or missing parameters. By utilizing this exception, the application can provide meaningful error messages and guide clients towards resolving their requests.  

**UnauthorizedException**: When a client attempts to access a resource without proper authentication or authorization, the application throws UnauthorizedException. This exception signals that the request lacks the necessary credentials or permissions to perform the operation, prompting clients to authenticate or obtain appropriate authorization.

## Testing:  
The Employee Management System API has been thoroughly tested to ensure reliability and functionality across its features. Both integration and unit tests were conducted using frameworks such as MockMvc, JUnit, Mockito, and others. Below is an overview of the testing approach:  

**Unit Tests**  
Unit tests were implemented to test individual components, methods, and classes in isolation. These tests validate the correctness of business logic and ensure that each unit behaves as expected.   

**Integration Tests**
Integration tests were conducted to verify the interaction between various components within the application, ensuring that integrated units function correctly as a whole.  

1. MockMvc: Utilized for testing the entire application stack, including controllers, service layer, and repository layer, in a controlled environment.
2. JUnit: Used for writing and executing unit tests.
3. Mockito: Employed for mocking dependencies and verifying interactions between components.

## Deploy:  
### Dockerfile  
The multistage Dockerfile for this application optimizes the Docker image build process by leveraging separate stages for build and runtime environments. This approach enhances efficiency, security, and maintainability of the Docker image, adhering to best practices for Java application deployment in Docker containers.  

**BUILD STAGE**:  

**Base Image - maven:3.8.4-openjdk-17**: This stage starts with a Maven image that includes JDK 17, suitable for building Java applications.  
**Working Directory: /app**: Sets the working directory within the container where the application source code will be copied and built.  
**Copy Files**: Copies pom.xml and the entire src directory into the container.    
**Build Application**: Executes mvn clean package -DskipTests to build the application. It cleans build enviroment, then package and skip tests during build process to speed up process.  

**RUNTIME STAGE**:  

**Base Image: openjdk:17-jdk-slim**: This stage starts with a minimal OpenJDK 17 image, optimized for runtime.  
**Volume:**: Defines a volume mount point at /tmp, allowing the application to write temporary files inside the container.    
**Copy Artifact**: Copies the built JAR file (app/target/*.jar) from the build stage into the current runtime stage, renaming it to app.jar.      
**Entry Point**: Defines the command to run when the container starts. java -jar /app.jar executes the JAR file as the main application entry point.  

### docker-compose.yml  

This Docker Compose file defines a multi-container setup for an Employee Management System (EMS) application, including PostgreSQL for the database, RabbitMQ for messaging, Swagger UI for API documentation, and the EMS application itself. Here's an explanation of each section:  

### Services:  

**1. psql-db**:    

**Image**: Uses the official PostgreSQL image.  
**Environment Variables**: Configures environment variables required for PostgreSQL:  
1. **POSTGRES_USER**: Specifies the PostgreSQL username as postgres.  
2. **POSTGRES_PASSWORD**: Specifies the password for the PostgreSQL user as root.  
3. **POSTGRES_DB**: Specifies the name of the PostgreSQL database as ems_db.  
**Ports Mapping**: Maps port 5432 of the host machine to port 5432 of the PostgreSQL container, allowing access to the PostgreSQL service from outside the container.
**Volumes**: Mounts a volume (db_data) to persist PostgreSQL data.

**2. rabbitmq**:  

**Image**: Uses the official RabbitMQ image with management plugins.  
**Ports**:  
1. **5672**: RabbitMQ default port for AMQP.
2. **15672**: RabbitMQ management UI port.  

**3. swagger**:  

**Image**: Uses the Swagger UI image from **swaggerapi**.  
**Ports**: Maps host port 8090 to container port 8080 for Swagger UI.  
**Enviroment Variables**:  
1. **SWAGGER_JSON**: Specifies the location of the Swagger JSON file **/api-docs/swagger.json**.
2. **BASE_URL**: Sets the base URL for Swagger UI **/swagger-ui**.

**4. app**:  

**Build**: Builds the EMS application using the Dockerfile (Dockerfile) in the current context .  
**Ports**: Maps host port 8080 to container port 8080 for the EMS application.  
**Enviroment Variables**:  
1. **SPRING_DATASOURCE_URL**: JDBC URL to connect to PostgreSQL (jdbc:postgresql://psql-db:5432/ems_db).
2. **SPRING_DATASOURCE_USERNAME**: PostgreSQL username (postgres).
3. **SPRING_DATASOURCE_PASSWORD**: PostgreSQL password (root).
4. **SPRING_JPA_HIBERNATE_DDL_AUTO**: Hibernate DDL auto strategy (none to disable automatic schema generation).  
**Depends On**: Specifies dependencies on psql-db, rabbitmq, and swagger services.  
**Command**: Executes Maven test (./mvnw test) command when starting the container.

**NOTE**: Ensure that **Docker** and **Docker Compose** are installed on your system and that there are no conflicts with ports already in use by other applications. Adjust configurations as needed based on your specific development environment and requirements.

## Author

**Andrej Stjepanović**  
Student at the **Faculty of Technical Sciences** in Novi Sad  
Undergraduate of Software Engineering

