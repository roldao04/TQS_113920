# Lab3.1
## a)
AssertJ provides an expressive API for assertions.

**Example in the project**:
- A_EmployeeRepositoryTest.java
```java
assertThat(found).isNotNull()
    .extracting(Employee::getName)
    .isEqualTo(persistedAlex.getName());
```
- D_EmployeeRestControllerIT.java
```java
assertThat(found)
    .extracting(Employee::getName)
    .containsOnly("bob");
```

## b)
@DataJpaTest is used to test only the JPA layer.

**Transitive annotations**:
- @AutoConfigureTestDatabase → Configures an in-memory test database.
- @Transactional → Rolls back changes after each test.

**Example in the project**:
- A_EmployeeRepositoryTest.java
```java
@DataJpaTest  // Loads JPA components only
class A_EmployeeRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;
}
```

## c)
Mocking allows testing service logic without touching the database.

**Example in the project**:
- B_EmployeeService_UnitTest.java
```java
@Mock
private EmployeeRepository employeeRepository; // Mock repository (no database)

@InjectMocks
private EmployeeServiceImpl employeeService; // Injects the mock
```

## d)
| **Annotation**  | **Scope**        | **Used in**          | **Purpose**                                   |
|---------------|----------------|-----------------|-------------------------------------------|
| `@Mock`       | JUnit tests     | Unit tests      | Mocks a class without Spring Context     |
| `@MockBean`   | Spring Context  | Spring Boot tests | Mocks a Spring Bean in `@WebMvcTest` or `@SpringBootTest` |


**Example of @Mock (Unit Test)**
- B_EmployeeService_UnitTest.java
```java
@Mock
private EmployeeRepository employeeRepository; // Used inside unit tests
```

**Example of @MockBean (Spring Boot Test)**
- C_EmployeeController_WithMockServiceTest.java
```java
@MockBean
private EmployeeService service; // Mocks a Spring bean in `@WebMvcTest`
```

## e)
'aplication-integrationtest.properties' is used to configure a seperate database on integration tests. 

**Example of Usage**:
- E_EmployeeRestControllerTemplateIT.java
```java
// Switch to real database using TestPropertySource
//@TestPropertySource(locations = "application-integrationtest.properties")
```

## f)
| **Test Strategy**          | **Approach**                                   | **Spring Annotations**                     | **Example in Project**                                      |
|---------------------------|----------------------------------------------|--------------------------------------------|------------------------------------------------------------|
| **C - Web Layer Test**     | Tests only controllers, mocks dependencies  | `@WebMvcTest` + `@MockBean`               | `C_EmployeeController_WithMockServiceTest.java`            |
| **D - Full Spring Boot Test** | Loads the full app context, uses MockMvc  | `@SpringBootTest` + `@AutoConfigureMockMvc` | `D_EmployeeRestControllerIT.java`                          |
| **E - API Test with HTTP Client** | Uses a real `TestRestTemplate` client | `@SpringBootTest(webEnvironment = RANDOM_PORT)` | `E_EmployeeRestControllerTemplateIT.java` |


| **Test Type**                         | **Scope**         | **Mocking Used?**        | **Real Server Started?** |
|--------------------------------------|----------------|---------------------|-------------------|
| **C (`@WebMvcTest`)**                 | Controller Only | ✅ Yes (`@MockBean`) | ❌ No            |
| **D (`@SpringBootTest` + `MockMvc`)** | Full App (Mock Server) | ❌ No            | ❌ No            |
| **E (`@SpringBootTest` + `TestRestTemplate`)** | Full App (Real Server) | ❌ No            | ✅ Yes            |
