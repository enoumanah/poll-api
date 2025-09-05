# Poll-API

Backend API for a poll creation and management system, built with Java and Spring Boot. It supports creating polls, voting, viewing details/results, and deleting polls, with persistent storage using PostgreSQL. This backend powers the frontend at [poll-pro-hq](https://github.com/enoumanah/poll-pro-hq).

## Features
- Create polls with a question and multiple options (POST /api/polls).
- Vote on poll options and view real-time results (POST /api/polls/{id}/vote).
- List all polls with pagination (GET /api/polls).
- View poll details (GET /api/polls/{id}) and results with percentages (GET /api/polls/{id}/results).
- Delete polls with cascading option removal (DELETE /api/polls/{id}).
- Persistent storage using PostgreSQL.
- Error handling for invalid requests (e.g., 404 for not found, 400 for validation errors).

## Tech Stack
- **Language**: Java 21
- **Framework**: Spring Boot
- **Database**: PostgreSQL (persistent storage)
- **ORM**: Spring Data JPA
- **Dependencies**: Spring Web, Lombok, PostgreSQL Driver
- **Build Tool**: Maven
- **Deployment**: Docker on Render

## API Endpoints
Base URL: `https://poll-api-7doi.onrender.com/api/polls`

- **POST /api/polls**: Create a poll. Body: `{ "question": "string", "options": ["string", "string"] }`. Returns 201.
- **GET /api/polls**: List polls (supports `?page=0&size=2`). Returns 200.
- **GET /api/polls/{id}**: Get poll by ID. Returns 200.
- **POST /api/polls/{id}/vote**: Vote on an option. Body: `{ "optionId": number }`. Returns 200.
- **GET /api/polls/{id}/results**: Get results with percentages. Returns 200.
- **DELETE /api/polls/{id}**: Delete a poll. Returns 204.

## Setup

### Local Development
1. **Prerequisites**:
   - Java 21 (JDK).
   - Maven.
   - PostgreSQL (local server, e.g., via Docker: `docker run -d --name poll-postgres -e POSTGRES_USER=eno -e POSTGRES_PASSWORD=password -e POSTGRES_DB=polls -p 5432:5432 postgres:latest`).
   - Clone: `git clone https://github.com/enoumanah/poll-api.git && cd poll-api`.

2. **Database Setup**:
   - Log in to PostgreSQL: `psql -U postgres`.
   - Run:
     ```sql
     CREATE DATABASE polls;
     CREATE USER eno WITH PASSWORD 'password';
     GRANT ALL PRIVILEGES ON DATABASE polls TO eno;
     Verify: psql -U eno -d polls -h localhost -p 5432.

3. **Configuration**:
   - Update src/main/resources/application-local.properties if needed:
   - properties:
      spring.datasource.url=jdbc:postgresql://localhost:5432/polls
      spring.datasource.username=eno
      spring.datasource.password=password
      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

4. **Run**:
      mvn clean install
      mvn spring-boot:run -Dspring-boot.run.profiles=local
   - API runs at http://localhost:8080.

**Deployment on Render**

- Live URL: https://poll-api-7doi.onrender.com
- Setup:
   1. Create a PostgreSQL instance on Render (free tier, database: polls, user: eno).
   2. Create a Web Service, select this repo, use Docker environment.
   3. Add env vars: SPRING_PROFILES_ACTIVE=prod, DATABASE_URL=<Render PostgreSQL Internal URL>.
   4. Deploy via Dockerfile.


**Testing**

 - Use Postman to test all endpoints (create, list, vote, results, delete).
 - Verify persistence: Create polls, redeploy, check GET /api/polls.
 - Postman collection available in repo (if added).
