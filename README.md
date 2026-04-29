# Job Application Tracker

## About
A REST API for tracking job applications. 
Users can manage companies and job applications with JWT-based authentication.

## Tech Stack
Java 21, Spring Boot 3.4.4, PostgreSQL, Hibernate/JPA, Spring Security + JWT, 
Flyway, MapStruct, Docker, Swagger/OpenAPI


## Features
- User registration and authentication (JWT)
- CRUD operations for companies
- CRUD operations for job applications
- API documentation via Swagger UI
- Global exception handling with custom exceptions
- Input validation with @Valid
- Unit and integration tests (Mockito, MockMvc)
- Database migrations with Flyway

## Security
- JWT-based stateless authentication
- Token validation via custom JwtAuthFilter on every request
- Invalid/expired token returns 403
- User details loaded from database on each request


## Database
PostgreSQL with Flyway migrations.

Tables: users, companies, applications

Relationships:
- User → many Companies
- Company → many Applications

## Architecture
The application follows a layered architecture:
- **Controller** - handles HTTP requests
- **Service** - business logic
- **Repository** - database access (Spring Data JPA)
- **DTO** - request/response objects (mapped via MapStruct)

Error handling: custom exceptions (NotFoundException, CompanyAlreadyExists)
with GlobalExceptionHandler returning structured HTTP responses.


## Testing
- Unit tests for CompanyService and ApplicationService (Mockito)
- Happy path + all exception scenarios
- Integration tests for CompanyController and ApplicationController (MockMvc)
- 200, 201, 204, 400, 403, 404, 409 scenarios

## How to Run 
Requirements: Docker Desktop

```
git clone https://github.com/caterinaomelc/job-tracker.git
cd job-tracker
docker-compose up --build -d
```

Swagger UI: http://localhost:8080/swagger-ui/index.html


## API Endpoints

### Auth
POST /auth/register - register a new user

POST /auth/login - authenticate and get JWT token

### Companies
GET /companies - get all companies

GET /companies/{id} - get company by id

POST /companies - create a company

PUT /companies/{id} - update a company

DELETE /companies/{id} - delete a company

### Applications
GET /applications - get all applications

GET /companies/{companyId}/applications - get applications by company

POST /companies/{companyId}/applications - add application

PUT /companies/{companyId}/applications/{id} - update application

DELETE /companies/{companyId}/applications/{id} - delete application
