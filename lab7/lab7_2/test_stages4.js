import http from "k6/http";
import { check } from "k6";

const BASE_URL = __ENV.BASE_URL || "http://localhost:3333"; // Set the base URL, default to localhost

// k6 options for the test
export const options = {
  stages: [
    // Ramp up from 0 to 20 VUs over the next 30 seconds
    { duration: '30s', target: 20 },

    // Run 20 VUs for the next 30 seconds
    { duration: '30s', target: 20 },

    // Ramp down from 20 to 0 VUs over the next 30 seconds
    { duration: '30s', target: 0 },
  ],

  // Define thresholds to assess test performance
  thresholds: {
    // Ensure that 98% of the requests pass the checks
    "checks": ["rate>0.98"],

    // Ensure the 95th percentile of the request duration is lower than 1.1 seconds
    "http_req_duration": ["p(95)<1100"],

    // Ensure that less than 1% of the requests fail
    "http_req_failed": ["rate<0.01"],
  },
};

export default function () {
  // Define pizza restrictions for the POST request body
  let restrictions = {
    maxCaloriesPerSlice: 500,
    mustBeVegetarian: false,
    excludedIngredients: ["pepperoni"],
    excludedTools: ["knife"],
    maxNumberOfToppings: 6,
    minNumberOfToppings: 2,
  };

  // Perform the POST request with the Authorization header
  let res = http.post(`${BASE_URL}/api/pizza`, JSON.stringify(restrictions), {
    headers: {
      "Content-Type": "application/json",
      "Authorization": "token abcdef0123456789",  // Add Authorization token here
      "X-User-ID": 23423,
    },
  });

  // Check for HTTP status 200 and log the response
  check(res, {
    "is status 200": (r) => r.status === 200,  // Assert status 200 for successful requests
    "body size is less than 1KB": (r) => r.body.length < 1024,  // Assert body size is less than 1KB
  });

  // Optional: Check if specific content exists in the response
  check(res, {
    "contains pizza name": (r) => r.json().pizza.name !== undefined,  // Ensure the pizza name exists in the response
  });

  // Optionally, log the pizza name and ingredients count for debugging
  console.log(`${res.json().pizza.name} (${res.json().pizza.ingredients.length} ingredients)`);
}
