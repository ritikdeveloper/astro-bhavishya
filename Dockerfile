# ---- Build Stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ---- Run Stage ----
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Use port 10000 for Render, or 8080 if running elsewhere
EXPOSE 10000
ENV PORT=10000

CMD ["java", "-jar", "app.jar"]
