# Job Portal Backend Interview Guide

## Simple Architecture Story

This backend follows a standard Spring Boot layered architecture:

1. **Controller layer** receives HTTP requests and returns HTTP responses.
2. **Service layer** contains business rules such as applying for a job, checking ownership, updating status, and preventing duplicate applications.
3. **Repository layer** talks to the database using Spring Data JPA.
4. **Entity classes** represent database tables.
5. **DTO classes** define the request and response shapes exposed by the API.
6. **Mapper classes** convert between entities and DTOs.

The main idea is separation of responsibility: controllers stay thin, services own logic, repositories own persistence, and mappers keep API models separate from database models.

## How To Explain Controllers

Controllers are the API entry points.

Example: `JobApplyController`

- Reads the logged-in user from Spring Security `Authentication`.
- Accepts request data such as `jobId` and `ApplyJobRequestDTO`.
- Calls `JobApplyService`.
- Returns the service result using `ResponseEntity`.

Interview line:

> I kept controllers simple. They only handle routing, authentication context, request parameters, and response status. All business logic is pushed into services.

## How To Explain Services

Services contain business decisions.

Example: `JobApplyServiceImpl`

- Loads the user and job from repositories.
- Checks whether the user already applied.
- Creates the `ApplyJob` entity.
- Applies optional request fields like resume link, cover letter, and expected salary.
- Saves the application.
- Checks ownership before user or recruiter updates.

Interview line:

> The service layer is where I enforce rules. For example, users can only update their own applications, recruiters can only manage applications for jobs they posted, and duplicate applications are blocked.

## How To Explain DTOs

DTOs are API models.

- `ApplyJobRequestDTO` is used when a user applies for a job.
- `ApplyJobDto` is a richer response that includes application, user, and job details.
- `ApplyJobResponseDTO` is shaped for the user's applied-jobs screen.
- `JobDto` exposes job details without exposing the full database entity.

Interview line:

> DTOs protect the API from exposing entities directly. They also let the frontend receive exactly the fields it needs.

## How To Explain Mappers

Mappers convert between entity objects and DTO objects.

Example: `ApplyJobMapper`

- Takes an `ApplyJob` entity.
- Copies simple application fields.
- Uses `UserMapper` for applicant details.
- Uses `JobMapper` for job details.

Interview line:

> Mappers avoid repeated conversion code inside controllers and services. That keeps the business logic readable.

## Example Flow: Apply For Job

1. User sends `POST /apply/applications/apply?jobId=10`.
2. `JobApplyController` gets the current user from the JWT-authenticated session.
3. Controller calls `jobApplicationService.applyForJob(userId, jobId, request)`.
4. `JobApplyServiceImpl` loads the `User` and `Job`.
5. Service checks if the application already exists.
6. Service creates and saves an `ApplyJob` record.
7. API returns a success message.

## Example Flow: Recruiter Views Applications

1. Recruiter sends `GET /recruiter/jobs/{jobId}/applications`.
2. `RecruiterController` passes recruiter email and job id to the service.
3. Service loads the job.
4. Service checks that the logged-in recruiter owns the job.
5. Service returns applications mapped to `ApplyJobDto`.

## What Was Simplified

- Removed an empty duplicate `ApplyJobRequestDTO` from the mapper package.
- Removed old commented code from job and application flows.
- Replaced field injection with constructor injection in touched controllers/services.
- Reused existing mappers instead of manually rebuilding nested user/job DTOs.
- Split long service logic into small helper methods with clear names.
- Made the sorted jobs endpoint actually return paginated/sorted job data.

