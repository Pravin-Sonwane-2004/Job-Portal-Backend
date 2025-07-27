# # Use OpenJDK 21 slim image (Temurin JDK)
# FROM eclipse-temurin:21-jdk-jammy

# # Maintainer information
# LABEL maintainer="pravin"

# # JVM options (can be overridden during runtime)
# ENV JAVA_OPTS=""

# # Set working directory inside the container
# WORKDIR /app

# # Copy the Spring Boot JAR file from the host to the container
# COPY target/*.jar app.jar

# # Expose the default Spring Boot port
# EXPOSE 8080

# # Run the JAR file using the provided JVM options
# ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]




# =====================
# Stage 1: Build JAR
# =====================
FROM maven:3.9-eclipse-temurin-21 AS builder

# Set working directory inside container
WORKDIR /app

# Copy all project files into the container
COPY . .

# Package the Spring Boot application (skip tests if needed)
RUN mvn clean package -DskipTests

# =====================
# Stage 2: Run JAR
# =====================
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Environment variable for custom JVM options
ENV JAVA_OPTS=""

EXPOSE 8080

# Run the JAR
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
