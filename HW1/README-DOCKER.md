# Dockerized TQS Meal Booking Application

This document provides instructions on how to run the TQS Meal Booking Application using Docker.

## Architecture Overview

The application is composed of several microservices:

- **Frontend**: A web interface built with Vite/React, served through Nginx
- **Meals Service**: Spring Boot application for meal reservations and restaurant management
- **Users Service**: Spring Boot application for user authentication and management
- **MySQL Databases**: Two separate MySQL instances for meals and users data
- **Redis**: For caching weather data

## Prerequisites

- Docker and Docker Compose installed on your machine
- Git to clone the repository

## Getting Started

1. Clone the repository:
   ```
   git clone <repository-url>
   cd HW1
   ```

2. Set up the environment variables by copying the example file and modifying it:
   ```
   cp .env.example .env
   ```
   
   Open the `.env` file and customize the values according to your environment, especially:
   - `MYSQL_ROOT_PASSWORD` - Root password for both MySQL instances
   - `MYSQL_MEALS_PASSWORD` - Password for meals database user
   - `MYSQL_USERS_PASSWORD` - Password for users database user
   - `WEATHER_API_KEY` - Your weather API key from [Visual Crossing Weather](https://www.visualcrossing.com/weather-api)

3. Start the application:
   ```
   docker-compose up -d
   ```

   The first run will build the images which might take a few minutes.

4. Verify that all services are running:
   ```
   docker-compose ps
   ```

## Service Details

### Database Configuration

- **Meals Database**:
  - Host: `mysql-meals`
  - Port: `3306` (exposed as `3306` on host)
  - Database: `tqs_meals`
  - User: `meals_user`

- **Users Database**:
  - Host: `mysql-users`
  - Port: `3306` (exposed as `3307` on host)
  - Database: `tqs_users`
  - User: `users_user`

### Backend Services

- **Meals Service**:
  - URL: http://localhost:8080/api
  - Provides endpoints for restaurant and meal reservation management
  - Connects to the meals database and Redis

- **Users Service**:
  - URL: http://localhost:8081/api
  - Provides endpoints for user management
  - Connects to the users database

### Frontend 

- **Web Interface**:
  - URL: http://localhost:8090
  - Communicates with backend services through API calls

## Logs and Monitoring

- View service logs:
  ```
  docker-compose logs -f <service-name>
  ```

  Replace `<service-name>` with one of: `meals-service`, `users-service`, `frontend`, `mysql-meals`, `mysql-users`, `redis`

- The logs are also persisted to the `./logs` directory on your host machine

## Stopping the Application

- To stop all services while preserving data:
  ```
  docker-compose down
  ```

- To stop all services and remove all data (clean start next time):
  ```
  docker-compose down -v
  ```

## Data Persistence

- Meals MySQL data is stored in a Docker volume named `mysql-meals-data`
- Users MySQL data is stored in a Docker volume named `mysql-users-data`
- Redis data is stored in a Docker volume named `redis-data`

## Troubleshooting

1. If a service fails to start, check its logs:
   ```
   docker-compose logs <service-name>
   ```

2. To rebuild a service after making changes:
   ```
   docker-compose build <service-name>
   docker-compose up -d <service-name>
   ```

3. To reset a specific service:
   ```
   docker-compose rm -sf <service-name>
   docker-compose up -d <service-name>
   ```

4. If you need to connect to a database from your host machine:
   - For meals database: `mysql -h 127.0.0.1 -P 3306 -u meals_user -p tqs_meals`
   - For users database: `mysql -h 127.0.0.1 -P 3307 -u users_user -p tqs_users`

5. If the frontend container fails with "port is already allocated":
   - Check if another service is using port 8090
   - Change the `FRONTEND_PORT` value in the `.env` file 