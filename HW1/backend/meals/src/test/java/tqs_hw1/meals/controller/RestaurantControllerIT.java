package tqs_hw1.meals.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tqs_hw1.meals.model.Restaurant;
import tqs_hw1.meals.repository.RestaurantRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class RestaurantControllerIT {

    @SuppressWarnings("resource")
    @Container
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        restaurantRepository.deleteAll();
    }

    @Test
    void whenGetAllRestaurants_thenReturnEmptyList() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/restaurants")
        .then()
            .statusCode(200)
            .body("$", hasSize(0));
    }

    @Test
    void whenCreateRestaurant_thenReturnCreatedRestaurant() {
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setDescription("Test Description");
        restaurant.setAddress("Test Address");
        restaurant.setPhoneNumber("+351123456789");

        given()
            .contentType(ContentType.JSON)
            .body(restaurant)
        .when()
            .post("/restaurants")
        .then()
            .statusCode(201)
            .body("name", equalTo("Test Restaurant"))
            .body("description", equalTo("Test Description"))
            .body("address", equalTo("Test Address"))
            .body("phoneNumber", equalTo("+351123456789"));
    }

    @Test
    void whenGetRestaurantById_withExistingId_thenReturnRestaurant() {
        // Create a restaurant first
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant.setDescription("Test Description");
        restaurant.setAddress("Test Address");
        restaurant.setPhoneNumber("+351123456789");
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/restaurants/{id}", savedRestaurant.getId())
        .then()
            .statusCode(200)
            .body("name", equalTo("Test Restaurant"))
            .body("description", equalTo("Test Description"))
            .body("address", equalTo("Test Address"))
            .body("phoneNumber", equalTo("+351123456789"));
    }

    @Test
    void whenGetRestaurantById_withNonExistingId_thenReturn404() {
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/restaurants/{id}", 999L)
        .then()
            .statusCode(404);
    }

    @Test
    void whenUpdateRestaurant_withExistingId_thenReturnUpdatedRestaurant() {
        // Create a restaurant first
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Original Name");
        restaurant.setDescription("Original Description");
        restaurant.setAddress("Original Address");
        restaurant.setPhoneNumber("+351123456789");
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        // Update data
        Restaurant updateData = new Restaurant();
        updateData.setName("Updated Name");
        updateData.setDescription("Updated Description");
        updateData.setAddress("Updated Address");
        updateData.setPhoneNumber("+351987654321");

        given()
            .contentType(ContentType.JSON)
            .body(updateData)
        .when()
            .put("/restaurants/{id}", savedRestaurant.getId())
        .then()
            .statusCode(200)
            .body("name", equalTo("Updated Name"))
            .body("description", equalTo("Updated Description"))
            .body("address", equalTo("Updated Address"))
            .body("phoneNumber", equalTo("+351987654321"));
    }

    @Test
    void whenDeleteRestaurant_withExistingId_thenReturn204() {
        // Create a restaurant first
        Restaurant restaurant = new Restaurant();
        restaurant.setName("To Delete");
        restaurant.setDescription("To Delete");
        restaurant.setAddress("To Delete");
        restaurant.setPhoneNumber("+351123456789");
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        given()
            .contentType(ContentType.JSON)
        .when()
            .delete("/restaurants/{id}", savedRestaurant.getId())
        .then()
            .statusCode(204);

        // Verify restaurant is deleted
        given()
            .contentType(ContentType.JSON)
        .when()
            .get("/restaurants/{id}", savedRestaurant.getId())
        .then()
            .statusCode(404);
    }
} 