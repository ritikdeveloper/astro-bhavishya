# PostgreSQL Setup for Development

## 1. Install PostgreSQL
Download and install PostgreSQL from: https://www.postgresql.org/download/windows/

**During installation:**
- Set password for `postgres` user (use: `password`)
- Port: `5432` (default)
- Install pgAdmin 4 (included)

## 2. Create Database using pgAdmin

1. **Open pgAdmin 4**
2. **Connect to PostgreSQL server:**
   - Server: localhost
   - Username: postgres
   - Password: password (or what you set during installation)

3. **Create Database:**
   - Right-click on "Databases"
   - Select "Create" → "Database..."
   - Database name: `astro_db`
   - Owner: postgres
   - Click "Save"

## 3. Update Configuration (if needed)

If you used different credentials, update `application-dev.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/astro_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

## 4. Run the Application

```bash
mvn spring-boot:run
```

## 5. Verify Database Connection

1. **Check application logs** for successful database connection
2. **In pgAdmin**, refresh the `astro_db` database to see created tables
3. **Test endpoint**: http://localhost:5000/api/auth/test

## 6. Database Management

**View Tables in pgAdmin:**
- Navigate to: astro_db → Schemas → public → Tables
- You should see tables like: userdata, astrologers, wallet, etc.

**Query Data:**
- Right-click on any table → "View/Edit Data" → "All Rows"

## Troubleshooting

**Connection refused:**
- Ensure PostgreSQL service is running
- Check Windows Services for "postgresql-x64-xx"

**Authentication failed:**
- Verify username/password in application-dev.properties
- Try connecting via pgAdmin first

**Port already in use:**
- Change server.port in application-dev.properties
- Or stop other applications using port 5000