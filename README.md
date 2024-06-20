# Employee Management System Api
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
Main technologies which are used in this project are **Spring Boot**, **Spring Data JPA**, **Spring Security**, for database its **Postgres** and platform for sending APIs **Postman**. For testing I used **Mockito** and **JUnit** and for containerization is **Docker**

## Overview:  
BenefitSeller is a company that issues payment cards for various company benefits. BenefitSeller partners with their customer companies, and issues cards for employees of the customer company.  
Employees can use those cards to pay for various benefits selected by their company.  
There are several categories of benefit merchants (Food and Drinks, Recreation, Education, Culture…) also there are three categories of users(**Standard**, **Premium** and **Platinum**).  
**Standard** users can use benefits only from categories selected by their company.  
**Premium** users can use all benefits from all BenefitSeller merchants.  
**Platinum** users can use all benefits from all BenefitSeller merchants. Additionally, platinum users can achieve discounts for specific merchants selected by their company. This tier of users is especially good for companies that want to use discounts to promote specific products or services in their company.

## Getting Started:  
To start this project you need to have installed next things:  
**IntelliJ IDEA**, **Java 17**, **PostgreSQL** database, **Postman** and **Docker**.

## API Documentation and Postman  
Docs: https://documenter.getpostman.com/view/26096728/2sA3JFBQbq  
Collection and Enviroment: https://github.com/stjepanoviccc/benefit-card-payment-processor/tree/develop/bcpp/src/main/resources.   
There is mini-documentation for using postman and how to set it up inside of the collection.  

## Spring Security:  

In this project I implemented **JWT(Json Web Token)** for Authentication.  
There are three different roles in this app (Standard,Platinum and Premium). Since there isn't a role for the Administrator in specification, Initialization Data is free access and you don’t need to be authorised to do that (this is a testing environment not production).   
For usage of transactions and adding and removing funds from the card I used authorization for any role.  
Also it's important to take note about transactions, there is one endpoint which all users can access but in TransactionServiceImpl, I implemented JwtService for extracting roles and based on that role different ways of processing transactions is called.  
There wasn’t much to do with Spring Security in this project but I still wanted to implement it.  

## Database:  

There are several entities in the database: **User**, **Card**, **Company**, **Merchant**, CompanyCategory (junction), CompanyMerchant (junction), **Discount** and **Transaction**.  
On creation, each user will get one card with a unique card number and will be able to add/remove funds from the card. So there is a one to one mapping here.  
Companies can have multiple users and multiple merchants but also merchants can be connected to multiple companies.   
CompanyCategory is a junction table for identifying which category is connected to a company. This is most important for standard users because they can use only benefits from merchant categories which are connected with their company.  
CompanyMerchant is a junction table for identifying which category is connected to a specific merchant. This is most important for platinum users because they can have a special Discount for that merchant.  
Transaction entity is persisting all transactions that are happening in the app and it doesn’t mean it is successful or unsuccessful.  

## Error Handling:  

For Error Handling I used SpringBoot’s global exception mechanism. I did setup for more exceptions but in this project I was using only these two(NotFoundException, BadRequestException).  
**Global Exception Handling**: Spring Boot's global exception handling mechanism is employed to manage and respond to exceptions effectively throughout the application. This ensures consistency and reliability in handling various types of errors  .
**NotFoundException**: For model-related errors, such as when a requested resource is not found, the application utilizes a custom NotFoundException. This exception is thrown when attempting to access a resource that does not exist, providing clear feedback to the client.  
**BadRequestException**: In cases of technical errors or invalid requests, the application employs BadRequestException. This exception is used to indicate problems with the client's request, such as malformed input or missing parameters. By utilizing this exception, the application can provide meaningful error messages and guide clients towards resolving their requests.

## Testing:  

I've implemented over 30 unit tests across all services in this project.  
These unit tests are designed to ensure the functionality of individual components in isolation.  
Mockito is used to mock dependencies, allowing us to isolate the units under test.

## Deploy:  

### Dockerfile  
**Base Image**: This Dockerfile starts by using the OpenJDK 17 base image as the foundation for the application.  
**Volume Configuration**: It sets up a volume at /tmp within the container to store temporary files.  
**Exposed Port**: The Dockerfile exposes port 8080, which is the port on which the Spring Boot application will run.  
**Application Copy**: It copies the compiled JAR file bcpp-0.0.1-SNAPSHOT.jar from the target directory of the application to the root directory of the Docker image, renaming it to bcpp.jar.  
**Entry Point**: Finally, it specifies the command to execute when the Docker container starts, which is to run the Java application using the java -jar command and passing the path to the JAR file (/bcpp.jar). 
This Dockerfile simplifies the deployment process of the bcpp application by encapsulating it into a Docker image, making it easier to manage and deploy across different environments.  

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

**START APP**:  
It's important to make sure you have existing database source in *application.properties* so .jar file can be builded (it's because of jpa) so it's easiest for you to just manually create database bcpp_db in pgAdmin/any other dashboard and you don't need to configure anything else but if you want you can connect to any database because it will be overwritten with docker-compose.yaml ...  
1. clone project  
2. mvn clean  
3. mvn package  
1. navigate to root folder  
2. use next command in terminal: **docker build . -t bcpp_v1**  
3. use next command in terminal: **docker-compose up --build**  
4. open postman collection provided in src/main/resources and set up enviroment  
5. start sending requests from postman and responses will be created DTOs (you have documentation about postman)...  
6. (optional): to check database in docker use next command in docker terminal:  **psql -h localhost -p 5432 -U postgres bcpp**  

## Author

**Andrej Stjepanović**  
Student at the **Faculty of Technical Sciences** in Novi Sad  
Undergraduate of Software Engineering

