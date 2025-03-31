import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class JsonPlaceholderApiTest {

    @BeforeAll
    public static void setup() {
        // Base URL for the API
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    // Test to verify that the endpoint to list all ToDos is available (status code 200)
    @Test
    void testListAllToDos() {
        given().
        when().
            get("/todos").
        then().
            statusCode(200);  // Assert that status code is 200
    }

    // Test to verify that when querying for ToDo #4, the title is "et porro tempora"
    @Test
    void testGetToDoById() {
        given().
        when().
            get("/todos/4").
        then().
            statusCode(200).  // Assert that status code is 200
            body("title", equalTo("et porro tempora"));  // Assert that the title is as expected
    }

    // Test to verify that when listing all "todos", id #198 and #199 are in the results
    @Test
    void testListAllToDosWithSpecificIds() {
        given().
        when().
            get("/todos").
        then().
            statusCode(200).  // Assert that status code is 200
            body("id", hasItem(198))  // Assert that id #198 is in the list
            .body("id", hasItem(199));  // Assert that id #199 is in the list
    }

    // Test to verify that when listing all "todos", the results come in less than 2 seconds
    @Test
    void testListAllToDosResponseTime() {
        given().
        when().
            get("/todos").
        then().
            statusCode(200).  // Assert that status code is 200
            time(lessThan(2000L));  // Assert that the response time is less than 2000 milliseconds (2 seconds)
    }
}
