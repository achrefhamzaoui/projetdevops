# Use a lightweight OpenJDK image as base
FROM openjdk:17-jdk-alpine
LABEL authors="hamza"
# Set a working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
COPY target/*.jar app.jar

# Expose the port your Spring Boot app runs on (default 8080)
EXPOSE 8086

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]