# Spring Security Guide For SkillSync Backend

This document explains how Spring Security works in this project, from beginner concepts to advanced production improvements.

The project uses JWT-based stateless authentication. Public routes such as login, signup, company signup, forgot password, reset password, public job search, and public company/review reads are available without a token. Protected routes require a valid `Authorization: Bearer <jwt>` header, and Spring Security checks the user's role before the request reaches protected business logic.

## Interview-Ready Explanation

Use this version instead of the rough explanation:

> I implemented JWT-based stateless authentication using Spring Security. Public APIs like login, signup, company signup, password reset, and public job search are allowed without authentication. Other APIs are protected through a `SecurityFilterChain`.
>
> During login, the controller creates a `UsernamePasswordAuthenticationToken` with the email and password. `AuthenticationManager` delegates authentication to `DaoAuthenticationProvider`, which loads the user through `CustomUserDetailsServiceImpl` and verifies the raw password against the BCrypt-hashed password using `PasswordEncoder`.
>
> After successful authentication, the backend generates a signed JWT containing the user's email as the subject, the user id, the roles, the issued time, and the expiry time. The frontend sends this token on later requests using the `Authorization: Bearer <token>` header.
>
> For every protected request, `JwtFilter` runs before Spring's username/password filter. It extracts the Bearer token, validates the signature and expiry through `JwtUtil`, converts the JWT role claims into Spring Security authorities, creates a `UsernamePasswordAuthenticationToken`, and stores it in `SecurityContextHolder`. After that, Spring Security applies route-level authorization rules from `SecurityConfig`, and controller/service-level rules enforce ownership checks such as "users can manage only their own applications" and "recruiters can manage only their own jobs."

Important correction:

Your original explanation said the filter "loads user details" for every secured request. In the current code, `JwtFilter` injects `UserDetailsService`, but does not call it during token validation. It trusts the roles present in the signed JWT. That is valid for many stateless systems, but the tradeoff is that role/status changes in the database do not affect already-issued tokens until they expire. A stronger version can reload the user from the database after JWT validation.

## Security Files In This Project

| File | Responsibility |
| --- | --- |
| `src/main/java/com/pravin/job_portal_backend/config/SecurityConfig.java` | Main Spring Security configuration: CORS, CSRF, route authorization, stateless session policy, authentication provider, password encoder, JWT filter registration |
| `src/main/java/com/pravin/job_portal_backend/filter/JwtFilter.java` | Reads `Authorization: Bearer ...`, validates JWT, creates `Authentication`, stores it in `SecurityContextHolder` |
| `src/main/java/com/pravin/job_portal_backend/utilis/JwtUtil.java` | Creates JWTs, verifies signatures, checks expiry, extracts username/user id/roles |
| `src/main/java/com/pravin/job_portal_backend/service/impl/CustomUserDetailsServiceImpl.java` | Loads users by email from the database for username/password authentication |
| `src/main/java/com/pravin/job_portal_backend/config/AdminBootstrapConfig.java` | Creates the first admin from environment-backed config if no admin exists |
| `src/main/java/com/pravin/job_portal_backend/service/impl/UserRegistrationServiceImpl.java` | Registers users, hashes passwords, restricts admin role assignment |
| `src/main/java/com/pravin/job_portal_backend/service/impl/PasswordResetServiceImpl.java` | Creates secure password reset tokens and resets passwords |
| `src/main/java/com/pravin/job_portal_backend/enums/Role.java` | Defines app roles: `USER`, `RECRUITER`, `COMPANY_ADMIN`, `COMPANY_EMPLOYEE`, `ADMIN` |

## Authentication Vs Authorization

Authentication answers:

```text
Who are you?
```

In this app, authentication happens when a user logs in with email and password. If the email exists and the BCrypt password check succeeds, the app returns a JWT.

Authorization answers:

```text
What are you allowed to do?
```

In this app, authorization happens after authentication. Spring Security checks endpoint rules like:

```java
.requestMatchers("/admin/**").hasRole("ADMIN")
.requestMatchers("/recruiter/**").hasRole("RECRUITER")
.requestMatchers("/user/**").hasRole("USER")
```

Service methods then add ownership rules:

```text
USER can edit only their own application.
RECRUITER can manage only jobs they posted.
COMPANY_ADMIN can manage employees only inside their own company.
ADMIN can manage platform-level data.
```

## Full Security Flow

### 1. Signup Flow

Main paths:

```text
POST /public/signup
POST /public/company/signup
```

Flow:

```text
Client sends signup request
        |
PublicController or CompanyPortalController receives request
        |
Service validates duplicate email/company
        |
PasswordEncoder hashes raw password with BCrypt
        |
Role is assigned
        |
User is saved in users table
        |
Response is returned
```

Important details:

- Candidate/recruiter signup uses `UserRegistrationServiceImpl`.
- Company signup creates a `Company` and a first `COMPANY_ADMIN` user.
- Passwords are never stored as plain text. They are stored as BCrypt hashes.
- `UserRegistrationServiceImpl` defaults missing roles to `USER`.
- `ADMIN` creation is restricted. Only an authenticated admin can create another admin, and the service allows only one admin user.
- `AdminBootstrapConfig` can create the first admin at startup from `APP_ADMIN_EMAIL` and `APP_ADMIN_PASSWORD` when no admin exists.

Production note:

Do not depend on committed fallback admin credentials in production. Always set admin credentials through environment variables or a secret manager, then rotate them after first use.

### 2. Login Flow

Main path:

```text
POST /public/login
```

Controller:

```java
authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
```

Detailed flow:

```text
Client sends email + password
        |
PublicController.login(...)
        |
AuthenticationManager.authenticate(...)
        |
DaoAuthenticationProvider
        |
CustomUserDetailsServiceImpl.loadUserByUsername(email)
        |
UserRepository.findByEmail(email)
        |
Spring Security UserDetails is created with:
  - username = email
  - password = BCrypt hash from DB
  - authorities = ROLE_USER / ROLE_ADMIN / etc.
        |
PasswordEncoder checks raw password against BCrypt hash
        |
If valid, PublicController generates JWT
        |
Response returns:
  - token
  - user DTO
```

What `AuthenticationManager` really does:

- It is the login entry point.
- It does not directly query the database.
- It delegates to configured authentication providers.
- In this project the provider is `DaoAuthenticationProvider`.

What `DaoAuthenticationProvider` does:

- Calls `UserDetailsService.loadUserByUsername(email)`.
- Gets the stored BCrypt password hash.
- Uses `PasswordEncoder.matches(rawPassword, storedHash)`.
- Returns an authenticated `Authentication` if the password is valid.

### 3. JWT Generation Flow

The JWT is created in `JwtUtil.generateToken(...)`.

Current claims:

| Claim | Meaning |
| --- | --- |
| `sub` | Subject. This project stores the user's email here |
| `id` | Database user id |
| `role` | List of Spring authorities such as `ROLE_USER` |
| `iat` | Issued-at timestamp |
| `exp` | Expiration timestamp |

JWT structure:

```text
header.payload.signature
```

Header:

```json
{
  "typ": "JWT",
  "alg": "HS256"
}
```

Payload example:

```json
{
  "sub": "candidate@example.com",
  "id": 10,
  "role": ["ROLE_USER"],
  "iat": 1710000000,
  "exp": 1710003600
}
```

Signature:

```text
HMAC(secret, base64Url(header) + "." + base64Url(payload))
```

The signature proves:

- The token was created by this backend.
- The payload was not modified.
- A fake token without the secret will be rejected.

The signature does not hide payload data. JWT payloads are encoded, not encrypted. Do not put secrets, passwords, OTPs, reset tokens, or sensitive profile data inside JWT claims.

### 4. Frontend Token Usage

After login, the frontend should call protected APIs like this:

```http
GET /role-profile/get-profile HTTP/1.1
Host: localhost:8080
Authorization: Bearer <jwt>
```

The backend expects exactly this style:

```text
Authorization: Bearer eyJhbGciOi...
```

If the header is missing, invalid, malformed, expired, or uses the wrong signing secret, the request stays unauthenticated.

### 5. Protected Request Flow

Main class:

```text
JwtFilter extends OncePerRequestFilter
```

Flow:

```text
HTTP request enters Spring Security filter chain
        |
JwtFilter runs before UsernamePasswordAuthenticationFilter
        |
Reads Authorization header
        |
Checks "Bearer " prefix
        |
Extracts compact JWT string
        |
JwtUtil.extractUsername(token)
        |
JwtUtil.validateToken(token)
        |
JwtUtil.extractRoles(token)
        |
Creates UsernamePasswordAuthenticationToken
        |
Stores auth object in SecurityContextHolder
        |
SecurityFilterChain checks route rules
        |
Controller runs if allowed
```

Current filter behavior:

```java
UsernamePasswordAuthenticationToken auth =
    new UsernamePasswordAuthenticationToken(username, null, authorities);

SecurityContextHolder.getContext().setAuthentication(auth);
```

This means controllers can receive:

```java
Authentication authentication
```

And then call:

```java
authentication.getName()
```

In this app, `authentication.getName()` is the email from the JWT subject.

### 6. Authorization Flow

Authorization happens in multiple layers.

Layer 1: Route-level rules in `SecurityConfig`

```java
.requestMatchers("/admin/**").hasRole("ADMIN")
.requestMatchers("/recruiter/**").hasRole("RECRUITER")
.requestMatchers("/company-portal/employees/**").hasRole("COMPANY_ADMIN")
.requestMatchers("/company-portal/**").hasAnyRole("COMPANY_ADMIN", "COMPANY_EMPLOYEE")
.anyRequest().authenticated()
```

Layer 2: Method-level rules through `@PreAuthorize`

Examples:

```java
@PreAuthorize("hasRole('USER')")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@PreAuthorize("hasRole('RECRUITER')")
```

Layer 3: Business ownership checks in services

Examples from this project:

- `JobApplyServiceImpl.assertUserOwnsApplication(...)`
- `JobApplyServiceImpl.assertRecruiterOwnsApplication(...)`
- `JobServiceImpl.getOwnedJob(...)`
- `CompanyPortalServiceImpl.requireCompanyAdmin(...)`
- `CompanyPortalServiceImpl.companyJob(...)`

Why ownership checks matter:

Route rules can prove "this person is a recruiter", but they cannot automatically prove "this recruiter owns job id 42." That must be checked in business logic.

### 7. Controller And Service Flow After Security

Example: candidate applies to a job.

```text
POST /apply/applications/apply?jobId=123
Authorization: Bearer <USER token>
        |
JwtFilter authenticates token
        |
SecurityConfig requires ROLE_USER
        |
@PreAuthorize("hasRole('USER')") checks again
        |
JobApplyController reads Authentication
        |
currentUserId(authentication) finds DB user by email
        |
JobApplyServiceImpl.applyForJob(userId, jobId, request)
        |
Service checks duplicate application
        |
Service saves application for the authenticated user
```

Critical security idea:

The application does not trust `userId` from the request body for applying to a job. It resolves the real user from the authenticated JWT identity.

### 8. Password Reset Flow

Main paths:

```text
POST /public/password/forgot
POST /public/password/reset
```

Flow:

```text
User requests password reset
        |
Backend normalizes email
        |
If user exists, old reset tokens are deleted
        |
SecureRandom creates a 32-byte URL-safe token
        |
Token is stored server-side with expiry
        |
Email sends reset link
        |
User submits token + new password
        |
Backend verifies token exists, is unused, and is not expired
        |
New password is BCrypt-hashed
        |
Token is marked used
```

Good security choices already present:

- Does not reveal whether an email exists in normal production response.
- Uses `SecureRandom`.
- Stores reset tokens server-side.
- Marks tokens as used.
- Deletes older tokens before creating a new one.
- Returns the reset link only outside `prod`.

## Roles In This Application

| Role enum | Spring authority | Typical access |
| --- | --- | --- |
| `USER` | `ROLE_USER` | Candidate features: apply, save jobs, resumes, job alerts, own applications |
| `RECRUITER` | `ROLE_RECRUITER` | Recruiter portal: own jobs, applicants for own jobs, talent search |
| `COMPANY_ADMIN` | `ROLE_COMPANY_ADMIN` | Company portal plus employee management and company profile updates |
| `COMPANY_EMPLOYEE` | `ROLE_COMPANY_EMPLOYEE` | Company portal job collaboration |
| `ADMIN` | `ROLE_ADMIN` | Platform management: users, jobs, applications, moderation-like actions |

Spring Security detail:

```java
hasRole("USER")
```

checks for this authority:

```text
ROLE_USER
```

So the code correctly creates authorities with the `ROLE_` prefix:

```java
new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
```

If you use:

```java
hasAuthority("USER")
```

then Spring checks exactly `USER`.

If you use:

```java
hasAuthority("ROLE_USER")
```

then Spring checks exactly `ROLE_USER`.

## Public And Protected Endpoints

Public endpoints:

```text
GET  /public/health-check
POST /public/signup
POST /public/company/signup
POST /public/login
POST /public/password/forgot
POST /public/password/reset
GET  /public/jobs/paginated
GET  /user/jobs/paginated
GET  /api/companies/**
GET  /api/company-reviews/**
```

Swagger endpoints:

```text
/v3/api-docs/**
/swagger-ui/**
/swagger-ui.html
/swagger-resources/**
/webjars/**
```

Swagger is allowed only when the active profile is not `prod`.

Protected role groups:

| Endpoint group | Access |
| --- | --- |
| `/admin/**` | `ADMIN` |
| `/recruiter/**` | `RECRUITER` |
| `/company-portal/employees/**` | `COMPANY_ADMIN` |
| `/company-portal/**` | `COMPANY_ADMIN`, `COMPANY_EMPLOYEE` |
| `/user/**` | Mostly `USER`, with selected routes also allowing `ADMIN` |
| `/apply/applications/apply` | `USER` |
| `/api/saved-jobs/**` | `USER` |
| `/api/resumes/**` | `USER` |
| `/api/job-alerts/**` | `USER` |
| `/api/users/**` | `ADMIN` |
| `/api/messages/**` | Authenticated app roles |
| `/api/interviews/**` | Authenticated app roles |
| `/email/**` | Authenticated app roles |
| `/role-profile/**` | Authenticated app roles |

Final fallback:

```java
.anyRequest().authenticated()
```

If a route is not explicitly listed, it still requires authentication.

## 401 Vs 403

401 Unauthorized:

```text
You are not authenticated.
```

Examples:

- No JWT on a protected route.
- Expired JWT.
- Invalid signature.
- Bad login credentials.

403 Forbidden:

```text
You are authenticated, but not allowed.
```

Examples:

- `USER` tries `/admin/users`.
- `RECRUITER` tries to edit another recruiter's job.
- `COMPANY_EMPLOYEE` tries employee management requiring `COMPANY_ADMIN`.

## Stateless Session Policy

This project uses:

```java
.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```

Meaning:

- Spring Security does not create or rely on an HTTP session.
- The server does not remember login state between requests.
- Each protected request must bring its JWT.
- Scaling is simpler because any backend instance can verify the token if it has the same signing secret.

Tradeoff:

- Token revocation is harder.
- Role/status changes do not automatically invalidate old tokens unless you add a revocation or token-version strategy.

## CSRF

This project disables CSRF:

```java
.csrf(csrf -> csrf.disable())
```

Why this is acceptable for the current design:

- The API uses Bearer tokens, not server-side browser sessions.
- The backend does not rely on automatically attached session cookies for auth.
- The frontend manually sends the `Authorization` header.

When CSRF should be enabled:

- If auth is stored in cookies and the browser automatically sends the cookie.
- If the backend uses session-based login.
- If JWT is placed in an HttpOnly cookie and used as the only auth mechanism.

Important rule:

```text
Bearer token in Authorization header: CSRF is usually less relevant.
Cookie-based auth: CSRF protection matters.
```

## CORS

CORS is configured in `SecurityConfig.corsConfigurationSource()`.

Allowed local origins:

```text
http://localhost:*
http://127.0.0.1:*
```

Additional origins:

```text
FRONTEND_URL
FRONTEND_URLS
```

Allowed methods:

```text
GET, POST, PUT, PATCH, DELETE, OPTIONS
```

Allowed headers:

```text
*
```

Exposed headers:

```text
Authorization
```

Important CORS points:

- CORS is enforced by browsers, not by Postman or backend-to-backend clients.
- CORS is not authentication.
- CORS does not replace JWT validation.
- `allowCredentials(true)` should be paired with a careful origin allowlist.
- In production, avoid broad wildcard-like patterns for public internet frontends.

## `permitAll` Vs `web.ignoring`

This project uses both:

```java
.requestMatchers("/public/**").permitAll()
```

and:

```java
web.ignoring().requestMatchers("/public/**", "/api/register/**")
```

Difference:

| Method | Meaning |
| --- | --- |
| `permitAll()` | Request still goes through the Spring Security filter chain, but no login is required |
| `web.ignoring()` | Request completely skips Spring Security filters |

Practical insight:

Use `permitAll()` for most public API routes because security headers, logging, CORS, and filters can still run. Use `web.ignoring()` mainly for static resources or truly external paths that do not need Spring Security at all.

In this app, `/public/**` is ignored, so the JWT filter is not involved for those routes. That is okay for login/signup, but remember that ignored routes bypass all Spring Security filters.

## Current JWT Filter Tradeoff

Current behavior:

```text
JWT is valid -> roles are extracted from JWT -> Authentication is created
```

The filter does not currently reload the user from the database.

Benefits:

- Fast.
- Stateless.
- Fewer database queries.
- Works well when token expiry is short.

Tradeoffs:

- If an admin changes a user's role, old tokens keep the old role until expiration.
- If a user is disabled, deleted, or soft-deleted, an old token may still authenticate unless services check the database later.
- Password changes do not automatically invalidate existing JWTs.

Stronger DB-backed variant:

```java
if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

    if (jwtUtil.validateToken(jwt)) {
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
```

Even stronger variant:

- Validate token signature and expiry.
- Load user from DB.
- Check user status is `ACTIVE`.
- Check user is not soft-deleted.
- Check token version matches the user's current token version.
- Use DB authorities instead of token authorities.

## SecurityContextHolder

`SecurityContextHolder` is where Spring Security stores the current request's authentication.

After `JwtFilter` runs, the current request can access:

```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
```

Controllers can also ask Spring to inject it:

```java
public ResponseEntity<?> getProfile(Authentication authentication) {
    String email = authentication.getName();
}
```

In this app:

```text
authentication.getName() = user email
authentication.getAuthorities() = ROLE_USER / ROLE_ADMIN / etc.
```

Important:

`SecurityContextHolder` is request-scoped through thread-local storage. Do not store it in fields. Read it when needed.

## Password Security

This project uses:

```java
new BCryptPasswordEncoder()
```

Why BCrypt is good:

- It is one-way hashing, not encryption.
- It automatically includes a salt.
- It is intentionally slow, making brute force attacks harder.
- The raw password is never stored.

Signup:

```java
user.setPassword(passwordEncoder.encode(user.getPassword()));
```

Login:

```text
PasswordEncoder.matches(rawPassword, storedBCryptHash)
```

Never do:

```java
if (rawPassword.equals(user.getPassword())) { ... }
```

Never store:

```text
plain text passwords
reversible encrypted passwords
passwords inside JWT claims
passwords in logs
```

## JWT Secret Management

Current config keys:

```yaml
app:
  jwt:
    secret: ${JWT_SECRET:...}
    expiration-ms: ${JWT_EXPIRATION_MS:3600000}
```

Production best practices:

- Set `JWT_SECRET` through environment variables or a secret manager.
- Use a long, random secret.
- Do not commit real production secrets.
- Rotate the secret if it was leaked.
- Use different secrets per environment.
- Keep token expiry short enough to reduce damage if a token is stolen.

If the HMAC secret changes:

```text
All existing JWTs signed with the old secret become invalid.
```

This can be useful for emergency logout-all behavior.

## Token Expiration

Current default:

```text
JWT_EXPIRATION_MS = 3600000
```

That is 1 hour.

Short expiry benefits:

- Limits damage if token is stolen.
- Makes role changes take effect sooner.

Short expiry downside:

- Users must log in again more often unless refresh tokens are implemented.

Common production pattern:

```text
Access token: 5 to 15 minutes
Refresh token: days or weeks, stored and revocable server-side
```

## Logout In Stateless JWT

With pure stateless JWT, logout usually means:

```text
Frontend deletes the token.
```

But the token is still technically valid until expiration.

Stronger logout options:

1. Short access token expiry.
2. Refresh tokens stored in DB and revoked on logout.
3. Token denylist for access tokens until they expire.
4. Token version field on the user.
5. Rotate `JWT_SECRET` in emergencies.

## Possible Ways To Implement Security

### Option 1: Session-Based Login

Flow:

```text
User logs in
Server creates HTTP session
Browser stores JSESSIONID cookie
Every request sends cookie
Server looks up session
```

Best for:

- Traditional server-rendered Spring MVC apps.
- Apps where frontend and backend are the same origin.

Pros:

- Server can immediately revoke sessions.
- Mature Spring Security support.
- Easy logout.

Cons:

- Needs session storage or sticky sessions when scaled.
- CSRF protection is required.
- Less convenient for separate SPA/mobile clients.

### Option 2: HTTP Basic Authentication

Flow:

```text
Every request sends Base64(email:password)
```

Best for:

- Internal tools.
- Simple machine-to-machine APIs.
- Local testing.

Pros:

- Very simple.
- Built into HTTP.

Cons:

- Sends credentials on every request.
- Must use HTTPS.
- Not ideal for user-facing apps.
- No clean logout except deleting credentials client-side.

### Option 3: JWT Access Token Only

This is the current style.

Flow:

```text
Login -> receive JWT -> send Bearer token on each protected request
```

Best for:

- Separate frontend/backend apps.
- Mobile apps.
- Stateless APIs.

Pros:

- Stateless.
- Easy horizontal scaling.
- No server session lookup.

Cons:

- Revocation is difficult.
- Stolen token works until expiry.
- Role changes may not apply until expiry.

### Option 4: JWT Access Token + Refresh Token

Recommended next step for this project.

Flow:

```text
Login returns:
  - short-lived access token
  - longer-lived refresh token

Access token is sent to APIs.
When access token expires, frontend calls refresh endpoint.
Backend validates refresh token and issues a new access token.
```

Store refresh tokens:

- In database as hashed tokens.
- With expiry.
- With `revokedAt`.
- With device/browser metadata.
- Rotate on every use.

Pros:

- Short access token lifetime.
- Users stay logged in.
- Logout can revoke refresh token.

Cons:

- More code.
- Refresh token theft must be handled carefully.

### Option 5: OAuth2 Login

Example providers:

```text
Google
GitHub
LinkedIn
Microsoft
```

Best for:

- "Login with Google" or social login.
- Enterprise login.

Pros:

- User does not create a new password.
- Provider handles identity verification.
- Can reduce password reset burden.

Cons:

- More setup.
- Need account linking.
- Need provider-specific callback handling.

### Option 6: OAuth2 Resource Server

Flow:

```text
External identity provider issues JWT
Spring Boot validates JWT as a resource server
```

Best for:

- Enterprise apps using Auth0, Keycloak, Okta, Cognito, Azure AD.

Pros:

- Authentication is delegated to a specialized identity provider.
- Supports standard token validation.
- Good for microservices.

Cons:

- More infrastructure.
- Role mapping can be complex.

### Option 7: Opaque Tokens

Flow:

```text
Client sends random token
Backend or auth server introspects token
```

Best for:

- Systems needing immediate revocation.
- Centralized auth servers.

Pros:

- Token contents are not visible to clients.
- Revocation is easier.

Cons:

- Requires token lookup or introspection.
- Less stateless than JWT.

### Option 8: API Keys

Flow:

```text
Client sends X-API-Key
Backend checks key
```

Best for:

- Partner APIs.
- Server-to-server integrations.

Pros:

- Simple for machines.
- Can be scoped per integration.

Cons:

- Not ideal for end-user login.
- Key leakage is common.
- Needs rotation and rate limits.

### Option 9: Method Security And Domain Permissions

Route role checks are not always enough. For advanced apps, use:

```java
@PreAuthorize("@permissionService.canEditJob(authentication, #jobId)")
```

Best for:

- Ownership checks.
- Team permissions.
- Company-level access.
- Admin override rules.

This project already does some of this manually in services. A permission service could centralize those checks later.

### Option 10: Attribute-Based Access Control

ABAC uses attributes, not only roles.

Example:

```text
Allow if:
  user.companyId == job.companyId
  and user.status == ACTIVE
  and job.status != ARCHIVED
```

Best for:

- Company portals.
- Multi-tenant applications.
- Advanced admin rules.

## Why JWT Was A Good Choice Here

This project has:

- Separate frontend and backend.
- REST APIs.
- Candidate, recruiter, company, and admin portals.
- Role-based route protection.
- Need for scalable stateless authentication.

JWT works well because:

- The frontend can store a token after login.
- Every request can prove identity without server session storage.
- The backend can verify the token signature quickly.
- Roles can be carried in the token for quick authorization.

## What Happens If Someone Modifies A JWT

Example attack:

```json
{
  "role": ["ROLE_ADMIN"]
}
```

If someone edits the payload, the signature no longer matches.

Result:

```text
JwtUtil validation fails.
Request remains unauthenticated.
Protected endpoint returns 401 or 403.
```

JWT payload is visible, but tamper-protected.

## What Happens If A Token Is Stolen

If someone steals a valid token, they can call APIs as that user until the token expires.

Mitigations:

- Use HTTPS everywhere.
- Keep token expiry short.
- Implement refresh token rotation.
- Avoid storing tokens in unsafe places.
- Add rate limiting.
- Add device/session management.
- Add token version or denylist.
- Avoid logging `Authorization` headers.

Frontend storage choices:

| Storage | Pros | Cons |
| --- | --- | --- |
| Memory | Harder to steal persistently | Lost on refresh |
| LocalStorage | Easy | Vulnerable if XSS occurs |
| SessionStorage | Easy, tab-scoped | Still vulnerable to XSS |
| HttpOnly secure cookie | Not readable by JS | Requires CSRF protection and cookie setup |

There is no perfect option. The right choice depends on frontend architecture and threat model.

## Production Improvements For This Project

High priority:

- Replace committed fallback secrets and admin fallback credentials with environment-only production values.
- Use `SPRING_PROFILES_ACTIVE=prod` in production.
- Use HTTPS only.
- Add refresh tokens.
- Add token revocation or token versioning.
- Reload user details in `JwtFilter` or check user status/version from DB.
- Enforce `UserStatus.ACTIVE` during authentication and protected requests.
- Add account lockout or throttling for repeated failed login attempts.
- Rate limit login, signup, forgot password, reset password, and email endpoints.
- Add security tests for every role.

Medium priority:

- Use a custom `AuthenticationEntryPoint` for consistent 401 JSON responses.
- Use a custom `AccessDeniedHandler` for consistent 403 JSON responses.
- Add audit logs for admin/company/recruiter sensitive actions.
- Add request id logging.
- Avoid returning exception internals in API responses.
- Add stricter CORS origins in production.
- Add validation to all auth DTOs.
- Add login success/failure audit events.
- Add password strength rules.
- Add email verification.

Advanced:

- Move authorization ownership checks into a central permission service.
- Add multi-device refresh token management.
- Add token family rotation and reuse detection.
- Add IP/device anomaly detection.
- Add admin-forced logout for a user.
- Add MFA for admin and company admins.
- Add OAuth2 login for Google/GitHub/LinkedIn.
- Add external identity provider support through Spring OAuth2 Resource Server.

## Suggested Stronger JWT Request Flow

Current:

```text
Validate JWT -> trust roles in JWT -> set Authentication
```

Stronger:

```text
Validate JWT signature and expiry
        |
Extract email
        |
Load user from DB
        |
Check status ACTIVE
        |
Check not deleted
        |
Check token version or last password change
        |
Use DB roles/authorities
        |
Set Authentication
```

Why this is stronger:

- Disabled users stop working immediately.
- Role changes take effect immediately.
- Password reset can invalidate old tokens.
- Soft-deleted users cannot keep using old tokens.

Possible user fields to support this:

```text
tokenVersion
passwordChangedAt
lastLogoutAt
failedLoginAttempts
lockedUntil
```

Possible JWT claims:

```text
sub = email
id = user id
ver = token version
iat = issued time
exp = expiry
```

Then validate:

```text
jwt.ver == user.tokenVersion
jwt.iat > user.passwordChangedAt
user.status == ACTIVE
user.isDeleted == false
```

## Testing Security

Security tests should verify both authentication and authorization.

Authentication test cases:

- Login succeeds with correct credentials.
- Login fails with wrong password.
- Protected route returns 401 without token.
- Protected route returns 401 with invalid token.
- Protected route returns 401 with expired token.
- Token contains expected subject, user id, role, and expiry.

Authorization test cases:

- `USER` cannot access `/admin/**`.
- `RECRUITER` cannot access `/user/**` candidate-only routes.
- `ADMIN` can access admin routes.
- `COMPANY_EMPLOYEE` cannot manage employees.
- `COMPANY_ADMIN` can manage company employees.
- Public endpoints work without token.
- Swagger is blocked in `prod`.

Ownership test cases:

- User cannot edit another user's application.
- Recruiter cannot update another recruiter's job.
- Recruiter cannot see applications for another recruiter's job.
- Company user cannot update another company's job.
- Admin can perform platform-level operations.

Password reset test cases:

- Forgot password does not reveal whether email exists.
- Reset token expires.
- Used reset token cannot be reused.
- Password is changed as BCrypt hash.
- Old token is deleted when a new one is requested.

## Beginner To Advanced Learning Path

### Level 1: Beginner Concepts

Learn:

- What authentication means.
- What authorization means.
- What roles and authorities mean.
- What HTTP status `401` and `403` mean.
- Why passwords must be hashed.
- What a filter is in a web request.

In this project, read:

- `PublicController.login`
- `UserRegistrationServiceImpl.saveUser`
- `CustomUserDetailsServiceImpl.loadUserByUsername`

### Level 2: Spring Security Basics

Learn:

- `SecurityFilterChain`
- `HttpSecurity`
- `requestMatchers`
- `permitAll`
- `authenticated`
- `hasRole`
- `hasAnyRole`
- `PasswordEncoder`
- `AuthenticationManager`
- `AuthenticationProvider`

In this project, read:

- `SecurityConfig.securityFilterChain`
- `SecurityConfig.authenticationProvider`
- `SecurityConfig.passwordEncoder`

### Level 3: JWT Basics

Learn:

- JWT header, payload, signature.
- Claims like `sub`, `iat`, `exp`.
- Difference between encoded and encrypted.
- HMAC signing.
- Bearer token header.

In this project, read:

- `JwtUtil.generateToken`
- `JwtUtil.validateToken`
- `JwtUtil.extractRoles`

### Level 4: Request Filter Flow

Learn:

- `OncePerRequestFilter`
- `SecurityContextHolder`
- `UsernamePasswordAuthenticationToken`
- `SimpleGrantedAuthority`
- Filter ordering with `addFilterBefore`

In this project, read:

- `JwtFilter.doFilterInternal`
- `SecurityConfig.addFilterBefore(...)`

### Level 5: Role-Based Authorization

Learn:

- Role names vs authorities.
- Why `hasRole("USER")` expects `ROLE_USER`.
- Route-level authorization.
- Method-level authorization.

In this project, read:

- `/admin/**` rules in `SecurityConfig`
- `/recruiter/**` rules in `SecurityConfig`
- `/company-portal/**` rules in `SecurityConfig`
- `@PreAuthorize` annotations in controllers

### Level 6: Ownership-Based Authorization

Learn:

- Why role checks are not enough.
- How to protect resources by owner.
- How to prevent IDOR attacks.

IDOR means insecure direct object reference. Example:

```text
User changes /applications/10 to /applications/11 and accesses someone else's data.
```

In this project, read:

- `JobApplyServiceImpl.assertUserOwnsApplication`
- `JobApplyServiceImpl.assertRecruiterOwnsApplication`
- `JobServiceImpl.getOwnedJob`
- `CompanyPortalServiceImpl.companyJob`

### Level 7: Production Security

Learn:

- Secret management.
- HTTPS.
- Refresh token rotation.
- Rate limiting.
- Account lockout.
- Audit logs.
- CORS hardening.
- Consistent error handling.
- Security tests.

In this project, improve:

- JWT filter DB validation.
- Refresh token flow.
- Login throttling.
- Admin MFA.
- Audit logs.

### Level 8: Advanced Architectures

Learn:

- OAuth2 login.
- OAuth2 resource server.
- OpenID Connect.
- Key rotation with asymmetric JWT signing.
- Multi-tenant authorization.
- Policy-based permission services.
- API gateway security.
- Distributed tracing and security audit events.

Possible future architecture:

```text
Frontend
   |
API Gateway
   |
Auth Service / Identity Provider
   |
Job Portal Backend
   |
Database
```

## Common Interview Questions

### Why did you use JWT?

Because the frontend and backend are separate, and the backend is a stateless REST API. JWT lets the frontend send a signed token on every request, and the backend can verify the token without storing server-side sessions.

### Why disable CSRF?

The current API uses Bearer tokens in the `Authorization` header and does not rely on browser session cookies for authentication. CSRF mainly protects cookie/session-based browser authentication. If we move JWT to cookies, we should add CSRF protection.

### Where is the password checked?

The password is checked by `DaoAuthenticationProvider`. It loads the user through `CustomUserDetailsServiceImpl`, then uses `BCryptPasswordEncoder` to compare the raw login password with the stored BCrypt hash.

### What is stored in the JWT?

The email as subject, user id, roles, issued-at time, and expiration time. The JWT does not store the password.

### What happens after JWT validation?

The app creates a Spring Security `Authentication` object and stores it in `SecurityContextHolder`. Then Spring Security can apply `hasRole` and `@PreAuthorize` checks.

### What is the difference between `hasRole` and `hasAuthority`?

`hasRole("USER")` checks for `ROLE_USER`. `hasAuthority("ROLE_USER")` checks the exact authority string. This project stores authorities like `ROLE_USER`.

### How do you prevent a user from applying as another user?

The application resolves the user from `Authentication.getName()`, which comes from the JWT subject. It does not trust a `userId` supplied by the client for the real applicant identity.

### How do you prevent recruiters from seeing other recruiters' applicants?

The service checks whether the job's `postedBy.email` matches the authenticated recruiter's email before returning or updating applications.

### What is the biggest current security improvement you would add?

I would add refresh tokens with rotation and revocation, and update `JwtFilter` to reload the user from the database so role/status changes take effect immediately.

## Clean Mental Model

Think of the app security as gates:

```text
Gate 1: Is this endpoint public?
        |
        yes -> controller can run
        no
        |
Gate 2: Is there a valid JWT?
        |
        no -> 401
        yes
        |
Gate 3: Does the JWT produce the required role?
        |
        no -> 403
        yes
        |
Gate 4: Does the user own this resource or have admin/company permission?
        |
        no -> 403
        yes
        |
Controller/service completes the action
```

## Quick Security Checklist

Use this before deployment:

- `JWT_SECRET` is set through environment variables.
- `APP_ADMIN_EMAIL` and `APP_ADMIN_PASSWORD` are set securely.
- Default local credentials are not used in production.
- `SPRING_PROFILES_ACTIVE=prod` is active.
- HTTPS is enabled.
- Swagger is blocked in production.
- CORS only allows trusted frontend origins.
- Password reset links are not returned in production responses.
- Logs do not print JWTs, passwords, reset tokens, or auth headers.
- Login and password reset are rate limited.
- All protected endpoints have role tests.
- Ownership checks are tested.
- Token expiry is reasonable.
- Refresh token/revocation strategy is planned.

## Final Summary

The security design of this application is:

```text
BCrypt password hashing
        +
Spring Security AuthenticationManager login
        +
JWT access token
        +
JwtFilter per request
        +
SecurityContextHolder
        +
role-based authorization
        +
service-level ownership checks
```

That is a strong foundation for a job portal backend. The most important advanced upgrades are refresh tokens, database-backed user status checks inside the JWT filter, login rate limiting, security tests, and production-grade secret management.
