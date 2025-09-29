# PollCreatorAPI

PollCreatorAPI is a robust, production-ready Spring Boot application designed for creating, managing, and voting on polls with MongoDB persistence. It supports user authentication via JWT, public/private poll visibility with shareable links, and enforces a one-vote-per-user-per-poll rule. Paired with a modern **React frontend** (built using Lovable), the app offers a seamless user experience with dark mode, smooth animations, and responsive design. Deployed on Render with MongoDB Atlas, it ensures data persistence and scalability for real-world use.

## Features

- **User Authentication**: Secure registration and login using JWT for API access.
- **Poll Creation**: Create polls with a question, 2–10 options, and public/private visibility settings.
- **Voting System**: Enforces one vote per user per poll, tracked via user ID.
- **Public/Private Polls**: Public polls are accessible to all; private polls use unique shareable UUID links.
- **Poll Management**: Owners can delete their polls; results display vote counts and percentages.
- **MongoDB Persistence**: Data persists across app restarts using MongoDB Atlas.
- **CORS Support**: Configured for integration with the React frontend.
- **Modern Frontend (Built with Lovable)**: A responsive React app with dark mode, Framer Motion animations, and a polished landing page for user engagement.
- **Production-Ready**: Deployed on Render with Docker, using environment variables for secure configuration.

## Tech Stack

### Backend
- **Framework**: Spring Boot 3.5.4, Java 21
- **Database**: MongoDB (Atlas)
- **Authentication**: Spring Security with JWT
- **Dependencies**: Spring Web, Spring Data MongoDB, JJWT, Lombok
- **Deployment**: Render (Docker-based)
- **Build Tool**: Maven

### Frontend (Partly Built with Lovable)
- **Framework**: React (Vite)
- **Styling**: Tailwind CSS with dark mode support
- **Routing**: React Router
- **Animations**: Framer Motion for smooth transitions
- **Notifications**: React Toastify for user feedback
- **Deployment**: Vercel

## Demo

[https://www.loom.com/share/f07998fabd094c04908cb9c1ce4e3a61?sid=421fdc82-6c8f-4a20-8441-f79daf5b26fc]

## Prerequisites

- **Java 21**: Install JDK 21 (e.g., Eclipse Temurin).
- **MongoDB Atlas**: Set up a cluster (database `polls` auto-created on first insert).
- **Maven**: For building and running the backend.
- **Node.js**: For running the frontend (v18+ recommended).
- **Postman**: For API testing.
- **Render/Vercel Accounts**: For backend and frontend deployment.

## Setup and Installation

### Backend (poll-api)

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/enoumanah/poll-api.git
   cd poll-api
   ```

2. **Configure Environment**:
   - Update `src/main/resources/application.properties` (local):
     ```properties
     spring.application.name=PollCreatorAPI
     spring.profiles.active=local
     spring.data.mongodb.uri=mongodb+srv://eno:<your-mongodb-password>@cluster0.qev4xg5.mongodb.net/polls?retryWrites=true&w=majority
     spring.data.mongodb.database=polls
     spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
     app.jwt.secret=<your-strong-secret-key>
     app.jwt.expiration=86400000
     app.cors.allowed-origins=*
     logging.level.org.springframework.security=DEBUG
     logging.level.org.springframework.data.mongodb=DEBUG
     ```
     - Replace `<your-mongodb-password>` and `<your-strong-secret-key>` (64-char random key, e.g., from https://randomkeygen.com/).
   - For production (`src/main/resources/application-prod.properties`):
     ```properties
     spring.data.mongodb.uri=${MONGODB_URI}
     app.jwt.secret=${JWT_SECRET}
     ```

3. **Build and Run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   - API runs on `http://localhost:8080`.

4. **Test API**:
   - Use Postman:
     - `POST /api/auth/register`: `{ "username": "test", "password": "pass", "email": "test@example.com" }`
     - `POST /api/auth/login`: `{ "username": "test", "password": "pass" }` → Get JWT
     - `POST /api/polls` (with `Authorization: Bearer <jwt>`): `{ "question": "Favorite color?", "options": ["Red", "Blue"], "visibility": "public" }`
   - Verify data in MongoDB Atlas (`polls`, `options`, `votes`, `users` collections).

### Frontend (pollhub, Partly Built with Lovable)

1. **Clone the Frontend Repository**:
   ```bash
   git clone https://github.com/enoumanah/pollhub.git
   cd pollhub
   ```

2. **Install Dependencies**:
   ```bash
   npm install
   ```

3. **Configure Environment**:
   - Create `.env` in the root:
     ```env
     VITE_API_URL=https://poll-api-7doi.onrender.com/api
     ```
   - For local dev, use `VITE_API_URL=http://localhost:8080/api`.

4. **Run Locally**:
   ```bash
   npm run dev
   ```
   - Frontend runs on `http://localhost:5173`.

5. **Features (Partly Built with Lovable)**:
   - **Landing Page (/) (Public)**: Hero with "Create Polls in Seconds", "Get Started" (to /login), and feature cards (poll creation, voting, sharing). Smooth fade-in animations via Framer Motion.
   - **Login Page (/login)**: Form for username/password, stores JWT in localStorage, redirects to /dashboard.
   - **Dashboard (/dashboard, Protected)**: Lists user’s polls with delete buttons, links to /create, and shows "No polls" prompt if empty.
   - **Create Poll (/create, Protected)**: Form for question, options, and visibility (public/private). Displays shareable link for private polls.
   - **Poll View (/polls/:id or /share/:shareLink)**: Shows poll question, options, vote button (disabled if already voted), and results with percentages.
   - **Dark Mode**: Toggle in navbar/footer, persists via localStorage, uses Tailwind’s `dark` class.
   - **Animations**: Framer Motion for page transitions (fade-ins), button hovers, and poll card entrances.

6. **Deploy to Vercel**:
   - Push to GitHub:
     ```bash
     git add .
     git commit -m "Updated frontend with auth and poll features"
     git push origin main
     ```
   - In Vercel dashboard:
     - Create a new project, link to `pollhub` repo.
     - Set env var: `VITE_API_URL=https://poll-api-7doi.onrender.com/api`.
     - Deploy and verify (e.g., `https://poll-pro-hq.vercel.app`).

## API Endpoints

| Method | Endpoint                     | Description                          | Auth Required |
|--------|------------------------------|--------------------------------------|---------------|
| POST   | `/api/auth/register`         | Register a user                      | No            |
| POST   | `/api/auth/login`            | Login and get JWT                    | No            |
| POST   | `/api/polls`                 | Create a poll                        | Yes           |
| POST   | `/api/polls/{id}/vote`       | Vote on a poll option                | Yes           |
| GET    | `/api/polls`                 | List public polls                    | No            |
| GET    | `/api/polls/{id}`            | Get a poll (private needs auth)      | Yes (private) |
| GET    | `/api/polls/share/{shareLink}` | Access private poll via link        | No            |
| GET    | `/api/polls/{id}/results`    | Get poll results                     | No            |
| DELETE | `/api/polls/{id}`            | Delete a poll (owner only)           | Yes           |

## Project Structure

### Backend (poll-api)
```
poll-api/
├── src/main/java/com/enoumanah/pollcreator/poll_api/
│   ├── config/          # SecurityConfig, JwtAuthenticationFilter
│   ├── controller/      # AuthController, PollController
│   ├── dto/            # CreatePollRequest, PollResponse, etc.
│   ├── exception/       # PollNotFoundException, etc.
│   ├── model/          # Poll, Option, Vote, User
│   ├── repository/     # MongoDB repositories
│   ├── service/        # PollService, AuthService
├── src/main/resources/
│   ├── application.properties        # Local config
│   ├── application-prod.properties   # Production config
├── Dockerfile           # Render deployment
├── pom.xml             # Maven dependencies
```

### Frontend (poll-pro-hq, Built with Lovable)
```
pollhub/
├── src/
│   ├── components/     # Navbar, Footer, PollCard, etc.
│   ├── pages/         # Landing, Login, Dashboard, CreatePoll
│   ├── services/      # api.js for fetch calls
│   ├── App.jsx        # Routes and layout
├── .env               # VITE_API_URL
├── package.json       # Dependencies (react-router-dom, framer-motion, react-toastify)
├── tailwind.config.js # Tailwind CSS config
├── vite.config.js     # Vite config
```

## Troubleshooting

- **Backend Fails to Start**:
  - **Error**: "Failed to configure a DataSource":
    - Ensure `spring-boot-starter-data-jpa` is removed from `pom.xml`.
    - Verify `spring.autoconfigure.exclude` in `application.properties`.
    - Run `mvn dependency:tree` to check for JPA dependencies.
  - **MongoDB Connection**: Update Atlas Network Access to "Allow Anywhere" or add your IP.
  - Run with debug: `mvn spring-boot:run -Dspring-boot.run.arguments=--debug`.

- **Frontend Issues**:
  - Ensure `VITE_API_URL` matches backend URL (local or Render).
  - Check browser console for CORS errors (update `app.cors.allowed-origins` if needed).
  - Verify JWT is sent in `Authorization: Bearer` header for protected routes.

- **Render Deployment**:
  - Check logs for MongoDB connection errors (wrong `MONGODB_URI` or password).
  - Ensure env vars (`SPRING_PROFILES_ACTIVE`, `MONGODB_URI`, `JWT_SECRET`) are set.
  - See Render’s [troubleshooting guide](https://render.com/docs/troubleshooting-deploys).

## Future Improvements

- Add Swagger API docs (`springdoc-openapi-starter-webmvc-ui`).
- Implement poll expiration dates.
- Add email verification for users.
- Enhance frontend with real-time poll updates (e.g., WebSocket).
- Set up GitHub Actions for CI/CD.

## Contributing

1. Fork the repo.
2. Create a feature branch (`git checkout -b feature/new-feature`).
3. Commit changes (`git commit -m "Add new feature"`).
4. Push to the branch (`git push origin feature/new-feature`).
5. Open a Pull Request.

## License

MIT License. See [LICENSE](LICENSE) for details.

---

Built by Eno. Connect on [Linkdin](https://www.linkedin.com/in/eno-umanah) or [email](mailto:umanaheno10@gmail.com).
