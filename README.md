# Job Portal Backend

Spring Boot REST API for a full-stack Job Portal. The backend supports candidates, recruiters, company accounts, company employees, and admins. It handles authentication, job discovery, job posting, applications, saved jobs, resumes, profile management, company portals, interviews, messaging, job alerts, email sending, and role-based dashboards.

## Interview Pitch

This backend is built as a layered Spring Boot application. Controllers expose REST endpoints, services contain business logic, repositories handle persistence through Spring Data JPA, DTOs keep API payloads separate from database entities, and Spring Security protects routes with JWT authentication.

In interviews, explain it as:

> "I built a job portal backend with Spring Boot, Spring Security, JWT, JPA, and MySQL. The system has candidate, recruiter, and admin flows. Candidates can register, log in, browse jobs, save jobs, apply, manage resumes, and update profiles. Recruiters can create jobs, view applications for their jobs, update application status, and search talent. Admins can manage users, jobs, and applications. I used role-based authorization, DTO mapping, a global exception handler, Swagger documentation, and a frontend-friendly CORS/proxy setup."

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Java 25 |
| Framework | Spring Boot 3.5.4 |
| API | Spring Web REST |
| Security | Spring Security, JWT |
| Database | MySQL with Spring Data JPA/Hibernate |
| Validation | Spring Boot Validation |
| Email | Spring Mail / SMTP |
| API Docs | Springdoc OpenAPI / Swagger UI |
| Build | Maven Wrapper |
| Deployment Support | Dockerfiles, Docker Compose |

## Main Features

- JWT login with stateless Spring Security.
- Role-based access for `USER`, `RECRUITER`, and `ADMIN`.
- Company role access for `COMPANY_ADMIN` and `COMPANY_EMPLOYEE`.
- Candidate job browsing, filtering, sorting, saving, applying, and resume management.
- Recruiter job ownership, applicant tracking, status updates, and talent search.
- Company signup, company profile management, employee accounts, and company-owned jobs.
- Admin user management, job management, and application overview.
- Forgot-password flow using expiring reset tokens and email reset links.
- Role-aware profile insights for candidate, recruiter, and admin dashboards.
- Companies, company reviews, interviews, messages, and job alerts promoted from drafts into active APIs.
- DTO and mapper layer to avoid exposing entities directly.
- Global exception handling with consistent JSON error responses.
- Configurable CORS for local and deployed frontends.
- Swagger UI enabled outside the `prod` Spring profile.
- Configurable JWT secret and expiration through environment variables.

## Project Structure

```text
src/main/java/com/pravin/job_portal_backend
|-- config/          Spring Security and CORS configuration
|-- controller/      REST endpoints grouped by feature and role
|-- dto/             Request and response objects used by the API
|-- entity/          JPA entities mapped to database tables
|-- enums/           Role, status, job type, and experience enums
|-- exception/       Global and custom exception handling
|-- filter/          JWT request filter
|-- mapper/          Entity-to-DTO conversion helpers
|-- repository/      Spring Data JPA repositories
|-- service/         Service interfaces and implementations
|-- utilis/          JWT helper utilities
```

The active API modules cover authentication, jobs, applications, profiles, saved jobs, resumes, company portals, recruiters, admins, messages, interviews, alerts, reviews, and email.

## Documentation

| File | Purpose |
| --- | --- |
| `README.md` | Setup, architecture, and interview-ready overview |
| `docs/API_REFERENCE.md` | Role-based endpoint reference and application payload |
| `docs/database.md` | Database table notes and schema explanation |

## Architecture Flow

1. A user signs up or logs in through `/public/signup` or `/public/login`.
2. On successful login, the backend returns a JWT containing the username, user id, and roles.
3. The frontend sends the token in the `Authorization: Bearer <token>` header.
4. `JwtFilter` validates the token on every protected request and sets Spring Security authentication.
5. `SecurityConfig` checks the requested endpoint against role rules.
6. Controllers call services, services call repositories, and DTOs are returned to the frontend.

## Environment Variables

| Variable | Required | Purpose |
| --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | No | Defaults to `pravin` |
| `PORT` | No | Defaults to `8080` |
| `SPRING_DATASOURCE_USERNAME` | Yes | Database username |
| `SPRING_DATASOURCE_PASSWORD` | Yes | Database password |
| `SMTP_USERNAME` | Yes, for email | Gmail/SMTP username |
| `SMTP_PASSWORD` | Yes, for email | Gmail/SMTP app password |
| `JWT_SECRET` | Recommended | JWT signing secret, at least 32 characters |
| `JWT_EXPIRATION_MS` | No | Defaults to `3600000` milliseconds |
| `PASSWORD_RESET_EXPIRY_MINUTES` | No | Defaults to `30` minutes |
| `FRONTEND_URL` | No | One allowed frontend origin |
| `FRONTEND_URLS` | No | Comma-separated allowed frontend origins |

Local defaults are defined in `src/main/resources/application.yml` and `application-pravin.yml`.

## Local Setup

1. Create a MySQL database:

```sql
CREATE DATABASE jobapp;
```

2. Set environment variables:

```powershell
$env:SPRING_DATASOURCE_USERNAME="root"
$env:SPRING_DATASOURCE_PASSWORD="your_password"
$env:SMTP_USERNAME="your_email@gmail.com"
$env:SMTP_PASSWORD="your_app_password"
$env:JWT_SECRET="replace-this-with-a-long-secret-key"
```

3. Run the backend:

```powershell
.\mvnw.cmd spring-boot:run
```

The API runs at:

```text
http://localhost:8080
```

Swagger UI, when not using the `prod` profile:

```text
http://localhost:8080/swagger-ui.html
```

## Useful Commands

```powershell
.\mvnw.cmd test
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

## Important API Endpoints

| Feature | Method and Path | Access |
| --- | --- | --- |
| Health check | `GET /public/health-check` | Public |
| Signup | `POST /public/signup` | Public |
| Company signup | `POST /public/company/signup` | Public |
| Login | `POST /public/login` | Public |
| Forgot password | `POST /public/password/forgot` | Public |
| Reset password | `POST /public/password/reset` | Public |
| Public jobs | `GET /public/jobs/paginated` | Public |
| Candidate jobs | `GET /user/jobs` | USER, ADMIN |
| Job details | `GET /user/jobs/{id}` | USER, ADMIN |
| Apply to job | `POST /apply/applications/apply?jobId={id}` | USER |
| My applications | `GET /apply/applications/my-applied-dto` | USER |
| Cancel application | `DELETE /apply/applications/cancel?jobId={id}` | USER |
| Saved jobs | `GET /api/saved-jobs/user/{userId}` | USER |
| Save job | `POST /api/saved-jobs/save?userId={id}&jobId={id}` | USER |
| Upload resume | `POST /api/resumes/upload` | USER |
| Profile | `GET /role-profile/get-profile` | USER, RECRUITER, ADMIN |
| Update profile | `PUT /role-profile/update-profile` | USER, RECRUITER, ADMIN |
| Profile insights | `GET /api/profile-insights/me` | USER, RECRUITER, ADMIN |
| Recruiter jobs | `GET /recruiter/jobs` | RECRUITER |
| Recruiter create job | `POST /recruiter/jobs` | RECRUITER |
| Recruiter applications | `GET /recruiter/applications` | RECRUITER |
| Recruiter talent search | `GET /recruiter/talent` | RECRUITER |
| Company dashboard | `GET /company-portal/dashboard` | COMPANY_ADMIN, COMPANY_EMPLOYEE |
| Company profile | `GET /company-portal/company` | COMPANY_ADMIN, COMPANY_EMPLOYEE |
| Company employees | `GET /company-portal/employees` | COMPANY_ADMIN |
| Company jobs | `GET /company-portal/jobs` | COMPANY_ADMIN, COMPANY_EMPLOYEE |
| Create company job | `POST /company-portal/jobs` | COMPANY_ADMIN, COMPANY_EMPLOYEE |
| Update company job | `PUT /company-portal/jobs/{id}` | COMPANY_ADMIN, COMPANY_EMPLOYEE |
| Delete company job | `DELETE /company-portal/jobs/{id}` | COMPANY_ADMIN, COMPANY_EMPLOYEE |
| Companies | `GET /api/companies` | Public |
| Company reviews | `GET /api/company-reviews/company/{companyId}` | Public |
| Job alerts | `GET /api/job-alerts/me` | USER |
| Messages | `GET /api/messages/inbox` | Authenticated roles |
| Interviews | `GET /api/interviews/me` | Authenticated roles |
| Admin users | `GET /admin/users` | ADMIN |
| Admin jobs | `GET /admin/jobs` | ADMIN |
| Admin create job | `POST /admin/jobs` | ADMIN |
| Admin applications | `GET /admin/applications` | ADMIN |
| Send email | `POST /email/send` | Authenticated roles |

## Security Design

- Passwords are verified through Spring Security authentication.
- Tokens are signed with HMAC using `JWT_SECRET`.
- JWT expiry defaults to one hour and can be changed with `JWT_EXPIRATION_MS`.
- The app is stateless: no server-side session is required.
- CORS allows local frontend ports and optional deployed frontend origins.
- CORS allows `GET`, `POST`, `PUT`, `PATCH`, `DELETE`, and `OPTIONS`.
- Swagger endpoints are blocked when the `prod` profile is active.

## Database Notes

The main entities are:

- `User`
- `Job`
- `ApplyJob`
- `SavedJob`
- `Resume`
- `EmailRequest`

The current local profile uses:

```yaml
spring.jpa.hibernate.ddl-auto: create
```

That recreates tables during startup. For persistent local work or deployment, change it to:

```yaml
spring.jpa.hibernate.ddl-auto: update
```

For production, prefer migrations such as Flyway or Liquibase.

## How To Explain The Backend In Interviews

Focus on these points:

- "I separated responsibilities into controller, service, repository, DTO, mapper, and entity layers."
- "I used JWT because the frontend is a separate SPA and the backend should stay stateless."
- "I modelled three real product roles: candidate, recruiter, and admin."
- "I protected endpoints with role-based authorization in `SecurityConfig`."
- "I kept recruiter actions scoped to the authenticated recruiter's own jobs."
- "I used DTOs so the API contract is independent from database internals."
- "I added global exception handling so errors follow a consistent response shape."
- "I used Swagger UI so the API can be tested and explained quickly."

## Things You Can Implement Next

Use this as a backend roadmap. These ideas are ordered from most useful to more advanced.

### High Priority

- Replace `spring.jpa.hibernate.ddl-auto: create` with `update` for local persistence, then move to Flyway or Liquibase migrations.
- Add integration tests for auth, job application, company job posting, recruiter review, and admin management.
- Add pagination and filtering to admin and recruiter application endpoints.
- Add structured validation messages for all request DTOs.
- Add consistent response DTOs for success and error messages.
- Add refresh tokens or a token renewal endpoint so users are not forced to log in after every JWT expiry.

### Candidate Features

- Add real resume file upload using multipart requests and storage on disk, S3, Cloudinary, or another storage provider.
- Add application edit restrictions based on status, for example editable only while `APPLIED`.
- Add duplicate application prevention responses as proper HTTP conflict responses.
- Add job recommendation APIs based on skills, location, experience level, and saved jobs.
- Add job alert emails when new matching jobs are posted.

### Recruiter Features

- Add application status history so every status change is tracked.
- Add recruiter remarks and private notes history.
- Add interview scheduling with date, time, meeting link, and status.
- Add recruiter analytics such as applications per job, shortlist ratio, and hiring pipeline counts.
- Add recruiter team support if multiple recruiters manage the same company.

### Company Features

- Add company job update and delete audit logging.
- Add company verification workflow managed by admins.
- Add company public profile endpoint with jobs, reviews, and summary stats.
- Add employee permission levels beyond `COMPANY_ADMIN` and `COMPANY_EMPLOYEE`.
- Add company-level analytics for jobs, applications, interviews, and hires.

### Admin Features

- Add admin company management endpoints.
- Add soft delete for jobs, applications, companies, and resumes where audit history matters.
- Add admin audit logs for sensitive actions.
- Add platform analytics endpoints for total users, active jobs, applications, companies, recruiters, and hires.
- Add moderation endpoints for company reviews and reported jobs.

### Security And Production Ideas

- Add email verification for new users.
- Add rate limiting for login, signup, password reset, and email endpoints.
- Add account lockout after repeated failed login attempts.
- Move secrets fully to environment variables or a secret manager.
- Add `prod` profile database and mail settings.
- Add stricter CORS configuration for production deployments.
- Add OpenAPI examples for important request and response bodies.

### Data Model Ideas

- Add status history table for applications.
- Add normalized skills table instead of storing skills only as strings.
- Add job view/impression tracking.
- Add notification table for in-app alerts.
- Add saved search table for candidate filters.
- Add salary min/max fields while keeping the current salary text for display.

### API Polish Ideas

- Add API versioning such as `/api/v1/...` before the project grows further.
- Add consistent pagination response DTOs with `content`, `page`, `size`, `totalPages`, and `totalElements`.
- Add sorting support to more list endpoints, especially users, jobs, applications, companies, and reviews.
- Add reusable request/response wrappers for messages like create, update, and delete success.
- Add OpenAPI tags per portal: Public, Candidate, Recruiter, Company, Admin, Auth.
- Add sample request and response examples in Swagger for login, apply job, create job, and reset password.
- Add validation groups for create vs update DTOs so partial updates are easier to manage.
- Return better HTTP statuses: `201 Created`, `204 No Content`, `400 Bad Request`, `403 Forbidden`, `404 Not Found`, and `409 Conflict`.

### Search And Discovery Ideas

- Add full-text search for job title, company, description, category, and requirements.
- Add combined filters for location, salary range, job type, experience level, status, and company.
- Add "similar jobs" endpoint based on category, skills, and location.
- Add "latest jobs" and "featured jobs" endpoints for the homepage.
- Add popular company and popular category endpoints.
- Add saved search subscriptions for candidates.
- Add recruiter talent search pagination and sorting.
- Add database indexes for common filters such as job status, category, location, company, and posted date.

### Notification Ideas

- Add in-app notification records for application submitted, status changed, interview scheduled, and message received.
- Add email templates for welcome email, application confirmation, interview invite, password reset, and application status update.
- Add async email sending so API responses are not blocked by SMTP delays.
- Add retry handling for failed emails.
- Add notification preferences for candidates and recruiters.
- Add unread notification count endpoint for the frontend header.

### File And Resume Ideas

- Add multipart upload endpoint for resumes instead of accepting only `filePath`.
- Store resume metadata such as original filename, file size, content type, and storage key.
- Add resume download endpoint with access checks.
- Add resume delete ownership checks.
- Add support for multiple resumes and a default resume.
- Add file type validation for PDF/DOC/DOCX.
- Add antivirus or file scanning hook for production-grade uploads.

### Observability Ideas

- Add structured logging with request id, user email, role, endpoint, and response status.
- Add request correlation id middleware/filter.
- Add actuator health details for database and mail connectivity.
- Add metrics for login attempts, job applications, job postings, and email failures.
- Add slow query logging for search and dashboard endpoints.
- Add centralized exception logging without leaking secrets or passwords.
- Add audit logs for admin and company actions.

### Performance Ideas

- Add pagination to every endpoint that can return many records.
- Avoid returning nested entity graphs from controllers; keep using DTOs.
- Add fetch joins or custom repository queries where lazy loading causes repeated queries.
- Add caching for public company lists, categories, and homepage stats.
- Add database indexes for foreign keys and frequent search fields.
- Add batch queries for dashboard count endpoints.
- Add async processing for analytics updates and email sending.

### DevOps And Deployment Ideas

- Add `.env.example` with required backend environment variables.
- Add separate profiles for `dev`, `test`, and `prod`.
- Add Docker Compose services for backend, frontend, and MySQL/PostgreSQL.
- Add CI checks for `mvn test`, frontend lint, and frontend build.
- Add production Dockerfile with smaller runtime image.
- Add health check endpoint usage in Docker Compose.
- Add database backup and restore notes.
- Add deployment notes for Render, Railway, AWS, Azure, or a VPS.

### Testing Ideas

- Add unit tests for services such as `JobApplyServiceImpl`, `CompanyPortalServiceImpl`, and `PasswordResetServiceImpl`.
- Add controller tests for auth, jobs, applications, recruiter, company, and admin endpoints.
- Add repository tests for custom search and ownership queries.
- Add security tests to verify each role can access only its own portal routes.
- Add duplicate application test.
- Add company job ownership tests.
- Add recruiter job ownership tests.
- Add password reset token expiry tests.
- Add test fixtures/builders for users, jobs, companies, and applications.

### Advanced Product Ideas

- Add chat or threaded messaging between candidate and recruiter.
- Add interview feedback forms.
- Add offer management after a candidate is hired.
- Add saved candidates for recruiters.
- Add job drafts and scheduled publishing.
- Add company review ratings summary.
- Add admin moderation for suspicious jobs.
- Add candidate profile visibility settings.
- Add recruiter subscription or plan limits for posted jobs.
- Add data export endpoints for admins.
