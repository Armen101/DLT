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

# Use railway profile if DATABASE_URL is set, otherwise use prod profile with H2
ENTRYPOINT ["sh", "-c", "if [ -n \"$DATABASE_URL\" ]; then java -jar app.jar --spring.profiles.active=railway; else java -jar app.jar --spring.profiles.active=prod; fi"]
