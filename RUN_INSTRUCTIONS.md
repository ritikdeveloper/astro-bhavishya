# How to Run the Application

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL database access

## Quick Start (Development)

### 1. Set Active Profile
Change `application.properties`:
```properties
spring.profiles.active=dev
```

### 2. Run with Maven
```bash
cd AstroRitaChaturvedi_Backend
mvn spring-boot:run
```

### 3. Run with Java
```bash
mvn clean package
java -jar target/astro-rita-chaturvedi.jar
```

## Environment Variables (Production)

Set these before running:
```bash
export JDBC_DATABASE_URL="jdbc:postgresql://your-host:5432/your-db"
export JDBC_DATABASE_USERNAME="your-username"
export JDBC_DATABASE_PASSWORD="your-password"
export RAZORPAY_KEY_ID="your-key"
export RAZORPAY_KEY_SECRET="your-secret"
export JWT_SECRET="your-256-bit-secret"
export JWT_EXPIRATION="86400000"
```

## Application URLs
- Backend: http://localhost:5000
- Health Check: http://localhost:5000/actuator/health
- API Base: http://localhost:5000/api/auth

## Test Endpoints
```bash
# Health check
curl http://localhost:5000/actuator/health

# Test endpoint
curl http://localhost:5000/api/auth/test
```