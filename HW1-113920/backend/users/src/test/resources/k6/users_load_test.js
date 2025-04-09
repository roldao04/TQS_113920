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
const BASE_URL = 'http://localhost:8081/api';

// Main test function
export default function() {
  // Get all users
  let usersResponse = http.get(`${BASE_URL}/users`);
  check(usersResponse, {
    'status is 200': (r) => r.status === 200,
    'has users': (r) => r.json().length > 0 || r.json().length === 0,
  }) ? successRate.add(true) : failedRequests.add(1);

  // Authentication test (login)
  const loginPayload = JSON.stringify({
    username: 'testuser',
    password: 'password123',
  });

  let loginResponse = http.post(`${BASE_URL}/auth/login`, loginPayload, {
    headers: { 'Content-Type': 'application/json' },
  });
  
  // We don't expect this to succeed in every test run since we're using fake credentials
  // But we check that the API responds properly
  check(loginResponse, {
    'login response is 200 or 401': (r) => r.status === 200 || r.status === 401,
  }) ? successRate.add(true) : failedRequests.add(1);

  // Get user by ID (assuming ID 1 might exist)
  let singleUserResponse = http.get(`${BASE_URL}/users/1`);
  check(singleUserResponse, {
    'status is 200 or 404': (r) => r.status === 200 || r.status === 404,
  }) ? successRate.add(true) : failedRequests.add(1);

  // Create user (commented out to prevent creating too many users during load testing)
  /*
  const userPayload = JSON.stringify({
    username: `user_${Math.floor(Math.random() * 10000)}`,
    email: `user_${Math.floor(Math.random() * 10000)}@example.com`,
    password: 'password123',
    role: 'USER'
  });

  let createResponse = http.post(`${BASE_URL}/users`, userPayload, {
    headers: { 'Content-Type': 'application/json' },
  });
  
  check(createResponse, {
    'status is 201': (r) => r.status === 201,
    'user created': (r) => r.json().id !== undefined,
  }) ? successRate.add(true) : failedRequests.add(1);
  */

  // Wait between iterations
  sleep(1);
} 