# PollCreatorAPI

A simple Spring Boot RESTful API for creating, managing, and voting on polls. This project allows users to create polls with multiple options, cast votes, and view results. It serves as a beginner-friendly backend project to learn Java and Spring Boot, focusing on REST APIs, JPA, and H2 in-memory database.

The frontend is vibe-coded using [Lovable](https://lovable.dev/), a no-code AI platform, to quickly generate an interactive web interface that consumes the API. The frontend code will be added to this repository in a `/frontend` directory for a complete full-stack experience.

## Features
- Create polls with a question and multiple options.
- Vote on poll options and view real-time results.
- Lightweight backend with Spring Boot, Spring Data JPA, H2, and Lombok.
- Planned frontend integration via Lovable for a user-friendly interface.

## Tech Stack
- **Backend**: Java, Spring Boot, Spring Web, Spring Data JPA, H2 Database, Lombok
- **Frontend**: Vibe-coded with Lovable (to be added in `/frontend`)
- **Tools**: Maven, Git

## Setup
1. Clone the repo: `git clone https://github.com/yourusername/PollCreatorAPI.git`
2. Navigate to the project: `cd PollCreatorAPI`
3. Run the backend: `mvn spring-boot:run`
4. Access the API at `http://localhost:8080/api/polls`
5. Frontend setup instructions will be added once the Lovable-generated frontend is included.

## Usage
- **Create a poll**: POST `/api/polls/create` with `{ "question": "Your question", "options": ["Option 1", "Option 2"] }`
- **Vote**: POST `/api/polls/{id}/vote` with `{ "optionId": <id> }`
- **View poll**: GET `/api/polls/{id}`
- **List polls**: GET `/api/polls/all`

## Future Plans
- Add the Lovable-generated frontend to `/frontend`.
- Deploy the backend to a free hosting service (e.g., Render).
- Add poll expiration and result percentage calculations.

Built as a weekend project to practice Spring Boot and vibe-coding with Lovable. Contributions welcome!
