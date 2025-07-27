# Use OpenJDK 21 slim image (Temurin JDK)
FROM eclipse-temurin:21-jdk-jammy

# Maintainer information
LABEL maintainer="pravin"

# JVM options (can be overridden during runtime)
ENV JAVA_OPTS=""

# Set working directory inside the container
WORKDIR /app

# Copy the Spring Boot JAR file from the host to the container
COPY target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the JAR file using the provided JVM options
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
