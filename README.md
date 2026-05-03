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

Some `working on` packages contain planned modules such as companies, reviews, messages, job alerts, and interviews. They are a good roadmap topic, but the core production flow is the user/job/application/resume/security flow.

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
| Companies | `GET /api/companies` | Public |
| Company reviews | `GET /api/company-reviews/company/{companyId}` | Public |
| Job alerts | `GET /api/job-alerts/me` | USER |
| Messages | `GET /api/messages/inbox` | Authenticated roles |
| Interviews | `GET /api/interviews/me` | Authenticated roles |
| Admin users | `GET /admin/users` | ADMIN |
| Admin create job | `POST /admin/jobs` | ADMIN |
| Admin applications | `GET /admin/applications` | ADMIN |
| Send email | `POST /email/send` | Authenticated roles |

## Security Design

- Passwords are verified through Spring Security authentication.
- Tokens are signed with HMAC using `JWT_SECRET`.
- JWT expiry defaults to one hour and can be changed with `JWT_EXPIRATION_MS`.
- The app is stateless: no server-side session is required.
- CORS allows local frontend ports and optional deployed frontend origins.
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

## Future Enhancements

- Add integration tests for auth, job application, and recruiter workflows.
- Replace `ddl-auto` with database migrations.
- Add refresh tokens.
- Move planned modules out of `working on` folders into normal package names when they are ready.
- Add file upload storage for real resume files instead of path-based upload.
- Add pagination to recruiter/admin application lists.
