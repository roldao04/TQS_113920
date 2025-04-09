# Performance Testing with k6

This directory contains performance tests for the Meals service using [k6](https://k6.io/), a modern load testing tool.

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

1. **Restaurant Load Test** (`restaurant_load_test.js`)
   - Tests the restaurant listing and details endpoints
   - Simulates users browsing through restaurants

2. **Reservation Load Test** (`reservation_load_test.js`)
   - More comprehensive test with multiple scenarios
   - Tests both browsing behavior and reservation creation
   - Uses advanced k6 features like custom metrics and multiple scenarios

## Running the Tests

Before running the tests, make sure the Meals service is running on the default port (8080).

To run a test:

```bash
k6 run restaurant_load_test.js
```

For the more advanced reservation test:

```bash
k6 run reservation_load_test.js
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
  - `restaurant_request_duration`/`reservation_request_duration`: Specific timing for different API calls

## Modifying Tests

If you need to test against a different environment, modify the `BASE_URL` constant in each test script.

To adjust the load parameters, modify the `options` object's `stages` property to increase/decrease load or duration.

## Integrating with CI/CD

These tests can be integrated into your CI/CD pipeline to automatically test performance after each deployment. 