# URL Shortener Application

Welcome to the URL Shortener application! This Java-based project leverages the latest version of Spring Boot and Java 21 to provide a robust and efficient URL shortening service. Below, you'll find key information on the project's structure, features, and deployment.

## Project Overview

1. **Technology Stack:**
   - Developed using the latest version of Spring Boot and Java 21.

2. **Database:**
   - Main database engine: Postgres
   - Shortened URLs database: Redis

3. **Authentication:**
   - JWT (JSON Web Token) authentication is implemented for secure user access.

4. **Profiles:**
   - Two profiles: "user" and "viewer," each running on different ports.

5. **Environment Variables:**
   - Set the following environment variables before running the project:
     - `JDBC_URL`: JDBC URL for Postgres database
     - `DB_USER`: Database username
     - `DB_PASS`: Database password
     - `REDIS_HOST`: Redis server host
     - `REDIS_PASS`: Redis server password
     - `REDIS_PORT`: Redis server port
     - `REDIS_SSL`: Enable SSL for Redis (true/false)

6. **Testing:**
   - Sample test methods are provided, but more unit tests are needed. Consider adding end-to-end tests in the future.

7. **Swagger Documentation:**
   - Two Swagger pages are available for user and viewer profiles.
   - Access Swagger UI at `http://{host}:{port}/swagger-ui/index.html`

8. **Authentication Requirements:**
   - User endpoints require authentication.
   - Viewer endpoints do not require authentication.

9. **URL Limits:**
   - Each user can add up to 10 unique URLs.
   - URL lifetime is set to 365 days from the last use, adjustable via properties file.

10. **Scheduled Maintenance:**
    - Inactive URLs are removed from the database every midnight.

11. **Query Strings:**
    - The application supports added query strings to shortened URLs.

## Getting Started

1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/url-shortener.git
   ```

2. Set the required environment variables.

3. Build and run the application:

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

4. Access Swagger UI for documentation and testing.

## Contributing

Feel free to contribute by opening issues, submitting pull requests, or suggesting improvements. Let's make this URL Shortener even better together!

## License

This project is licensed under the [MIT License](LICENSE).
