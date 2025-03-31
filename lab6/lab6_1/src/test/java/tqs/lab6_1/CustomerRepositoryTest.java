package tqs.lab6_1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Ensures test order
class CustomerRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
        .withUsername("postgres")
        .withPassword("test")
        .withDatabaseName("testdb")
        .waitingFor(org.testcontainers.containers.wait.strategy.Wait.forListeningPort()) // Ensure the container is fully up before tests
        .withExposedPorts(5432);  // Expose the PostgreSQL port (default is 5432)

    @Autowired
    private CustomerRepository customerRepository;

    // Dynamically setting properties for the Spring context
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop"); // Creates and drops schema
    }
    

    // Create a customer before each test
    @BeforeEach
    void setup() {
        customerRepository.deleteAll(); // Ensure clean state before tests
    }

    @Test
    @Order(1)
    void testInsertCustomer() {
        Customer customer = new Customer("John Doe", "john@example.com");
        customerRepository.save(customer);

        Customer foundCustomer = customerRepository.findByName("John Doe");
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getName()).isEqualTo("John Doe");
        assertThat(foundCustomer.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @Order(2)
    void testRetrieveCustomer() {
        Customer customer = new Customer("Jane Doe", "jane@example.com");
        customerRepository.save(customer);

        Customer foundCustomer = customerRepository.findByName("Jane Doe");
        assertThat(foundCustomer).isNotNull();
        assertThat(foundCustomer.getName()).isEqualTo("Jane Doe");
        assertThat(foundCustomer.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    @Order(3)
    void testUpdateCustomer() {
        // First insert a customer
        Customer customer = new Customer("Alice", "alice@example.com");
        customerRepository.save(customer);

        // Now update the customer
        Customer foundCustomer = customerRepository.findByName("Alice");
        foundCustomer.setEmail("newalice@example.com");
        customerRepository.save(foundCustomer);

        Customer updatedCustomer = customerRepository.findByName("Alice");
        assertThat(updatedCustomer.getEmail()).isEqualTo("newalice@example.com");
    }

    @Test
    @Order(4)
    void testRetrieveUpdatedCustomer() {
        // Retrieve the updated customer
        Customer customer = customerRepository.findByName("Alice");
        assertThat(customer).isNotNull();
        assertThat(customer.getEmail()).isEqualTo("newalice@example.com");
    }
}
