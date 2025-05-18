FROM amazoncorretto:21 AS runtime

# Set working directory
WORKDIR /app

ARG JAR_FILE=./app/build/libs/app.jar

# Copy JAR from builder
COPY ${JAR_FILE} app.jar

# Expose application port (e.g., 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
