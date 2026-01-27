# --- Build stage ---
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy Maven files and download dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# --- Runtime stage ---
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the jar from build stage
COPY --from=build /app/target/help-desk-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8081

# Placeholder env variables
ENV DB_HOST=
ENV DB_PORT=
ENV DB_NAME=
ENV DB_USERNAME=
ENV DB_PASSWORD=
ENV OPENAI_API_KEY=

# Use Render’s PORT variable for Spring Boot
ENV SERVER_PORT=${PORT}

# Run Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]