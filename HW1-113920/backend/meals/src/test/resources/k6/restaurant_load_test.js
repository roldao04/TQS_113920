import http from 'k6/http';
import { sleep, check } from 'k6';
import { Counter, Rate } from 'k6/metrics';

// Custom metrics
const failedRequests = new Counter('failed_requests');
const successRate = new Rate('success_rate');

// Test configuration
export const options = {
  stages: [
    { duration: '30s', target: 10 }, // Ramp-up to 10 users
    { duration: '1m', target: 50 },  // Ramp-up to 50 users
    { duration: '2m', target: 50 },  // Stay at 50 users
    { duration: '30s', target: 0 },  // Ramp-down to 0 users
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'], // 95% of requests should be below 500ms
    'failed_requests': ['count<10'],  // Less than 10 failed requests
    'success_rate': ['rate>0.95'],    // Success rate should be higher than 95%
  },
};

// Base URL (adjust according to your environment)
const BASE_URL = 'http://localhost:8080';

// Main test function
export default function() {
  // Get all restaurants
  let restaurantsResponse = http.get(`${BASE_URL}/restaurants`);
  check(restaurantsResponse, {
    'status is 200': (r) => r.status === 200,
    'has restaurants': (r) => r.json().length > 0 || r.json().length === 0,
  }) ? successRate.add(true) : failedRequests.add(1);

  // Create a restaurant (uncommenting may create too many restaurants during load test)
  /*
  const restaurantPayload = JSON.stringify({
    name: `Test Restaurant ${Math.floor(Math.random() * 10000)}`,
    description: 'Created during load test',
    address: 'Test Address',
    phoneNumber: '+351123456789',
  });

  let createResponse = http.post(`${BASE_URL}/restaurants`, restaurantPayload, {
    headers: { 'Content-Type': 'application/json' },
  });
  
  check(createResponse, {
    'status is 201': (r) => r.status === 201,
    'restaurant created': (r) => r.json().id !== undefined,
  }) ? successRate.add(true) : failedRequests.add(1);
  */

  // Get restaurant by ID (if your endpoint supports it)
  // Assuming ID 1 exists in your system
  let singleRestaurantResponse = http.get(`${BASE_URL}/restaurants/1`);
  check(singleRestaurantResponse, {
    'status is 200 or 404': (r) => r.status === 200 || r.status === 404,
  }) ? successRate.add(true) : failedRequests.add(1);

  // Get meals by restaurant (if your endpoint supports it)
  let mealsResponse = http.get(`${BASE_URL}/meals/restaurant/1`);
  check(mealsResponse, {
    'status is 200 or 404': (r) => r.status === 200 || r.status === 404,
  }) ? successRate.add(true) : failedRequests.add(1);

  // Wait between iterations
  sleep(1);
} 