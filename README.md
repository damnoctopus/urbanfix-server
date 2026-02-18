# UrbanFix Server - Spring Boot Backend

A complete Spring Boot REST API backend for the UrbanFix urban complaint management system with Gemini AI-powered auto-routing.

## Features

- **User Management**: User registration and JWT-based authentication
- **Complaint Management**: Submit, retrieve, and track complaints
- **Gemini AI Auto-Routing**: Intelligent complaint routing to departments using Google's Gemini API
- **Notification System**: Real-time notifications for complaint updates
- **Image Upload & Processing**: Store and process complaint images
- **PostgreSQL Database**: Persistent data storage on AWS RDS
- **Security**: JWT authentication, password hashing with BCrypt

## Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL
- **Authentication**: JWT (jjwt 0.12.3)
- **API Client**: Spring WebClient for Gemini integration
- **ORM**: Hibernate/JPA
- **Build**: Maven

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (or use AWS RDS)
- Git

## Installation & Setup

### 1. Clone or navigate to project
```bash
cd C:\Users\adira\studio_projects\urbanfix\urbanfix-server
```

### 2. Configure Database Connection
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://urbanfix-db.cdaymsco6ihx.ap-south-1.rds.amazonaws.com:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=I5may2DPqPNuZnq80KIV
```

### 3. Set Gemini API Key
Update `src/main/resources/application.properties`:
```properties
gemini.api-key=AIzaSyA_VwOY4Bc7e3UOm0i_LBjdsGrkASLkSxY
```

### 4. Build the project
```bash
mvn clean install
```

### 5. Run the application
```bash
mvn spring-boot:run
```

The server will start on `http://localhost:8000` using the single default profile.

## API Endpoints

### Authentication
- **POST** `/api/auth/register` - Register new user
- **POST** `/api/auth/login` - Login and get JWT token

### Complaints
- **POST** `/api/complaints` - Submit a complaint (with image optional)
- **GET** `/api/complaints/{id}` - Get complaint details
- **GET** `/api/complaints/user/{userId}` - Get all complaints by user
- **GET** `/api/complaints` - Get all complaints

### Notifications
- **GET** `/api/notifications` - Get all notifications for current user
- **GET** `/api/notifications/{userId}` - Get notifications for specific user
- **POST** `/api/notifications/{id}/read` - Mark notification as read

### Health Check
- **GET** `/api/health` - Health check endpoint

## Database Schema

### users table
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### complaints table
```sql
CREATE TABLE complaints (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    description TEXT NOT NULL,
    category VARCHAR(100) NOT NULL,
    department VARCHAR(100) NOT NULL,
    priority DECIMAL(3,2) NOT NULL,
    image_path VARCHAR(255),
    status VARCHAR(50) DEFAULT 'open',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### notifications table
```sql
CREATE TABLE notifications (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    complaint_id BIGINT REFERENCES complaints(id),
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Auto-Routing with Gemini

The system uses Google's Gemini API to intelligently route complaints to appropriate departments:

**Supported Departments:**
- Roads (potholes, damaged roads)
- Sanitation (waste, cleanliness)
- Water (supply, leaks)
- Electricity (power, streetlights)
- Parks (maintenance, repairs)
- General (other issues)

When a complaint is submitted:
1. Description and category are sent to Gemini AI
2. AI analyzes and suggests the best department
3. If Gemini fails, keyword matching fallback is used
4. Complaint is routed and user is notified

## Environment Variables

Create `.env` file or set system environment variables:
```
DATABASE_URL=jdbc:postgresql://host:5432/urbanfix
DB_USERNAME=postgres
DB_PASSWORD=your_password
JWT_SECRET=your-secret-key
GEMINI_API_KEY=your-gemini-key
```

## Development

### Running Tests
```bash
mvn test
```

### Building for Production
```bash
mvn clean package -DskipTests
```

This creates `target/urbanfix-server-1.0.0.jar`

### Running JAR
```bash
java -jar target/urbanfix-server-1.0.0.jar
```

## Docker Support (Optional)

```dockerfile
FROM openjdk:17-slim
WORKDIR /app
COPY target/urbanfix-server-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t urbanfix-server .
docker run -p 8080:8080 --env-file .env urbanfix-server
```

## Troubleshooting

### Database Connection Error
- Verify RDS instance is running
- Check security group allows outbound on port 5432
- Confirm DATABASE_URL is correct

### Gemini API Error
- Verify GEMINI_API_KEY is set correctly
- Check API quota in Google Cloud Console
- System will fallback to keyword-based routing if API fails

### Port Already in Use
Change port in `application.properties`:
```properties
server.port=8081
```
_Example above uses `8081`, but you can set any free port; the default in this repo is `8000`._

## Project Structure

```
urbanfix-server/
├── src/
│   ├── main/
│   │   ├── java/com/urbanfix/
│   │   │   ├── UrbanFixApplication.java
│   │   │   ├── config/          # Spring security, CORS config
│   │   │   ├── controller/      # REST endpoints
│   │   │   ├── service/         # Business logic
│   │   │   ├── repository/      # Database access
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── dto/             # Data transfer objects
│   │   │   └── exception/       # Custom exceptions
│   │   └── resources/
│   │       └── application.properties
│   └── test/java/com/urbanfix/  # Unit tests
├── pom.xml                      # Maven dependencies
└── README.md
```

## Key Classes

- **UrbanFixApplication**: Main Spring Boot entry point
- **User**: User entity with auth details
- **Complaint**: Complaint entity with routing info
- **Notification**: Notification entity for updates
- **JwtTokenProvider**: JWT generation and validation
- **GeminiService**: Gemini API integration
- **ComplaintService**: Complaint business logic
- **AuthService**: Authentication logic

## Next Steps

1. Create repositories (UserRepository, ComplaintRepository)
2. Implement service layer for all entities
3. Build REST controllers for all endpoints
4. Add JWT security configuration
5. Implement image upload functionality
6. Add comprehensive error handling
7. Create unit tests
8. Deploy to production

## License

MIT License
