import http from 'k6/http';
import { sleep, check, group } from 'k6';
import { Counter, Rate, Trend } from 'k6/metrics';

// Custom metrics
const failedRequests = new Counter('failed_requests');
const successRate = new Rate('success_rate');
const restaurantRequestDuration = new Trend('restaurant_request_duration');
const reservationRequestDuration = new Trend('reservation_request_duration');

// Test configuration
export const options = {
  scenarios: {
    restaurant_browsing: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        { duration: '30s', target: 20 },
        { duration: '1m', target: 20 },
        { duration: '30s', target: 0 },
      ],
      gracefulRampDown: '10s',
    },
    reservation_creation: {
      executor: 'ramping-arrival-rate',
      startRate: 1,
      timeUnit: '1s',
      preAllocatedVUs: 10,
      maxVUs: 50,
      stages: [
        { duration: '30s', target: 5 },  // 5 reservations per second
        { duration: '1m', target: 5 },   // maintain 5 reservations per second
        { duration: '30s', target: 0 },  // ramp down to 0
      ],
    }
  },
  thresholds: {
    'http_req_duration': ['p(95)<1000'], // 95% of requests should be below 1s
    'restaurant_request_duration': ['p(95)<500'], // Restaurant requests should be faster
    'reservation_request_duration': ['p(95)<1500'], // Reservation requests can be slightly slower
    'failed_requests': ['count<20'],
    'success_rate': ['rate>0.9'],
  },
};

// Base URL (adjust according to your environment)
const BASE_URL = 'http://localhost:8080';

// Get a timestamp for a future date (reservation time)
function getFutureTimestamp() {
  const futureDate = new Date();
  futureDate.setDate(futureDate.getDate() + 1);
  return futureDate.toISOString();
}

// Main test function
export default function() {
  // Browser scenario - users browsing restaurants and meals
  if (__ITER % 3 !== 0) {  // Run this scenario 2/3 of the time
    group('Browse Restaurants', function() {
      // Get all restaurants
      const start = new Date();
      let restaurantsResponse = http.get(`${BASE_URL}/restaurants`);
      restaurantRequestDuration.add(new Date() - start);
      
      check(restaurantsResponse, {
        'status is 200': (r) => r.status === 200,
      }) ? successRate.add(true) : failedRequests.add(1);
      
      if (restaurantsResponse.status === 200 && restaurantsResponse.json().length > 0) {
        // Get a specific restaurant
        const restaurants = restaurantsResponse.json();
        const restaurantId = restaurants[0].id || 1;
        
        const startDetail = new Date();
        let restaurantDetailResponse = http.get(`${BASE_URL}/restaurants/${restaurantId}`);
        restaurantRequestDuration.add(new Date() - startDetail);
        
        check(restaurantDetailResponse, {
          'restaurant detail status is 200 or 404': (r) => r.status === 200 || r.status === 404,
        }) ? successRate.add(true) : failedRequests.add(1);
        
        // Get meals for this restaurant
        const startMeals = new Date();
        let mealsResponse = http.get(`${BASE_URL}/meals/restaurant/${restaurantId}`);
        restaurantRequestDuration.add(new Date() - startMeals);
        
        check(mealsResponse, {
          'meals status is 200 or 404': (r) => r.status === 200 || r.status === 404,
        }) ? successRate.add(true) : failedRequests.add(1);
      }
      
      sleep(Math.random() * 3);
    });
  } else {
    // Reservation scenario - users making reservations
    group('Make Reservation', function() {
      // First get restaurants to find a valid ID
      let restaurantsResponse = http.get(`${BASE_URL}/restaurants`);
      let restaurantId = 1;
      let mealId = 1;
      
      // If we got restaurants back, use a real ID
      if (restaurantsResponse.status === 200 && restaurantsResponse.json().length > 0) {
        restaurantId = restaurantsResponse.json()[0].id;
        
        // Try to get meals for this restaurant
        let mealsResponse = http.get(`${BASE_URL}/meals/restaurant/${restaurantId}`);
        if (mealsResponse.status === 200 && mealsResponse.json().length > 0) {
          mealId = mealsResponse.json()[0].id;
        }
      }
      
      // Create a reservation
      const reservationPayload = JSON.stringify({
        restaurantId: restaurantId,
        mealId: mealId,
        reservationTime: getFutureTimestamp(),
        numberOfPeople: Math.floor(Math.random() * 5) + 1,
        customerUsername: `user_${Math.floor(Math.random() * 10000)}`,
      });
      
      const startReservation = new Date();
      let reservationResponse = http.post(`${BASE_URL}/reservations`, reservationPayload, {
        headers: { 'Content-Type': 'application/json' },
      });
      reservationRequestDuration.add(new Date() - startReservation);
      
      check(reservationResponse, {
        'reservation status is 201 or 400': (r) => r.status === 201 || r.status === 400,
      }) ? successRate.add(true) : failedRequests.add(1);
      
      // If reservation was successful, check it by token
      if (reservationResponse.status === 201 && reservationResponse.json().accessToken) {
        const token = reservationResponse.json().accessToken;
        
        const startCheck = new Date();
        let checkResponse = http.get(`${BASE_URL}/reservations/token/${token}`);
        reservationRequestDuration.add(new Date() - startCheck);
        
        check(checkResponse, {
          'reservation check status is 200': (r) => r.status === 200,
          'reservation has correct data': (r) => r.status === 200 && r.json().accessToken === token,
        }) ? successRate.add(true) : failedRequests.add(1);
      }
      
      sleep(Math.random() * 2);
    });
  }
} 