package com.pravin.job_portal_backend.config;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Central threading configuration for project-owned background work.
 *
 * Spring Boot already uses server threads for normal HTTP requests. This class
 * adds a separate, bounded thread pool for methods marked with @Async so slow
 * tasks such as email sending or admin report building do not block request
 * threads longer than needed.
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    public static final String APPLICATION_TASK_EXECUTOR = "applicationTaskExecutor";

    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);

    @Value("${app.async.core-pool-size:4}")
    private int corePoolSize;

    @Value("${app.async.max-pool-size:10}")
    private int maxPoolSize;

    @Value("${app.async.queue-capacity:100}")
    private int queueCapacity;

    /**
     * Named executor used by @Async("applicationTaskExecutor").
     *
     * ThreadPoolTaskExecutor is preferred over manually creating Thread objects
     * because Spring can manage lifecycle, queueing, graceful shutdown, and
     * exception handling for us.
     */
    @Bean(name = APPLICATION_TASK_EXECUTOR)
    public Executor applicationTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("job-portal-async-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    /**
     * Fallback executor for @Async methods that do not specify a bean name.
     */
    @Override
    public Executor getAsyncExecutor() {
        return applicationTaskExecutor();
    }

    /**
     * Logs exceptions from void @Async methods. CompletableFuture-returning
     * methods should still handle failures through future callbacks or join/get.
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (exception, method, params) ->
                log.error("Async method {} failed: {}", method.getName(), exception.getMessage(), exception);
    }
}
