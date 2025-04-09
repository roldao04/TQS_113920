# HomeWork 1 - Mid-term Assignment
## Meal Booking Web Application
### Project Overview
This project is a full-stack web application for managing meal reservations at the Moliceiro University campus. The application allows students to view available meal options for different restaurants, book reservations, and check reservation details. Additionally, it integrates weather forecasts to help students decide where to dine based on weather conditions.

### Features
- View Meal Plans: Students can browse the meal plans for different restaurants on campus.
- Weather Forecast Integration: Display the weather forecast for each restaurant's available meal days.
- Reservation System: Users can book meal reservations in advance.
- Reservation Details: Users can check and cancel their reservations.
- Check-in Page for Staff: Restaurant staff can verify and mark reservations as used.
- REST API: Provides endpoints for interacting with the backend programmatically.
- Caching for Weather Data: Weather data is cached to optimize performance and reduce API calls.

### Technologies Used
- Backend: Spring Boot (Java)
- Frontend: Vite/React
- Database: MySQL
- Weather API: Integration with Visual Crossing Weather API
- Caching: Redis for weather data
- Logging: SLF4J with Logback
- Testing Frameworks:
    - Unit Tests: JUnit
    - API Testing: REST Assured / Spring Boot MockMvc
    - Functional Testing: Selenium WebDriver (BDD tests)
    - Performance Testing: JMeter / Gatling
- Code Quality & CI/CD:
    - Code Analysis: SonarQube / Codacy
    - Containerization: Docker & Docker Compose

### Getting Started

#### Prerequisites
- Docker and Docker Compose installed on your system
- Git for cloning the repository

#### Installation and Running with Docker

1. Clone the repository:
```bash
git clone https://github.com/roldao04/TQS_113920.git
cd TQS_113920/HW1
```

2. Create a `.env` file in the root directory with the following environment variables:
```env
# MySQL Configuration
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_DATABASE=moliceiro_canteen
MYSQL_USER=your_username
MYSQL_PASSWORD=your_password

# Spring Boot Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/moliceiro_canteen
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password

# Weather API Configuration
WEATHER_API_KEY=your_weather_api_key

# Redis Configuration
REDIS_HOST=redis
REDIS_PORT=6379
```

3. Build and run the containers:
```bash
docker-compose up --build
```

The application will be available at:
- Frontend: http://localhost:8090
- Meals Service API: http://localhost:8080
- Users Service API: http://localhost:8081

### API Documentation
#### Meals Service API
The Meals Service provides endpoints for managing restaurants, meals, and reservations.

Access the Swagger UI documentation at:
```
http://localhost:8080/api/swagger-ui/index.html
```

#### Users Service API
The Users Service handles user authentication and management.

Access the Swagger UI documentation at:
```
http://localhost:8081/api/swagger-ui/index.html
```

### Project Structure
```
HW1/
├── backend/
│   ├── meals/          # Meals service (Spring Boot)
│   └── users/          # Users service (Spring Boot)
├── frontend/
│   └── moliceiro_canteen/  # React frontend
├── docker-compose.yml  # Docker composition
└── README.md          # This file
```

### Development

#### Running Services Individually

##### Backend Services
1. Meals Service:
```bash
cd backend/meals
mvn spring-boot:run
```

2. Users Service:
```bash
cd backend/users
mvn spring-boot:run
```

##### Frontend
```bash
cd frontend/moliceiro_canteen
npm install
npm run dev
```