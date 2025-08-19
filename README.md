# PollCreatorAPI

A beginner-friendly Spring Boot REST API for creating, voting on, and viewing polls, built with **Java 21**, Spring Web, Spring Data JPA, H2, and Lombok. The frontend will be vibe-coded using [Lovable](https://lovable.dev/) and added to the `/frontend` directory for a full-stack poll application.

## Features
- Create polls with a question and multiple options.
- Vote on options and view real-time results.
- Lightweight backend with in-memory H2 database.
- Frontend integration via Lovable (to be added).

## Tech Stack
- **Backend**: Java 21, Spring Boot, Spring Web, Spring Data JPA, H2 Database, Lombok
- **Frontend**: Vibe-coded with Lovable (to be added in `/frontend`)
- **Tools**: Maven, Git

## Setup
1. Clone the repo: `git clone https://github.com/yourusername/PollCreatorAPI.git`
2. Navigate to the project: `cd PollCreatorAPI`
3. Ensure JDK 21 is installed.
4. Run the backend: `mvn spring-boot:run`
5. Access the API at `http://localhost:8080/api/polls`
6. Frontend setup instructions will be added after Lovable integration.

## Usage
- **Create a poll**: POST `/api/polls/create` with `{ "question": "Your question", "options": ["Option 1", "Option 2"] }`
- **Vote**: POST `/api/polls/{id}/vote` with `{ "optionId": <id> }`
- **View poll**: GET `/api/polls/{id}`
- **List polls**: GET `/api/polls/all`

## Future Plans
- Add Lovable-generated frontend to `/frontend`.
- Deploy backend to Render or similar.
- Enhance with poll expiration and result percentages.

Built as a weekend project to practice Spring Boot with Java 21 and vibe-coding with Lovable.
