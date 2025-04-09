package tqs_hw1.meals.cucumber;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import tqs_hw1.meals.model.Restaurant;
import tqs_hw1.meals.repository.RestaurantRepository;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Response response;
    private Restaurant testRestaurant;
    private Map<String, String> restaurantDetails;

    @Before
    public void setup() {
        RestAssured.port = port;
        restaurantDetails = new HashMap<>();
    }

    @Given("the restaurant database is empty")
    public void clearDatabase() {
        restaurantRepository.deleteAll();
    }

    @When("I request all restaurants")
    public void requestAllRestaurants() {
        response = given()
            .contentType(ContentType.JSON)
        .when()
            .get("/restaurants");
    }

    @Then("I should receive an empty list")
    public void verifyEmptyList() {
        response.then()
            .body("$", hasSize(0));
    }

    @And("the response status should be {int}")
    public void verifyResponseStatus(int statusCode) {
        response.then()
            .statusCode(statusCode);
    }

    @When("I create a restaurant with the following details:")
    public void createRestaurant(Map<String, String> details) {
        restaurantDetails = details;
        Restaurant restaurant = new Restaurant();
        restaurant.setName(details.get("name"));
        restaurant.setDescription(details.get("description"));
        restaurant.setAddress(details.get("address"));
        restaurant.setPhoneNumber(details.get("phoneNumber"));

        response = given()
            .contentType(ContentType.JSON)
            .body(restaurant)
        .when()
            .post("/restaurants");
    }

    @Then("the restaurant should be created successfully")
    public void verifyRestaurantCreated() {
        response.then()
            .statusCode(201);
    }

    @And("the restaurant details should match the input")
    public void verifyRestaurantDetails() {
        response.then()
            .body("name", equalTo(restaurantDetails.get("name")))
            .body("description", equalTo(restaurantDetails.get("description")))
            .body("address", equalTo(restaurantDetails.get("address")))
            .body("phoneNumber", equalTo(restaurantDetails.get("phoneNumber")));
    }

    @Given("a restaurant exists with the following details:")
    public void createTestRestaurant(Map<String, String> details) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(details.get("name"));
        restaurant.setDescription(details.get("description"));
        restaurant.setAddress(details.get("address"));
        restaurant.setPhoneNumber(details.get("phoneNumber"));

        testRestaurant = restaurantRepository.save(restaurant);
        restaurantDetails = details;
    }

    @When("I request the restaurant by its ID")
    public void requestRestaurantById() {
        response = given()
            .contentType(ContentType.JSON)
        .when()
            .get("/restaurants/{id}", testRestaurant.getId());
    }

    @Then("I should receive the restaurant details")
    public void verifyRestaurantRetrieved() {
        response.then()
            .body("name", equalTo(restaurantDetails.get("name")))
            .body("description", equalTo(restaurantDetails.get("description")))
            .body("address", equalTo(restaurantDetails.get("address")))
            .body("phoneNumber", equalTo(restaurantDetails.get("phoneNumber")));
    }

    @When("I update the restaurant with the following details:")
    public void updateRestaurant(Map<String, String> details) {
        Restaurant updateData = new Restaurant();
        updateData.setName(details.get("name"));
        updateData.setDescription(details.get("description"));
        updateData.setAddress(details.get("address"));
        updateData.setPhoneNumber(details.get("phoneNumber"));

        response = given()
            .contentType(ContentType.JSON)
            .body(updateData)
        .when()
            .put("/restaurants/{id}", testRestaurant.getId());

        restaurantDetails = details;
    }

    @Then("the restaurant should be updated successfully")
    public void verifyRestaurantUpdated() {
        response.then()
            .statusCode(200);
    }

    @And("the restaurant details should be updated")
    public void verifyUpdatedDetails() {
        response.then()
            .body("name", equalTo(restaurantDetails.get("name")))
            .body("description", equalTo(restaurantDetails.get("description")))
            .body("address", equalTo(restaurantDetails.get("address")))
            .body("phoneNumber", equalTo(restaurantDetails.get("phoneNumber")));
    }

    @When("I delete the restaurant")
    public void deleteRestaurant() {
        response = given()
            .contentType(ContentType.JSON)
        .when()
            .delete("/restaurants/{id}", testRestaurant.getId());
    }

    @Then("the restaurant should be deleted successfully")
    public void verifyRestaurantDeleted() {
        response.then()
            .statusCode(204);
    }

    @And("the restaurant should not exist anymore")
    public void verifyRestaurantDoesNotExist() {
        assertFalse(restaurantRepository.existsById(testRestaurant.getId()));
    }
} 