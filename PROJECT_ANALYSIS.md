# Project Analysis

## Repository Layout
- `birthday-view/`: Vue 3 + Vite single-page app for the birthday site UI (guestbook and photo gallery).
- `chb/`: Spring Boot 3 backend API for managing guestbook entries.

## Frontend: `birthday-view`
- **Framework & setup**: Vue 3 with Pinia state management, Vite bundling. Entry mounts `App.vue` with the router at `#app`.【F:birthday-view/src/main.js†L1-L14】
- **Routing**: Two routes—`/` renders the guestbook, `/main` renders the photo gallery and landing content.【F:birthday-view/src/router/index.js†L1-L20】
- **Guestbook UI** (`components/GuestbookView.vue`):
  - Form validates author/message/password and enforces a 300-character limit before posting to the backend.【F:birthday-view/src/components/GuestbookView.vue†L75-L105】
  - List supports newest/oldest sorting, pagination via “더보기,” and highlights the most-liked message with a pin icon.【F:birthday-view/src/components/GuestbookView.vue†L38-L76】【F:birthday-view/src/components/GuestbookView.vue†L115-L177】
  - Likes, edits, and deletes call API endpoints; edit/delete actions require a modal password check.【F:birthday-view/src/components/GuestbookView.vue†L133-L199】
  - Timestamps formatted to Korean locale with Asia/Seoul timezone.【F:birthday-view/src/components/GuestbookView.vue†L190-L199】
- **Photo gallery** (`views/MainView.vue`): Loads/shuffles `public/photos.json`, supports modal viewing with keyboard navigation, responsive pagination (“더보기”), and a click-triggered fireworks effect via DOM particles.【F:birthday-view/src/views/MainView.vue†L1-L151】
- **API client**: `services/guestbookService.js` uses Axios against `import.meta.env.VITE_API_BASE` to call `/messages` endpoints (list/create/like/update/delete). Ensure `VITE_API_BASE` points to the backend origin.【F:birthday-view/src/services/guestbookService.js†L1-L36】

## Backend: `chb`
- **Framework & build**: Spring Boot 3.3 with Java 21, Gradle build, Oracle JDBC driver, JPA, Security (disabled for now), validation, and jasypt support configured in `build.gradle`.【F:chb/build.gradle†L1-L41】
- **Security/CORS**: CSRF disabled, stateless sessions, and all routes permitted. CORS allows configured frontend origins for `/api/**`.【F:chb/src/main/java/com/birthday/chb/config/SecurityConfig.java†L1-L29】【F:chb/src/main/java/com/birthday/chb/config/WebConfig.java†L1-L20】
- **Domain model**: `GuestbookMessage` stores author, message (300-char cap), like count, password, and created/modified timestamps via Hibernate auditing.【F:chb/src/main/java/com/birthday/chb/domain/GuestbookMessage.java†L1-L36】
- **Repository**: `GuestbookMessageRepository` provides creation-time-descending retrieval for list views.【F:chb/src/main/java/com/birthday/chb/repository/GuestbookMessageRepository.java†L1-L10】
- **API endpoints** (`GuestbookController`):
  - `GET /api/messages`: List all entries ordered newest-first.【F:chb/src/main/java/com/birthday/chb/controller/GuestbookController.java†L20-L23】
  - `POST /api/messages`: Create a new entry.【F:chb/src/main/java/com/birthday/chb/controller/GuestbookController.java†L25-L27】
  - `POST /api/messages/{id}/like`: Increment likes.【F:chb/src/main/java/com/birthday/chb/controller/GuestbookController.java†L29-L32】
  - `PUT /api/messages/{id}/update`: Update message text with password verification; 401 on mismatch, 404 if not found.【F:chb/src/main/java/com/birthday/chb/controller/GuestbookController.java†L40-L50】
  - `DELETE /api/messages/{id}/delete`: Delete entry with password verification; 401 on mismatch, 404 if not found.【F:chb/src/main/java/com/birthday/chb/controller/GuestbookController.java†L34-L38】
- **Service logic** (`GuestbookService`): Handles like count mutation, password checks for update/delete, and maps entities to DTO responses. Uses transactional boundaries for persistence updates.【F:chb/src/main/java/com/birthday/chb/service/GuestbookService.java†L1-L62】
- **DTOs**: `GuestbookMessageDto` defines request/update/delete payloads and response projection returning metadata and like counts.【F:chb/src/main/java/com/birthday/chb/dto/GuestbookMessageDto.java†L1-L52】

## Running & Testing
- **Frontend**: `cd birthday-view && npm install && npm run dev` for local dev; `npm run build` to bundle; `npm run test:unit` for Vitest suite.【F:birthday-view/README.md†L12-L28】
- **Backend**: `cd chb && ./gradlew bootRun` to start the API; tests via `./gradlew test`. Java 21 toolchain configured via Gradle.【F:chb/build.gradle†L1-L20】
- **Environment**: Set `VITE_API_BASE` so the frontend Axios client targets the backend’s `/api` origin.【F:birthday-view/src/services/guestbookService.js†L1-L36】
