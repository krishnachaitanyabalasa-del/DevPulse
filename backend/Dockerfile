# Multi-stage Dockerfile for DevPulse Spring Boot Backend (Java 21)

# Stage 1: Build JAR artifact using Maven
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper and dependencies configuration
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copy application source code and seed files
COPY src ./src
COPY seed ./seed

# Build executable JAR
RUN ./mvnw clean package -DskipTests

# Stage 2: Production Runtime Image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Set dynamic port environment variable (default 8080)
ENV PORT=8080
EXPOSE 8080

# Copy built JAR artifact from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run Spring Boot application
ENTRYPOINT ["java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
