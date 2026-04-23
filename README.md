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

## How to Run 
Requirements: Docker Desktop

```
git clone <your-repo-url>
cd job-application-tracker
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