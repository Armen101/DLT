# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Create data directory for H2 database (if using H2 in production)
RUN mkdir -p /app/data

EXPOSE 8080

# Use railway profile by default for Railway deployments
# Railway will inject DATABASE_URL and other variables at runtime
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=railway"]
