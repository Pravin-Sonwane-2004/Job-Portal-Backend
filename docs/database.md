# Job Portal Database Design

This document describes the current database model used by the Spring Boot Job Portal backend.

The schema is based on the JPA entities in:

```text
src/main/java/com/pravin/job_portal_backend/entity
```

The exact generated SQL can vary slightly by database dialect and Hibernate naming strategy. The table and column names below follow the names configured in the entities and the default Spring/Hibernate snake-case naming style.

## Main Tables

| Table | Purpose |
| --- | --- |
| `users` | Stores all accounts: candidates, recruiters, company users, and admins |
| `companies` | Stores company profiles used by the company portal |
| `job` | Stores job postings |
| `apply_job` | Stores candidate applications to jobs |
| `saved_job` | Stores candidate saved/bookmarked jobs |
| `resume` | Stores resume path/URL records |
| `password_reset_tokens` | Stores forgot-password reset tokens |
| `messages` | Stores user-to-user messages |
| `interviews` | Stores scheduled interviews |
| `job_alerts` | Stores candidate job alert preferences |
| `company_reviews` | Stores user reviews for companies |
| `user_skills` | Element collection table for user skills |
| `job_requirements` | Element collection table for job requirements |

`EmailRequest` is not a database table. It is a request object used for sending emails.

## Enum Values

### `Role`

```text
USER
RECRUITER
COMPANY_ADMIN
COMPANY_EMPLOYEE
ADMIN
```

### `UserStatus`

```text
ACTIVE
DEACTIVATED
SUSPENDED
```

### `ExperienceLevel`

```text
INTERN
JUNIOR
MID
SENIOR
EXPERT
```

### `JobType`

```text
FULL_TIME
PART_TIME
CONTRACT
INTERNSHIP
FREELANCE
```

### `JobStatus`

```text
OPEN
CLOSED
DRAFT
```

## `users`

Purpose: Stores platform users. A user may be a candidate, recruiter, company admin, company employee, or admin.

Entity: `User`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `id` | `BIGINT` | No | PK | Auto-generated user id |
| `email` | `VARCHAR(255)` | No | Unique, indexed | Login username and contact email |
| `password` | `VARCHAR(255)` | No |  | BCrypt-hashed password |
| `role` | `VARCHAR(50)` | No | Indexed | One of `Role` enum values |
| `experience_level` | `VARCHAR` | Yes |  | One of `ExperienceLevel` enum values |
| `status` | `VARCHAR` | No |  | Defaults to `ACTIVE` in entity |
| `name` | `VARCHAR(255)` | Yes |  | Display/full name |
| `avatar_url` | `VARCHAR(512)` | Yes |  | Profile image URL |
| `designation` | `VARCHAR(255)` | Yes |  | Current title/designation |
| `verified` | `BOOLEAN` | No |  | Defaults to `false` |
| `location` | `VARCHAR(255)` | Yes |  | User location |
| `bio` | `VARCHAR(1024)` | Yes |  | Profile summary |
| `phone_number` | `VARCHAR(20)` | Yes |  | Contact number |
| `linkedin_url` | `VARCHAR(512)` | Yes |  | LinkedIn profile URL |
| `github_url` | `VARCHAR(512)` | Yes |  | GitHub profile URL |
| `job_role` | `VARCHAR(255)` | Yes |  | Target or current job role |
| `company_id` | `BIGINT` | Yes | FK | References `companies.id` for company users |
| `is_deleted` | `BOOLEAN` | No |  | Soft-delete flag, defaults to `false` |
| `created_at` | `DATETIME` | Yes |  | Set in `@PrePersist` |
| `updated_at` | `DATETIME` | Yes |  | Set in `@PrePersist` and `@PreUpdate` |

Indexes:

- `idx_user_email` on `email`
- `idx_user_role` on `role`
- Unique constraint on `email`

Relationships:

- `users.company_id` -> `companies.id`
- One user can have many `apply_job` records.
- One user can have many `saved_job` records.
- One user can have many `resume` records.
- One user can have many `user_skills` values.

## `user_skills`

Purpose: Stores a user's skills as an element collection.

Entity field: `User.skills`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `user_id` | `BIGINT` | No | FK | References `users.id` |
| `skill` | `VARCHAR(100)` | Yes |  | Skill name |

Relationship:

- `user_skills.user_id` -> `users.id`

Note: This table does not have its own entity id in the current model.

## `companies`

Purpose: Stores company profiles for company admins and company employees.

Entity: `Company`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `id` | `BIGINT` | No | PK | Auto-generated company id |
| `name` | `VARCHAR(255)` | No | Unique | Company name |
| `description` | `VARCHAR(2000)` | Yes |  | Company description |
| `website` | `VARCHAR(512)` | Yes |  | Company website |
| `industry` | `VARCHAR(255)` | Yes |  | Company industry |
| `location` | `VARCHAR(255)` | Yes |  | Company location |
| `logo_url` | `VARCHAR(512)` | Yes |  | Company logo URL |
| `verified` | `BOOLEAN` | No |  | Company verification flag |
| `created_at` | `DATETIME` | Yes |  | Set in `@PrePersist` |
| `updated_at` | `DATETIME` | Yes |  | Set in `@PrePersist` and `@PreUpdate` |

Relationships:

- One company can have many `users` as employees.
- One company can have many `company_reviews`.
- Company jobs are currently linked by the `job.company` name string, not by a `company_id` foreign key.

## `company_reviews`

Purpose: Stores user reviews for companies.

Entity: `CompanyReview`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `id` | `BIGINT` | No | PK | Auto-generated review id |
| `company_id` | `BIGINT` | No | FK | References `companies.id` |
| `user_id` | `BIGINT` | No | FK | References `users.id` |
| `content` | `VARCHAR(2000)` | No |  | Review text |
| `rating` | `INT` | No |  | Numeric rating |
| `created_at` | `DATETIME` | No |  | Set in `@PrePersist` |

Relationships:

- `company_reviews.company_id` -> `companies.id`
- `company_reviews.user_id` -> `users.id`

## `job`

Purpose: Stores job postings created by admins, recruiters, company admins, or company employees.

Entity: `Job`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `id` | `BIGINT` | No | PK | Auto-generated job id |
| `title` | `VARCHAR(255)` | No | Indexed | Job title |
| `location` | `VARCHAR(255)` | No | Indexed | Job location |
| `salary` | `VARCHAR(100)` | No |  | Salary text/range |
| `company` | `VARCHAR(255)` | Yes |  | Company name shown on job listing |
| `description` | `TEXT` | Yes |  | Job description |
| `job_type` | `VARCHAR` | Yes |  | One of `JobType` enum values |
| `experience_level` | `VARCHAR` | Yes |  | One of `ExperienceLevel` enum values |
| `status` | `VARCHAR` | Yes |  | Defaults to `OPEN` in entity/service |
| `category` | `VARCHAR(100)` | Yes |  | Job category |
| `posted_by` | `BIGINT` | Yes | FK | References `users.id` |
| `posted_date` | `DATE` | Yes |  | Set by Hibernate `@CreationTimestamp` |
| `last_date_to_apply` | `DATE` | Yes |  | Application deadline |
| `updated_at` | `DATETIME` | Yes |  | Set by Hibernate `@UpdateTimestamp` |

Indexes:

- `idx_job_title` on `title`
- `idx_job_location` on `location`

Relationships:

- `job.posted_by` -> `users.id`
- One job can have many `apply_job` records.
- One job can have many `saved_job` records.
- One job can have many `job_requirements` values.

## `job_requirements`

Purpose: Stores requirements for a job as an element collection.

Entity field: `Job.requirements`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `job_id` | `BIGINT` | No | FK | References `job.id` |
| `requirement` | `VARCHAR(255)` | Yes |  | Requirement text |

Relationship:

- `job_requirements.job_id` -> `job.id`

Note: This table does not have its own entity id in the current model.

## `apply_job`

Purpose: Stores candidate applications to jobs.

Entity: `ApplyJob`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `id` | `BIGINT` | No | PK | Auto-generated application id |
| `user_id` | `BIGINT` | No | FK, indexed | Applicant user id |
| `job_id` | `BIGINT` | No | FK, indexed | Applied job id |
| `applied_at` | `DATETIME` | No |  | Set by Hibernate `@CreationTimestamp` |
| `updated_at` | `DATETIME` | Yes |  | Set by Hibernate `@UpdateTimestamp` |
| `status` | `VARCHAR(50)` | No |  | Example values: `APPLIED`, `UNDER_REVIEW`, `REJECTED`, `HIRED` |
| `recruiter_remarks` | `VARCHAR(1000)` | Yes |  | Recruiter/admin remarks |
| `resume_link` | `VARCHAR(500)` | Yes |  | Resume URL/path submitted with application |
| `cover_letter` | `TEXT` | Yes |  | Cover letter text |
| `phone_number` | `VARCHAR(30)` | Yes |  | Applicant phone for this application |
| `linkedin_url` | `VARCHAR(500)` | Yes |  | Applicant LinkedIn URL |
| `portfolio_url` | `VARCHAR(500)` | Yes |  | Applicant portfolio URL |
| `expected_salary` | `VARCHAR(100)` | Yes |  | Expected salary text |
| `notice_period` | `VARCHAR(100)` | Yes |  | Notice period text |
| `applied_from_ip` | `VARCHAR(45)` | Yes |  | IPv4/IPv6 address for audit/security |
| `source` | `VARCHAR(100)` | Yes |  | Example: `Web`, `Mobile`, `Referral` |
| `user_agent` | `VARCHAR(512)` | Yes |  | Browser/device user agent |

Indexes:

- `idx_applyjob_user` on `user_id`
- `idx_applyjob_job` on `job_id`

Relationships:

- `apply_job.user_id` -> `users.id`
- `apply_job.job_id` -> `job.id`

Important note: duplicate application prevention is enforced by application logic, not by a database unique constraint in the current entity.

## `saved_job`

Purpose: Stores jobs bookmarked by candidates.

Entity: `SavedJob`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `id` | `BIGINT` | No | PK | Auto-generated saved-job id |
| `user_id` | `BIGINT` | No | FK, indexed | Candidate user id |
| `job_id` | `BIGINT` | No | FK, indexed | Saved job id |
| `saved_at` | `DATETIME` | No |  | Set by Hibernate `@CreationTimestamp` |
| `updated_at` | `DATETIME` | Yes |  | Set by Hibernate `@UpdateTimestamp` |

Indexes:

- `idx_savedjob_user` on `user_id`
- `idx_savedjob_job` on `job_id`

Relationships:

- `saved_job.user_id` -> `users.id`
- `saved_job.job_id` -> `job.id`

Important note: duplicate saved jobs are handled by application logic, not by a database unique constraint in the current entity.

## `resume`

Purpose: Stores resume path/URL records for candidates.

Entity: `Resume`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `id` | `BIGINT` | No | PK | Auto-generated resume id |
| `user_id` | `BIGINT` | No | FK, indexed | Owner user id |
| `file_path` | `VARCHAR(512)` | No |  | Path or URL for resume file |
| `uploaded_at` | `DATETIME` | No |  | Set by Hibernate `@CreationTimestamp` |

Indexes:

- `idx_resume_user` on `user_id`

Relationship:

- `resume.user_id` -> `users.id`

## `password_reset_tokens`

Purpose: Stores reset tokens for the forgot-password flow.

Entity: `PasswordResetToken`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `id` | `BIGINT` | No | PK | Auto-generated token id |
| `token` | `VARCHAR(120)` | No | Unique | Secure random reset token |
| `user_id` | `BIGINT` | No | FK | References `users.id` |
| `expires_at` | `DATETIME` | No |  | Expiration time |
| `used_at` | `DATETIME` | Yes |  | Set when token is used |
| `created_at` | `DATETIME` | No |  | Set by service when token is created |

Relationships:

- `password_reset_tokens.user_id` -> `users.id`

Security notes:

- Tokens are single-use because `used_at` is checked.
- Tokens expire using `expires_at`.
- Existing tokens for a user are deleted before a new token is created.

## `messages`

Purpose: Stores messages between users.

Entity: `Message`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `id` | `BIGINT` | No | PK | Auto-generated message id |
| `sender_id` | `BIGINT` | No | FK | References `users.id` |
| `receiver_id` | `BIGINT` | No | FK | References `users.id` |
| `content` | `VARCHAR(2000)` | No |  | Message body |
| `sent_at` | `DATETIME` | No |  | Set in `@PrePersist` |
| `is_read` | `BOOLEAN` | No |  | Read/unread state |

Relationships:

- `messages.sender_id` -> `users.id`
- `messages.receiver_id` -> `users.id`

## `interviews`

Purpose: Stores scheduled interviews for applications.

Entity: `Interview`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `id` | `BIGINT` | No | PK | Auto-generated interview id |
| `application_id` | `BIGINT` | No | FK | References `apply_job.id` |
| `employer_id` | `BIGINT` | No | FK | Recruiter/company/admin user scheduling interview |
| `candidate_id` | `BIGINT` | No | FK | Candidate user |
| `scheduled_time` | `DATETIME` | No |  | Interview date/time |
| `status` | `VARCHAR(40)` | No |  | Interview status controlled by application logic |
| `meeting_link` | `VARCHAR(512)` | Yes |  | Online meeting URL |
| `notes` | `VARCHAR(1000)` | Yes |  | Interview notes |

Relationships:

- `interviews.application_id` -> `apply_job.id`
- `interviews.employer_id` -> `users.id`
- `interviews.candidate_id` -> `users.id`

## `job_alerts`

Purpose: Stores saved alert preferences for candidate job notifications.

Entity: `JobAlert`

| Column | Type | Nullable | Key/Index | Notes |
| --- | --- | --- | --- | --- |
| `id` | `BIGINT` | No | PK | Auto-generated alert id |
| `user_id` | `BIGINT` | No | FK | References `users.id` |
| `keywords` | `VARCHAR(500)` | No |  | Search keywords |
| `location` | `VARCHAR(255)` | Yes |  | Optional preferred location |
| `created_at` | `DATETIME` | No |  | Set in `@PrePersist` |
| `active` | `BOOLEAN` | No |  | Defaults to `true` |

Relationship:

- `job_alerts.user_id` -> `users.id`

## Relationship Summary

| Relationship | Meaning |
| --- | --- |
| `users.company_id` -> `companies.id` | Company admins/employees belong to a company |
| `company_reviews.company_id` -> `companies.id` | Reviews belong to a company |
| `company_reviews.user_id` -> `users.id` | Reviews are written by users |
| `job.posted_by` -> `users.id` | Jobs can be posted by a user |
| `job_requirements.job_id` -> `job.id` | Requirements belong to a job |
| `user_skills.user_id` -> `users.id` | Skills belong to a user |
| `apply_job.user_id` -> `users.id` | Applications belong to candidates |
| `apply_job.job_id` -> `job.id` | Applications target jobs |
| `saved_job.user_id` -> `users.id` | Saved jobs belong to candidates |
| `saved_job.job_id` -> `job.id` | Saved records point to jobs |
| `resume.user_id` -> `users.id` | Resumes belong to candidates |
| `password_reset_tokens.user_id` -> `users.id` | Reset tokens belong to users |
| `messages.sender_id` -> `users.id` | Message sender |
| `messages.receiver_id` -> `users.id` | Message receiver |
| `interviews.application_id` -> `apply_job.id` | Interview is tied to an application |
| `interviews.employer_id` -> `users.id` | Interview employer/recruiter/company user |
| `interviews.candidate_id` -> `users.id` | Interview candidate |
| `job_alerts.user_id` -> `users.id` | Alert owner |

## Indexing Strategy

Indexes currently defined in entities:

- `users.email`
- `users.role`
- `job.title`
- `job.location`
- `apply_job.user_id`
- `apply_job.job_id`
- `saved_job.user_id`
- `saved_job.job_id`
- `resume.user_id`

Recommended future indexes:

- `job.status`
- `job.company`
- `job.category`
- `job.posted_date`
- `companies.name`
- `company_reviews.company_id`
- `messages.receiver_id`
- `interviews.candidate_id`
- `interviews.employer_id`
- Composite unique index on `apply_job(user_id, job_id)`
- Composite unique index on `saved_job(user_id, job_id)`

## Soft Delete Behavior

The `users` table has an `is_deleted` flag. This allows user records to be marked as deleted without physically removing them from the database.

Current note: not every service currently filters by `is_deleted`, so full soft-delete behavior should be implemented consistently before relying on it for production.

## Timestamp Meaning

| Column | Meaning |
| --- | --- |
| `created_at` | Record creation time |
| `updated_at` | Last modification time |
| `posted_date` | Job posting date |
| `applied_at` | Candidate application time |
| `saved_at` | Candidate saved/bookmarked time |
| `uploaded_at` | Resume upload record time |
| `sent_at` | Message send time |
| `expires_at` | Password reset token expiry |
| `used_at` | Password reset token usage time |

## Security Notes

- User passwords are stored as hashed values, not plain text.
- Forgot-password tokens are stored server-side and marked used through `used_at`.
- Application records can store `applied_from_ip` and `user_agent` for audit/security.
- Resume records currently store a path or URL, not the actual file binary.
- `EmailRequest` is not persisted; it is only used to send email.

## Current Limitations And Future Improvements

- Add real file storage metadata for resumes: original filename, content type, size, storage key.
- Add database migrations with Flyway or Liquibase instead of relying on `ddl-auto`.
- Add unique database constraints for duplicate applications and duplicate saved jobs.
- Add `company_id` to `job` if jobs should be strongly linked to companies instead of only company name.
- Add application status history table for audit and timeline display.
- Normalize skills into a master `skills` table if filtering/search becomes more advanced.
- Add job analytics tables for views, impressions, clicks, and applications.
- Add notification table for in-app notifications.
