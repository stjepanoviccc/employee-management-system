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
To start this project you need to have installed **Docker**.  
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
Dockerfile utilizes multistage builds to optimize the build and runtime environments for the application. Here's a breakdown of each section and its purpose:  

**BUILD STAGE**:  

**Base Image - maven:3.8.4-openjdk-17**: This stage starts with a Maven image that includes JDK 17, suitable for building Java applications.  
**Working Directory: /app**: Sets the working directory within the container where the application source code will be copied and built.  
**Copy Files**: Copies pom.xml and the entire src directory into the container.    
**Build Application**: Executes mvn clean package -DskipTests to build the application. It cleans build enviroment, then package and skip tests during build process to speed up process.  
**Entry Point**: Finally, it specifies the command to execute when the Docker container starts, which is to run the Java application using the java -jar command and passing the path to the JAR file (/bcpp.jar). 
This Dockerfile simplifies the deployment process of the bcpp application by encapsulating it into a Docker image, making it easier to manage and deploy across different environments.  

**RUNTIME STAGE**:  
**Base Image: openjdk:17-jdk-slim**: This stage starts with a minimal OpenJDK 17 image, optimized for runtime.  
**Volume:**: Defines a volume mount point at /tmp, allowing the application to write temporary files inside the container.    
**Copy Artifact**: Copies the built JAR file (app/target/*.jar) from the build stage into the current runtime stage, renaming it to app.jar.      
**Entry Point**: Defines the command to run when the container starts. java -jar /app.jar executes the JAR file as the main application entry point.  

### docker-compose.yml  
This docker-compose.yml file is used to define and manage the services required for the bcpp (Benefit Card Payment Processor) application using Docker Compose.  

**Services**:  

**psql-db**:    
**Image**: Specifies the Docker image to be used, which in this case is postgres.  
**Container Name**: Defines the name of the container as psql-db.  
**Restart Policy**: Sets the restart policy to always, ensuring that the container restarts automatically if it stops unexpectedly.  
**Environment Variables**: Configures environment variables required for PostgreSQL:  
**POSTGRES_USER**: Specifies the PostgreSQL username as postgres.  
**POSTGRES_PASSWORD**: Specifies the password for the PostgreSQL user as root.  
**POSTGRES_DB**: Specifies the name of the PostgreSQL database as bcpp.  
**Ports Mapping**: Maps port 5432 of the host machine to port 5432 of the PostgreSQL container, allowing access to the PostgreSQL service from outside the container.  

**bcpp**:  
**Container Name**: Specifies the name of the container as bcpp_app.  
**Build Configuration**: Defines the build context and Dockerfile location for building the Docker image of the bcpp application.  
**Ports Mapping**: Maps port 8081 of the host machine to port 8080 of the bcpp application container, enabling access to the application from outside the container.  
**Environment Variables**: Sets up environment variables required for the Spring Boot application:  
**SPRING_DATASOURCE_URL**: Specifies the JDBC URL to connect to the PostgreSQL database.  
**SPRING_DATASOURCE_USERNAME**: Specifies the username for connecting to the PostgreSQL database as postgres.  
**SPRING_DATASOURCE_PASSWORD**: Specifies the password for connecting to the PostgreSQL database as root.  
**SPRING_JPA_HIBERNATE_DDL_AUTO**: Configures Hibernate to update the database schema automatically (update) based on the entity mappings.  
**Dependencies**: Defines that the bcpp application container depends on the psql-db service, ensuring that the PostgreSQL service is started before the application container.  
This docker-compose.yml file simplifies the deployment and management of the bcpp application and its dependencies by defining them as services within a single configuration file.  

## Author

**Andrej Stjepanović**  
Student at the **Faculty of Technical Sciences** in Novi Sad  
Undergraduate of Software Engineering

