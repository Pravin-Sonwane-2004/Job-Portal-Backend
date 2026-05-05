# Async Threading And Garbage Collection

This project uses Spring-managed threading for background work and lets the JVM manage garbage collection.

## Why We Added Async Threading

Spring Boot already has request threads through the embedded web server. Every API request runs on one of those server threads.

For slow, non-critical work, we use a separate Spring async executor so the request thread can return faster. In this project, the current async use cases are:

| Use case | Class | Method |
| --- | --- | --- |
| Welcome email after signup | `EmailServiceImpl` | `sendEmailAsync(...)` |
| Admin application profile projection | `JobApplyServiceImpl` | `getAllApplicationsWithProfiles()` |

## Main Classes And References

| File | Purpose |
| --- | --- |
| `src/main/java/com/pravin/job_portal_backend/config/AsyncConfig.java` | Enables Spring async support and creates the shared thread pool |
| `src/main/java/com/pravin/job_portal_backend/service/interfaces/EmailService.java` | Service interface used by controllers/services that need email |
| `src/main/java/com/pravin/job_portal_backend/service/impl/EmailServiceImpl.java` | Sends mail synchronously or asynchronously |
| `src/main/java/com/pravin/job_portal_backend/service/interfaces/JobApplyService.java` | Application service interface, including the async admin projection method |
| `src/main/java/com/pravin/job_portal_backend/service/impl/JobApplyServiceImpl.java` | Builds application DTOs on the async executor |
| `src/main/java/com/pravin/job_portal_backend/controller/PublicController.java` | Calls async welcome email after signup |
| `src/main/java/com/pravin/job_portal_backend/controller/AdminController.java` | Returns the service `CompletableFuture` so Spring completes the response later |

## How The Async Flow Works

1. `AsyncConfig` has `@EnableAsync`, which tells Spring to look for methods marked with `@Async`.
2. `AsyncConfig#applicationTaskExecutor()` creates a bounded `ThreadPoolTaskExecutor`.
3. Async methods use `@Async(AsyncConfig.APPLICATION_TASK_EXECUTOR)` to run on that executor.
4. The method returns `CompletableFuture<T>`, so the caller can either wait for the result or attach failure handling.
5. Controllers can return `CompletableFuture<ResponseEntity<?>>` when the HTTP response should complete after the background task finishes.

Example from this project:

```java
@Async(AsyncConfig.APPLICATION_TASK_EXECUTOR)
public CompletableFuture<Boolean> sendEmailAsync(EmailRequest email) {
    sendEmail(email);
    return CompletableFuture.completedFuture(true);
}
```

## Important Rules

- Do not create raw threads with `new Thread(...)`.
- Prefer `@Async` and the shared executor from `AsyncConfig`.
- Use async only for work that can safely happen outside the request thread.
- Keep database reads inside a transaction when an async method uses JPA entities or relationships.
- Use `.exceptionally(...)`, `.handle(...)`, `.join()`, or `.get()` when the caller needs to observe async failure.

## Configurable Properties

The async executor is configured in `application.yml`:

```yaml
app:
  async:
    core-pool-size: ${APP_ASYNC_CORE_POOL_SIZE:4}
    max-pool-size: ${APP_ASYNC_MAX_POOL_SIZE:10}
    queue-capacity: ${APP_ASYNC_QUEUE_CAPACITY:100}
```

Meaning:

| Property | Meaning |
| --- | --- |
| `core-pool-size` | Normal number of async worker threads kept ready |
| `max-pool-size` | Maximum async worker threads when the queue fills |
| `queue-capacity` | Number of waiting async tasks before Spring creates more threads up to max |

## Garbage Collection

Garbage collection is automatic in Java. This project does not call `System.gc()` and should not manually force GC.

The correct project approach is:

- Let the JVM collect unused objects automatically.
- Avoid holding large objects in static fields or long-lived collections.
- Tune JVM memory only when there is real evidence from logs or monitoring.
- Use GC logs in production-like debugging when memory behavior needs investigation.

Useful JVM options when needed:

```text
-Xms512m -Xmx1024m
-Xlog:gc*
```

`-Xms` sets initial heap size, `-Xmx` sets maximum heap size, and `-Xlog:gc*` prints GC activity for diagnosis.

## What Not To Do

Avoid this:

```java
System.gc();
```

Calling `System.gc()` asks the JVM to run GC, but it can hurt performance and does not guarantee better memory behavior. The JVM is better at deciding when collection should happen.
