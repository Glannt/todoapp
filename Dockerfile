# ==============================================================================
# Stage 1: Build the application
# ==============================================================================
FROM eclipse-temurin:23-jdk-alpine AS build
WORKDIR /app

# Install bash and findutils for gradle wrapper (Alpine's default sh can sometimes have issues, bash is safer)
RUN apk add --no-cache bash findutils

# Copy the gradle wrapper and configuration files
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
COPY settings.gradle .

# Ensure gradlew has execution permissions
RUN chmod +x gradlew

# Copy source code
COPY src/ src/

# Build the Spring Boot application (skipping tests for speed and self-containment)
RUN ./gradlew bootJar --no-daemon -x test

# ==============================================================================
# Stage 2: Runtime image
# ==============================================================================
FROM eclipse-temurin:23-jre-alpine
WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the jar from build stage
# Using wildcard to handle any name, renamed to app.jar for consistency
COPY --from=build --chown=spring:spring /app/build/libs/*-SNAPSHOT.jar app.jar

# Expose port (default Spring Boot port is 8080)
EXPOSE 8080

# Environment variables with defaults
ENV PORT=8080
ENV SPRING_PROFILES_ACTIVE=prod
ENV SPRING_DOCKER_COMPOSE_ENABLED=false

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
