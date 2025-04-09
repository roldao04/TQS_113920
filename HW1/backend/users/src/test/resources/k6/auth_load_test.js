import http from 'k6/http';
import { sleep, check, group } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';
import { SharedArray } from 'k6/data';
import { randomString } from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

// Custom metrics
const failedRequests = new Counter('failed_requests');
const successRate = new Rate('success_rate');
const loginDuration = new Trend('login_duration');
const registrationDuration = new Trend('registration_duration');
const userOperationsDuration = new Trend('user_operations_duration');

// Pre-generate usernames and passwords for consistency
const testUsers = new SharedArray('users', function() {
  const users = [];
  for (let i = 0; i < 50; i++) {
    users.push({
      username: `testuser_${randomString(8)}`,
      email: `testuser_${randomString(8)}@example.com`,
      password: `password_${randomString(10)}`
    });
  }
  return users;
});

// Test configuration
export const options = {
  scenarios: {
    registration_load: {
      executor: 'ramping-arrival-rate',
      startRate: 1,
      timeUnit: '1s',
      preAllocatedVUs: 10,
      maxVUs: 30,
      stages: [
        { duration: '20s', target: 2 },  // 2 registrations per second
        { duration: '40s', target: 2 },  // maintain for 40s
        { duration: '10s', target: 0 },  // ramp down
      ],
    },
    login_load: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '10s', target: 10 },
        { duration: '50s', target: 30 },
        { duration: '20s', target: 0 },
      ],
      gracefulRampDown: '10s',
    },
    user_operations: {
      executor: 'constant-vus',
      vus: 5,
      duration: '1m20s',
      startTime: '30s',
    }
  },
  thresholds: {
    'http_req_duration': ['p(95)<1500'], // 95% of requests should be below 1.5s
    'login_duration': ['p(95)<1000'],    // Login should be fast
    'registration_duration': ['p(95)<2000'], // Registration can be slower
    'user_operations_duration': ['p(95)<800'],
    'failed_requests': ['count<30'],
    'success_rate': ['rate>0.85'],      // Lower threshold for auth operations
  },
};

// Base URL (adjust according to your environment)
const BASE_URL = 'http://localhost:8081/api';

// Track registered users to use in login
let registeredUsers = [];

// Main test function
export default function() {
  const scenarioName = exec.scenario.name;
  
  // Registration scenario
  if (scenarioName === 'registration_load') {
    group('User Registration', function() {
      const userIndex = __VU % testUsers.length;
      const user = testUsers[userIndex];
      
      const registerPayload = JSON.stringify({
        username: user.username,
        email: user.email,
        password: user.password,
        role: 'USER'
      });
      
      const start = new Date();
      const registerResponse = http.post(`${BASE_URL}/auth/register`, registerPayload, {
        headers: { 'Content-Type': 'application/json' },
      });
      registrationDuration.add(new Date() - start);
      
      const success = check(registerResponse, {
        'registration status is 201 or 400': (r) => r.status === 201 || r.status === 400, // 400 if user exists
        'registration successful': (r) => r.status === 201 || (r.status === 400 && r.body.includes('already exists')),
      });
      
      if (success) {
        successRate.add(true);
        // If registration successful, add to our tracking for login tests
        if (registerResponse.status === 201) {
          registeredUsers.push(user);
        }
      } else {
        failedRequests.add(1);
      }
      
      sleep(Math.random() * 2);
    });
  }
  
  // Login scenario
  else if (scenarioName === 'login_load') {
    group('User Login', function() {
      // Try to use a registered user if available, otherwise use a test user
      let user;
      if (registeredUsers.length > 0) {
        user = registeredUsers[Math.floor(Math.random() * registeredUsers.length)];
      } else {
        user = testUsers[__VU % testUsers.length];
      }
      
      const loginPayload = JSON.stringify({
        username: user.username,
        password: user.password,
      });
      
      const start = new Date();
      const loginResponse = http.post(`${BASE_URL}/auth/login`, loginPayload, {
        headers: { 'Content-Type': 'application/json' },
      });
      loginDuration.add(new Date() - start);
      
      // We count 401 as a "success" for the API since unregistered users should get this
      const success = check(loginResponse, {
        'login status is 200 or 401': (r) => r.status === 200 || r.status === 401,
      });
      
      if (success) {
        successRate.add(true);
      } else {
        failedRequests.add(1);
      }
      
      sleep(Math.random() * 3);
    });
  }
  
  // User operations scenario
  else if (scenarioName === 'user_operations') {
    group('User Operations', function() {
      // Get all users
      const start = new Date();
      const usersResponse = http.get(`${BASE_URL}/users`);
      userOperationsDuration.add(new Date() - start);
      
      check(usersResponse, {
        'users status is 200': (r) => r.status === 200,
        'users response is JSON': (r) => r.headers['Content-Type'] && r.headers['Content-Type'].includes('application/json'),
      }) ? successRate.add(true) : failedRequests.add(1);
      
      // If we got users back, get a specific user
      if (usersResponse.status === 200 && usersResponse.json().length > 0) {
        const users = usersResponse.json();
        const randomUser = users[Math.floor(Math.random() * users.length)];
        
        if (randomUser && randomUser.id) {
          const startUserGet = new Date();
          const userResponse = http.get(`${BASE_URL}/users/${randomUser.id}`);
          userOperationsDuration.add(new Date() - startUserGet);
          
          check(userResponse, {
            'user get status is 200': (r) => r.status === 200,
            'user data is correct': (r) => r.status === 200 && r.json().id === randomUser.id,
          }) ? successRate.add(true) : failedRequests.add(1);
        }
      }
      
      sleep(Math.random() * 2);
    });
  }
} 