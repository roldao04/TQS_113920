# Performance Testing with k6

This directory contains performance tests for the Users service using [k6](https://k6.io/), a modern load testing tool.

## Setup

1. Install k6 by following the instructions at https://k6.io/docs/get-started/installation/

   For Linux users:
   ```bash
   # Add the k6 repository
   sudo gpg --no-default-keyring --keyring /usr/share/keyrings/k6-archive-keyring.gpg --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D69
   echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list
   sudo apt-get update
   sudo apt-get install k6
   ```

   For macOS users:
   ```bash
   brew install k6
   ```

## Available Tests

1. **Users Load Test** (`users_load_test.js`)
   - Basic load test for user management endpoints
   - Tests retrieving user lists and individual users

2. **Authentication Load Test** (`auth_load_test.js`)
   - Advanced test with multiple scenarios for the authentication system
   - Tests user registration, login, and user operations
   - Uses advanced k6 features like scenarios, custom metrics, and data sharing

## Running the Tests

Before running the tests, make sure the Users service is running on the default port (8081).

To run a test:

```bash
k6 run users_load_test.js
```

For the more advanced authentication test:

```bash
k6 run auth_load_test.js
```

## Interpreting Results

k6 will output detailed metrics after each test run, including:

- **http_req_duration**: How long requests took
- **http_reqs**: Total number of requests made
- **iteration_duration**: How long each test iteration took
- **vus**: Virtual User metrics
- **Custom metrics**: 
  - `failed_requests`: Count of failed requests
  - `success_rate`: Percentage of successful requests
  - `login_duration`/`registration_duration`: Specific timing for different authentication operations

## Modifying Tests

If you need to test against a different environment, modify the `BASE_URL` constant in each test script.

To adjust the load parameters, modify the `options` object's `stages` or `scenarios` properties to increase/decrease load or duration.

## Integrating with CI/CD

These tests can be integrated into your CI/CD pipeline to automatically test performance after each deployment. Add the following to your CI configuration:

```yaml
performance_test:
  stage: test
  script:
    - k6 run users/src/test/resources/k6/users_load_test.js
    - k6 run users/src/test/resources/k6/auth_load_test.js
``` 