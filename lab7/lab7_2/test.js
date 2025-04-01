import http from "k6/http";

const BASE_URL = __ENV.BASE_URL || "http://localhost:3333";

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

  // Output the pizza name and the number of ingredients for debugging
  console.log(`${res.json().pizza.name} (${res.json().pizza.ingredients.length} ingredients)`);

  // Optionally you can add assertions for the response, e.g., to check status code
  // Check if status code is 200 OK
  if (res.status !== 200) {
    console.error(`Request failed with status ${res.status}`);
  }
}
