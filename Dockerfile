# Base image for Gradle build
FROM gradle:8.10.2-jdk21 AS builder

# Set working directory
WORKDIR /app

# Copy project files to the container
COPY . .

# Build the project
RUN gradle clean build -x test

# Base image for running the application
FROM amazoncorretto:21 AS runtime

# Set working directory
WORKDIR /app

# Copy JAR from builder
COPY --from=builder /app/app/build/libs/app-*.jar app.jar

# Expose application port (e.g., 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
