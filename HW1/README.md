# HomeWork 1 - Mid-term Assignment
## Meal Booking Web Application
### Project Overview
This project is a full-stack web application for managing meal reservations at the Moliceiro University campus. The application allows students to view available meal options for different restaurants, book reservations, and check reservation details. Additionally, it integrates weather forecasts to help students decide where to dine based on weather conditions.

### Features
- View Meal Plans: Students can browse the meal plans for different restaurants on campus.
- Weather Forecast Integration: Display the weather forecast for each restaurant’s available meal days.
- Reservation System: Users can book meal reservations in advance.
- Reservation Details: Users can check and cancel their reservations.
- Check-in Page for Staff: Restaurant staff can verify and mark reservations as used.
- REST API: Provides endpoints for interacting with the backend programmatically.
- Caching for Weather Data: Weather data is cached to optimize performance and reduce API calls.

### Technologies Used
- Backend: Spring Boot (Java)
- Frontend: HTML, JavaScript (or Thymeleaf for templating)
- Database: PostgreSQL or MySQL
- Weather API: Integration with an external weather API (e.g., OpenWeatherMap)
- Logging: SLF4J with Logback
- Testing Frameworks:
    - Unit Tests: JUnit
    - API Testing: REST Assured / Spring Boot MockMvc
    - Functional Testing: Selenium WebDriver (BDD tests)
    - Performance Testing: JMeter / Gatling
- Code Quality & CI/CD:
    - Code Analysis: SonarQube / Codacy
    - Continuous Integration: GitHub Actions / Jenkins

### Development Timeline Guide
1. Architecture & Planning
- Define REST API endpoints
- Design database schema
- Research and choose weather API
- Identify caching mechanism
2. Low-Fidelity Mockups & Frontend Design
- Create wireframes for the application
- Define the user journey and key interactions
- Implement basic UI components
3. Backend Development
- Implement REST API with Spring Boot
- Connect to the database and create models
- Integrate weather API and implement caching logic
4. Frontend Development
- Develop the web interface
- Connect interface with SpringBoot backend
5. Testing & Optimization
- Write and run unit tests
- Perform integration and functional tests
- Optimize API performance and caching
6. Finalizing & Deployment
- Prepare technical documentation
- Set up deployment environment (e.g., Docker, AWS, or Heroku)
- Submit the project and schedule presentation

### Submission Requirements
- Technical Report: Explains the adopted development strategy, test results, and SonarQube analysis.
- Code Repository: Hosted in the TQS Git repository under /HW1.
- Video Demonstration: Short screencast demonstrating the project features.
- Oral Presentation: Scheduled presentation of the project.