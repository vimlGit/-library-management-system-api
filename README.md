📚 Library Management System API

This is a Library Management System Backend developed using Spring Boot REST API with JWT-based authentication and authorization.
It provides secure endpoints for managing library operations with two roles: Librarian and Student.

🚀 Tech Stack
Spring Boot

Spring Security with JWT

PostgreSQL

Swagger (OpenAPI) for API documentation and testing

JPA (Hibernate)

Lombok

🔐 Roles
Librarian: Can manage books and issue operations.

Student: Can view available books and check their issued books.

🏗️ Project Structure
sql
Copy
Edit
src/main/java/com/vk/
├── controller          --> REST Controllers
│   └── LibraryController.java
├── dto                 --> Data Transfer Objects
├── entity              --> JPA Entities
├── filter              --> JWT Authentication Filter
├── repository          --> Spring Data JPA Repositories
├── securityconfig      --> Spring Security Configuration
├── service             --> Service Layer
├── swaggerconfig       --> Swagger Configuration
├── util                --> Utility classes (e.g., JWT utility)
└── SbRestApiLibraryManagementProj04Application.java
📡 API Endpoints & Access Control
📝 Public Endpoints (No authentication)
Method	Endpoint	Description
POST	/registerUser	Register new user
POST	/login	Authenticate user & generate JWT

🛡️ Secured Endpoints
✅ Accessible by ROLE_LIBRARIAN:
Method	Endpoint	Description
POST	/submitBookAndCalculateChagres/{bookId}	Submit book and calculate charges
POST	/addBookInLibrary	Add a new book to the library
POST	/saveBookIssuedDetails	Save issued book details
POST	/addMoreBooksOnExisting/{bookId}	Add more copies to an existing book

✅ Accessible by ROLE_STUDENT & ROLE_LIBRARIAN:
Method	Endpoint	Description
GET	/courseValidity	Check course validity
GET	/searchBook	Search for a book
GET	/showAllBook	View all available books

✅ All other endpoints require authentication with JWT token.
  Pass the JWT token in the Authorization header:


Authorization: Bearer <token>
📝 Swagger UI
Access Swagger UI at:

bash
Copy
Edit
http://localhost:8080/swagger-ui/
You can test all endpoints and pass JWT tokens directly from the Swagger UI.

🗄️ Database
This project uses PostgreSQL as the database.

Example application.properties:
properties
Copy
Edit
spring.datasource.url=jdbc:postgresql://localhost:5432/librarydb
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
🏃‍♂️ Running the Application
Clone the repository

Configure PostgreSQL in application.properties

Run the application using:

bash
Copy
Edit
mvn spring-boot:run
✅ Key Features
JWT token-based login

Role-based access control (Librarian vs Student)

Secure REST APIs

Swagger for testing APIs

PostgreSQL integration

Modular code structure (Controller-Service-Repository)

🙌 Future Enhancements (Optional Ideas)
Refresh token implementation

Pagination & sorting in book list API

Email notifications for issued books
