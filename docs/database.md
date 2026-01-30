# Job Portal Database Design

This document describes the **structured SQL schema** for a Job Portal system. It is written in a clean, documentation-style format suitable for reports, reviews, or implementation reference.

---

## 1. users Table

**Purpose:** Stores all user accounts including job seekers, recruiters, and administrators.

| Field Name       | Data Type                                       | Null | Key    | Default           | Extra          | Description                    |
| ---------------- | ----------------------------------------------- | ---- | ------ | ----------------- | -------------- | ------------------------------ |
| id               | BIGINT                                          | NO   | PK     | NULL              | AUTO_INCREMENT | Unique user identifier         |
| name             | VARCHAR(255)                                    | YES  |        | NULL              |                | Full name of the user          |
| email            | VARCHAR(255)                                    | NO   | UNIQUE | NULL              |                | Registered email address       |
| password         | VARCHAR(255)                                    | NO   |        | NULL              |                | Encrypted password             |
| phone_number     | VARCHAR(20)                                     | YES  |        | NULL              |                | Contact phone number           |
| role             | ENUM('ADMIN','RECRUITER','USER')                | NO   | INDEX  | NULL              |                | Role of the user in the system |
| experience_level | ENUM('INTERN','JUNIOR','MID','SENIOR','EXPERT') | YES  |        | NULL              |                | Professional experience level  |
| designation      | VARCHAR(255)                                    | YES  |        | NULL              |                | Current designation or title   |
| job_role         | VARCHAR(255)                                    | YES  |        | NULL              |                | Preferred job role             |
| location         | VARCHAR(255)                                    | YES  |        | NULL              |                | Current location               |
| bio              | VARCHAR(1024)                                   | YES  |        | NULL              |                | Short professional summary     |
| avatar_url       | VARCHAR(512)                                    | YES  |        | NULL              |                | Profile image URL              |
| github_url       | VARCHAR(512)                                    | YES  |        | NULL              |                | GitHub profile link            |
| linkedin_url     | VARCHAR(512)                                    | YES  |        | NULL              |                | LinkedIn profile link          |
| verified         | BIT(1)                                          | NO   |        | 0                 |                | Email verification status      |
| status           | ENUM('ACTIVE','DEACTIVATED','SUSPENDED')        | NO   |        | 'ACTIVE'          |                | Account status                 |
| is_deleted       | BIT(1)                                          | NO   |        | 0                 |                | Soft delete flag               |
| created_at       | DATETIME(6)                                     | YES  |        | CURRENT_TIMESTAMP |                | Account creation time          |
| updated_at       | DATETIME(6)                                     | YES  |        | CURRENT_TIMESTAMP | ON UPDATE      | Last update time               |

---------|----------|------|-----|---------|-------|------------|
| id | BIGINT | NO | PK | NULL | AUTO_INCREMENT | Unique user identifier |
| name | VARCHAR(255) | YES | | NULL | | Full name |
| email | VARCHAR(255) | NO | UNIQUE | NULL | | Email address |
| password | VARCHAR(255) | NO | | NULL | | Encrypted password |
| phone_number | VARCHAR(20) | YES | | NULL | | Contact number |
| role | ENUM('ADMIN','RECRUITER','USER') | NO | INDEX | NULL | | User role |
| experience_level | ENUM('INTERN','JUNIOR','MID','SENIOR','EXPERT') | YES | | NULL | | Experience category |
| designation | VARCHAR(255) | YES | | NULL | | Current job title |
| job_role | VARCHAR(255) | YES | | NULL | | Preferred role |
| location | VARCHAR(255) | YES | | NULL | | User location |
| bio | VARCHAR(1024) | YES | | NULL | | Short profile summary |
| avatar_url | VARCHAR(512) | YES | | NULL | | Profile image URL |
| github_url | VARCHAR(512) | YES | | NULL | | GitHub profile |
| linkedin_url | VARCHAR(512) | YES | | NULL | | LinkedIn profile |
| verified | BIT(1) | NO | | 0 | | Email verification status |
| status | ENUM('ACTIVE','DEACTIVATED','SUSPENDED') | NO | | 'ACTIVE' | | Account status |
| is_deleted | BIT(1) | NO | | 0 | | Soft delete flag |
| created_at | DATETIME(6) | YES | | CURRENT_TIMESTAMP | | Creation time |
| updated_at | DATETIME(6) | YES | | CURRENT_TIMESTAMP | ON UPDATE | Last update time |

---

## 2. resume Table

**Purpose:** Stores resumes uploaded by users.

| Field Name  | Data Type    | Null | Key | Default           | Extra          | Description                |
| ----------- | ------------ | ---- | --- | ----------------- | -------------- | -------------------------- |
| id          | BIGINT       | NO   | PK  | NULL              | AUTO_INCREMENT | Unique resume identifier   |
| user_id     | BIGINT       | NO   | FK  | NULL              |                | References users(id)       |
| file_path   | VARCHAR(512) | NO   |     | NULL              |                | Path or URL of resume file |
| uploaded_at | DATETIME(6)  | NO   |     | CURRENT_TIMESTAMP |                | Resume upload time         |

---------|----------|------|-----|---------|-------|------------|
| id | BIGINT | NO | PK | NULL | AUTO_INCREMENT | Resume ID |
| user_id | BIGINT | NO | FK | NULL | | Reference to users(id) |
| file_path | VARCHAR(512) | NO | | NULL | | Resume file path |
| uploaded_at | DATETIME(6) | NO | | CURRENT_TIMESTAMP | | Upload timestamp |

---

## 3. user_skills Table

**Purpose:** Stores skills associated with users.

| Field Name | Data Type    | Null | Key | Default | Extra | Description                 |
| ---------- | ------------ | ---- | --- | ------- | ----- | --------------------------- |
| user_id    | BIGINT       | NO   | FK  | NULL    |       | References users(id)        |
| skill      | VARCHAR(100) | YES  |     | NULL    |       | Skill name provided by user |

---------|----------|------|-----|------------|
| id | BIGINT | NO | PK | Skill record ID |
| user_id | BIGINT | NO | FK | Linked user |
| skill_name | VARCHAR(255) | NO | | Skill name |
| proficiency | ENUM('BEGINNER','INTERMEDIATE','ADVANCED','EXPERT') | YES | | Skill level |

---

## 4. job Table

**Purpose:** Stores job postings created by recruiters or admins.

| Field Name         | Data Type                                                         | Null | Key   | Default | Extra          | Description                             |
| ------------------ | ----------------------------------------------------------------- | ---- | ----- | ------- | -------------- | --------------------------------------- |
| id                 | BIGINT                                                            | NO   | PK    | NULL    | AUTO_INCREMENT | Unique job identifier                   |
| title              | VARCHAR(255)                                                      | NO   | INDEX | NULL    |                | Job title                               |
| description        | TEXT                                                              | YES  |       | NULL    |                | Detailed job description                |
| company            | VARCHAR(255)                                                      | NO   |       | NULL    |                | Company name                            |
| category           | VARCHAR(100)                                                      | YES  |       | NULL    |                | Job category / domain                   |
| location           | VARCHAR(255)                                                      | NO   | INDEX | NULL    |                | Job location                            |
| salary             | VARCHAR(100)                                                      | NO   |       | NULL    |                | Salary or compensation range            |
| experience_level   | ENUM('INTERN','JUNIOR','MID','SENIOR','EXPERT')                   | YES  |       | NULL    |                | Required experience level               |
| job_type           | ENUM('FULL_TIME','PART_TIME','CONTRACT','FREELANCE','INTERNSHIP') | YES  |       | NULL    |                | Type of employment                      |
| status             | ENUM('OPEN','CLOSED','DRAFT')                                     | YES  |       | NULL    |                | Job posting status                      |
| posted_by          | BIGINT                                                            | YES  | FK    | NULL    |                | References users(id) who posted the job |
| posted_date        | DATE                                                              | YES  |       | NULL    |                | Job posting date                        |
| last_date_to_apply | DATE                                                              | YES  |       | NULL    |                | Application deadline                    |
| updated_at         | DATETIME(6)                                                       | YES  |       | NULL    |                | Last update timestamp                   |

---------|----------|------|-----|------------|
| id | BIGINT | NO | PK | Job ID |
| recruiter_id | BIGINT | NO | FK | Posted by user |
| title | VARCHAR(255) | NO | | Job title |
| description | TEXT | NO | | Job details |
| location | VARCHAR(255) | YES | | Job location |
| experience_required | ENUM('INTERN','JUNIOR','MID','SENIOR','EXPERT') | YES | | Required experience |
| job_type | ENUM('FULL_TIME','PART_TIME','CONTRACT','INTERNSHIP') | NO | | Job type |
| salary_min | DECIMAL(10,2) | YES | | Minimum salary |
| salary_max | DECIMAL(10,2) | YES | | Maximum salary |
| status | ENUM('OPEN','CLOSED','ON_HOLD') | NO | | Job status |
| created_at | DATETIME(6) | YES | | Created timestamp |

---

## 5. job_requirements Table

**Purpose:** Stores individual requirements associated with a job posting.

| Field Name  | Data Type    | Null | Key | Default | Extra | Description                      |
| ----------- | ------------ | ---- | --- | ------- | ----- | -------------------------------- |
| job_id      | BIGINT       | NO   | FK  | NULL    |       | References job(id)               |
| requirement | VARCHAR(255) | YES  |     | NULL    |       | Job requirement or qualification |

---------|----------|------|-----|------------|
| id | BIGINT | NO | PK | Requirement ID |
| job_id | BIGINT | NO | FK | Linked job |
| requirement | VARCHAR(255) | NO | | Requirement detail |

---

## 6. apply_job Table

**Purpose:** Tracks job applications submitted by users.

| Field Name      | Data Type    | Null | Key | Default           | Extra          | Description                                 |
| --------------- | ------------ | ---- | --- | ----------------- | -------------- | ------------------------------------------- |
| id              | BIGINT       | NO   | PK  | NULL              | AUTO_INCREMENT | Unique application identifier               |
| user_id         | BIGINT       | NO   | FK  | NULL              |                | References users(id)                        |
| job_id          | BIGINT       | NO   | FK  | NULL              |                | References job(id)                          |
| status          | VARCHAR(50)  | NO   |     | NULL              |                | Current application status                  |
| applied_at      | DATETIME(6)  | NO   |     | CURRENT_TIMESTAMP |                | Application submission time                 |
| updated_at      | DATETIME(6)  | YES  |     | NULL              |                | Last status update time                     |
| source          | VARCHAR(100) | YES  |     | NULL              |                | Application source (portal, referral, etc.) |
| resume_link     | VARCHAR(500) | YES  |     | NULL              |                | Resume used for application                 |
| cover_letter    | TEXT         | YES  |     | NULL              |                | Cover letter content                        |
| applied_from_ip | VARCHAR(45)  | YES  |     | NULL              |                | Applicant IP address                        |
| user_agent      | VARCHAR(512) | YES  |     | NULL              |                | Browser or device information               |

---------|----------|------|-----|------------|
| id | BIGINT | NO | PK | Application ID |
| user_id | BIGINT | NO | FK | Applicant |
| job_id | BIGINT | NO | FK | Applied job |
| applied_at | DATETIME(6) | NO | | Application date |
| status | ENUM('APPLIED','SHORTLISTED','REJECTED','HIRED') | NO | | Application status |

---

## 7. saved_job Table

**Purpose:** Stores jobs bookmarked or saved by users for later viewing.

| Field Name | Data Type   | Null | Key | Default           | Extra          | Description                        |
| ---------- | ----------- | ---- | --- | ----------------- | -------------- | ---------------------------------- |
| id         | BIGINT      | NO   | PK  | NULL              | AUTO_INCREMENT | Unique saved job record identifier |
| user_id    | BIGINT      | NO   | FK  | NULL              |                | References users(id)               |
| job_id     | BIGINT      | NO   | FK  | NULL              |                | References job(id)                 |
| saved_at   | DATETIME(6) | NO   |     | CURRENT_TIMESTAMP |                | Time when job was saved            |
| updated_at | DATETIME(6) | YES  |     | NULL              |                | Last update timestamp              |

---------|----------|------|-----|------------|
| id | BIGINT | NO | PK | Saved record ID |
| user_id | BIGINT | NO | FK | User |
| job_id | BIGINT | NO | FK | Saved job |
| saved_at | DATETIME(6) | NO | | Bookmark time |

---

### Notes

* All foreign keys should enforce **ON DELETE CASCADE** where applicable.
* Soft delete is handled via `is_deleted` flag in users.
* Indexing recommended on `email`, `user_id`, `job_id` for performance.

---

✅ This structure is **interview-ready**, **production-aligned**, and easy to convert into actual SQL DDL statements.







🔴 MUST-HAVE SUGGESTIONS (Very Important)

These don’t change your logic, they strengthen correctness & scalability.

1️⃣ Add a “Constraints & Relationships” section in the doc

Even if you don’t enforce all in SQL, documentation must mention them.

Example:

resume.user_id → users.id

job.posted_by → users.id

apply_job.user_id → users.id

apply_job.job_id → job.id

saved_job.user_id → users.id

saved_job.job_id → job.id

➡️ Interviewers LOVE seeing this written clearly.

2️⃣ Mention Indexing Strategy (You already have MUL)

Add a small section:

users.email → UNIQUE INDEX

job.title, job.location → SEARCH INDEX

apply_job.user_id, apply_job.job_id → PERFORMANCE

saved_job(user_id, job_id) → COMPOSITE INDEX

This shows real backend thinking.

3️⃣ Clarify Soft Delete Behavior

You already use:

users.is_deleted

Document this clearly:

Deleted users are not physically removed. Data is preserved for audit and recovery.

This is production-grade design.

🟡 GOOD-TO-HAVE SUGGESTIONS (Very Practical)
4️⃣ Normalize Status Fields (Doc-level suggestion)

You currently use:

status VARCHAR in apply_job

ENUM elsewhere

Suggestion in doc:

Status values are controlled at application level to allow future flexibility.

No DB change needed — just explanation.

5️⃣ Add Audit Meaning to Timestamps

Explain intent:

created_at → record creation

updated_at → last modification

applied_at, saved_at → user action timestamps

This avoids confusion in reviews.

6️⃣ Add “Security Notes” Section

Very impressive for freshers:

Passwords are hashed

IP & user_agent stored for fraud detection

Resume links stored as secured URLs

Even seniors forget this.

🟢 OPTIONAL (Only if You Want to Level Up)
7️⃣ Future Enhancement Notes (No DB change)

Add a small section like:

Multiple resumes per user

Job analytics (views, impressions)

Application status history table

Skill normalization table

This shows vision, not overengineering.

8️⃣ Minor Data Type Improvements (Optional)

Only suggest, don’t change now:

salary VARCHAR → future salary_min, salary_max

skill → separate skills master table

🧠 My Honest Verdict

For a student / fresher / intern:

This DB design is already above average.

With just:

Constraints section

Indexing explanation

Security notes

👉 It becomes interview-ready + production-credible.
lot more things are remained to add ill add those