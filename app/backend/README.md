# Backend Project Documentation

This README document provides an overview of the folder structure of our backend (Java/Spring) project, explaining the purpose and role of each layer/subfolder.

## Folder Structure

- `config`: This folder contains configuration files for your Spring application. It's where you set up various custom beans, database configurations, security settings, and other application-wide settings.

- `controller`: The controller layer is responsible for handling incoming HTTP requests and defining the API endpoints. Controllers receive requests from clients and pass them to the appropriate service methods. As an application of a good practice, we will try to keep controller as clean as possible since, we want to use controller only for routing the incoming requests.

- `dto` (Data Transfer Objects): This folder is used to define DTO classes, which are simple data structures that represent the data you send and receive from the API. Our DTOs will be often used to transform data between the client and service layer. DTOs are also useful for exchanging data between backend and frontend/mobile.

- `exception`: In this folder, we will define custom exception classes or exception handlers which help to handle and manage exceptions gracefully within our application.

- `model`: The model layer contains our domain objects, which represent the core business entities of our application. These classes typically map to data collections in our MongoDB database.

- `repository`: The repository layer is responsible for database interactions, specifically with MongoDB. It usually includes Spring Data MongoDB repositories or custom data access objects (DAOs) for database operations.

- `service`: This layer contains the business logic of your application. Services receive requests from controllers, perform the necessary operations, and often interact with the repository to retrieve,store and manipulate data.

- `util`: The util folder can contain utility classes or helper methods that are used throughout your application. These classes can include various utility functions, such as date formatting, string manipulation, or other reusable code.

## Database (MongoDB)

- MongoDB is a NoSQL database that stores data in a flexible, schemaless format. It's known for its ability to handle large amounts of unstructured or semi-structured data.
- MongoDB stores data in BSON (Binary JSON - which resembles a JSON structure visually) format and provides high availability and horizontal scalability.

- Spring Data MongoDB is an extension of the Spring Data project that provides seamless integration between Spring applications and the MongoDB database. It offers a repository abstraction for interacting with MongoDB collections, making it easy to work with MongoDB in a Spring application.

## Starting the project
- One can use the `run` button in IntelliJ IDEA or simply use the command line via using below command (assuming user is in the root folder of backend) :

      mvn spring-boot:run

- Needless to say, one must have already installed Java 11 SDK and Maven into his/her system.
