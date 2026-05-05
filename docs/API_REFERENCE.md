# Backend API Reference

This document records the backend route ownership used by the frontend portals. Spring Security remains the source of truth for access control.

## Public And Auth APIs

| Method | Path | Access | Purpose |
| --- | --- | --- | --- |
| `GET` | `/public/health-check` | Public | Backend health check |
| `POST` | `/public/signup` | Public | Candidate or recruiter signup |
| `POST` | `/public/company/signup` | Public | Create company and first company admin |
| `POST` | `/public/login` | Public | Authenticate and return JWT plus user DTO |
| `POST` | `/public/password/forgot` | Public | Create and email a password reset token |
| `POST` | `/public/password/reset` | Public | Reset password using a token |
| `GET` | `/public/jobs/paginated` | Public | Public paginated job search |

## Candidate APIs

| Method | Path | Access | Purpose |
| --- | --- | --- | --- |
| `GET` | `/user/jobs` | `USER`, `ADMIN` | List jobs |
| `GET` | `/user/jobs/{id}` | `USER`, `ADMIN` | Get job details |
| `GET` | `/user/jobs/sorted` | `USER`, `ADMIN` | Sorted job list |
| `POST` | `/apply/applications/apply?jobId={id}` | `USER` | Apply to a job |
| `GET` | `/apply/applications/my-applied-dto` | `USER` | Candidate application cards |
| `GET` | `/apply/applications/my-applied-entities` | `USER` | Candidate application records |
| `DELETE` | `/apply/applications/cancel?jobId={id}` | `USER` | Cancel by job id |
| `DELETE` | `/apply/applications/{applicationId}` | `USER`, `ADMIN` | Delete application by id |
| `PUT` | `/apply/applications/{applicationId}` | `USER`, `ADMIN` | Update application fields |
| `GET` | `/api/saved-jobs/user/{userId}` | `USER` | List saved jobs |
| `POST` | `/api/saved-jobs/save` | `USER` | Save a job |
| `DELETE` | `/api/saved-jobs/unsave` | `USER` | Remove saved job |
| `GET` | `/api/resumes/user/{userId}` | `USER` | List resumes |
| `POST` | `/api/resumes/upload` | `USER` | Save a resume path or URL |
| `DELETE` | `/api/resumes/delete/{resumeId}` | `USER` | Delete resume |

## Application Request Payload

`POST /apply/applications/apply?jobId={id}` accepts an optional body. The authenticated JWT user is used as the real applicant.

```json
{
  "userId": 1,
  "jobId": 10,
  "resumeLink": "https://example.com/resume.pdf",
  "coverLetter": "I am interested in this role...",
  "phoneNumber": "9999999999",
  "linkedinUrl": "https://linkedin.com/in/example",
  "portfolioUrl": "https://portfolio.example.com",
  "expectedSalary": "8 LPA",
  "noticePeriod": "30 days",
  "source": "Web",
  "userAgent": "Mozilla/5.0"
}
```

The backend stores application metadata on `ApplyJob` and defaults status to `APPLIED`.

## Profile And Collaboration APIs

| Method | Path | Access | Purpose |
| --- | --- | --- | --- |
| `GET` | `/role-profile/get-profile` | Authenticated roles | Current profile |
| `PUT` | `/role-profile/update-profile` | Authenticated roles | Update profile |
| `GET` | `/role-profile/users-name` | Authenticated roles | Current display name |
| `GET` | `/role-profile/full-name` | Authenticated roles | Current full name |
| `GET` | `/api/profile-insights/me` | `USER`, `ADMIN`, `RECRUITER` | Dashboard metrics |
| `GET` | `/api/job-alerts/me` | `USER` | Candidate job alerts |
| `POST` | `/api/job-alerts` | `USER` | Create job alert |
| `DELETE` | `/api/job-alerts/{alertId}` | `USER` | Delete job alert |
| `GET` | `/api/messages/inbox` | Authenticated roles | Inbox messages |
| `GET` | `/api/messages/sent` | Authenticated roles | Sent messages |
| `POST` | `/api/messages` | Authenticated roles | Send message |
| `GET` | `/api/interviews/me` | Authenticated roles | My interviews |
| `POST` | `/api/interviews` | Authenticated roles | Schedule interview |
| `PATCH` | `/api/interviews/{id}/status` | Authenticated roles | Update interview status |

## Recruiter APIs

Recruiter routes are scoped to the authenticated recruiter. Recruiters can only update and delete jobs they posted.

| Method | Path | Access | Purpose |
| --- | --- | --- | --- |
| `GET` | `/recruiter/jobs` | `RECRUITER` | Recruiter's jobs |
| `POST` | `/recruiter/jobs` | `RECRUITER` | Create recruiter job |
| `GET` | `/recruiter/jobs/{id}` | `RECRUITER` | Get owned recruiter job |
| `PUT` | `/recruiter/jobs/{id}` | `RECRUITER` | Update owned recruiter job |
| `DELETE` | `/recruiter/jobs/{id}` | `RECRUITER` | Delete owned recruiter job |
| `GET` | `/recruiter/applications` | `RECRUITER` | Applications for recruiter's jobs |
| `GET` | `/recruiter/jobs/{jobId}/applications` | `RECRUITER` | Applications for one owned job |
| `PUT` | `/recruiter/applications/{applicationId}` | `RECRUITER` | Update application status or remarks |
| `GET` | `/recruiter/talent` | `RECRUITER` | Search candidate profiles |
| `GET` | `/recruiter/talent/{userId}` | `RECRUITER` | Candidate profile details |

## Company APIs

Company portal routes are scoped to the authenticated user's company. Employee management is limited to `COMPANY_ADMIN`.

| Method | Path | Access | Purpose |
| --- | --- | --- | --- |
| `GET` | `/company-portal/dashboard` | `COMPANY_ADMIN`, `COMPANY_EMPLOYEE` | Company metrics |
| `GET` | `/company-portal/company` | `COMPANY_ADMIN`, `COMPANY_EMPLOYEE` | Current company profile |
| `PUT` | `/company-portal/company` | `COMPANY_ADMIN` | Update company profile |
| `GET` | `/company-portal/employees` | `COMPANY_ADMIN` | List employees |
| `POST` | `/company-portal/employees` | `COMPANY_ADMIN` | Add employee |
| `DELETE` | `/company-portal/employees/{employeeId}` | `COMPANY_ADMIN` | Remove employee |
| `GET` | `/company-portal/jobs` | `COMPANY_ADMIN`, `COMPANY_EMPLOYEE` | Company jobs |
| `POST` | `/company-portal/jobs` | `COMPANY_ADMIN`, `COMPANY_EMPLOYEE` | Create company job |
| `PUT` | `/company-portal/jobs/{jobId}` | `COMPANY_ADMIN`, `COMPANY_EMPLOYEE` | Update company job |
| `DELETE` | `/company-portal/jobs/{jobId}` | `COMPANY_ADMIN`, `COMPANY_EMPLOYEE` | Delete company job |

## Admin APIs

| Method | Path | Access | Purpose |
| --- | --- | --- | --- |
| `GET` | `/admin/users` | `ADMIN` | List users |
| `POST` | `/admin/signup` | `ADMIN` | Create user |
| `PUT` | `/admin/user/{username}` | `ADMIN` | Update user profile |
| `DELETE` | `/admin/user/{username}` | `ADMIN` | Delete user |
| `GET` | `/admin/jobs` | `ADMIN` | List all jobs |
| `POST` | `/admin/jobs` | `ADMIN` | Create job |
| `PUT` | `/admin/jobs/{id}` | `ADMIN` | Update job |
| `DELETE` | `/admin/jobs/{id}` | `ADMIN` | Delete job |
| `GET` | `/admin/applications` | `ADMIN` | List applications |
| `GET` | `/admin/applications/job/{jobId}` | `ADMIN` | Applications by job |
| `GET` | `/admin/applications/user/{userId}` | `ADMIN` | Applications by user |
| `GET` | `/admin/admin/all-appliers` | `ADMIN` | Applications with profile summary |

## Public Company APIs

| Method | Path | Access | Purpose |
| --- | --- | --- | --- |
| `GET` | `/api/companies` | Public | List companies |
| `GET` | `/api/companies/{id}` | Public | Company details |
| `GET` | `/api/company-reviews/company/{companyId}` | Public | Company reviews |
| `POST` | `/api/company-reviews/company/{companyId}` | `USER`, `ADMIN` | Add company review |
| `DELETE` | `/api/company-reviews/{reviewId}` | `ADMIN` | Delete review |

## Cross-Role APIs

| Method | Path | Access | Purpose |
| --- | --- | --- | --- |
| `POST` | `/email/send` | Authenticated roles | Send email |

## CORS Notes

Allowed methods are:

```text
GET, POST, PUT, PATCH, DELETE, OPTIONS
```

Allowed origins include local frontend URLs and optional `FRONTEND_URL` or comma-separated `FRONTEND_URLS` environment values.
