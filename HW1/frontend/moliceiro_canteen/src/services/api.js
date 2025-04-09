const API_BASE_URL = import.meta.env.VITE_BACKEND_API_URL || 'http://localhost:8080';
const AUTH_BASE_URL = import.meta.env.VITE_AUTH_API_URL || 'http://localhost:8081';

const getHeaders = () => ({
  'Content-Type': 'application/json',
});

export const authApi = {
  login: async (username, password) => {
    try {
      console.log('Making login request to:', `${AUTH_BASE_URL}/api/auth/login`);
      const response = await fetch(`${AUTH_BASE_URL}/api/auth/login`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify({ username, password }),
      });
      
      console.log('Login response status:', response.status);
      const data = await response.json();
      console.log('Login response data:', data);
      
      if (!response.ok) {
        throw new Error(data.message || 'Login failed');
      }
      
      return data;
    } catch (error) {
      console.error('Login API error:', error);
      throw error;
    }
  },

  register: async (userData) => {
    try {
      console.log('Making registration request to:', `${AUTH_BASE_URL}/api/auth/register`);
      console.log('Registration data:', userData);
      
      const response = await fetch(`${AUTH_BASE_URL}/api/auth/register`, {
        method: 'POST',
        headers: getHeaders(),
        body: JSON.stringify(userData),
      });
      
      console.log('Registration response status:', response.status);
      const data = await response.json();
      console.log('Registration response data:', data);
      
      if (!response.ok) {
        throw new Error(data.message || 'Registration failed');
      }
      
      return data;
    } catch (error) {
      console.error('Registration API error:', error);
      throw error;
    }
  },
};

export const mealsApi = {
  getRestaurants: async () => {
    const response = await fetch(`${API_BASE_URL}/api/restaurants`, {
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error('Failed to fetch restaurants');
    }
    return response.json();
  },

  getRestaurantById: async (restaurantId) => {
    const response = await fetch(`${API_BASE_URL}/api/restaurants/${restaurantId}`, {
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error('Failed to fetch restaurant');
    }
    return response.json();
  },

  getMealsByRestaurant: async (restaurantId) => {
    const response = await fetch(`${API_BASE_URL}/api/meals/restaurant/${restaurantId}`, {
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error('Failed to fetch meals');
    }
    return response.json();
  },

  createReservation: async (reservationData) => {
    const response = await fetch(`${API_BASE_URL}/api/reservations`, {
      method: 'POST',
      headers: getHeaders(),
      body: JSON.stringify(reservationData),
    });
    if (!response.ok) {
      throw new Error('Failed to create reservation');
    }
    return response.json();
  },

  getReservationByToken: async (token) => {
    const response = await fetch(`${API_BASE_URL}/api//reservations/token/${token}`, {
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error('Failed to fetch reservation');
    }
    return response.json();
  },

  updateReservationStatus: async (reservationId, status) => {
    const response = await fetch(`${API_BASE_URL}/api//reservations/${reservationId}/status?status=${status}`, {
      method: 'PUT',
      headers: getHeaders(),
    });
    if (!response.ok) {
      throw new Error('Failed to update reservation status');
    }
    return response.json();
  },

  // Commenting out weather API call
  // getWeather: async (date) => {
  //   const response = await fetch(`${API_BASE_URL}/api/weather?date=${date}`, {
  //     headers: getHeaders(),
  //   });
  //   return response.json();
  // },
};

export const adminApi = {
  getUsers: async () => {
    const response = await fetch(`${AUTH_BASE_URL}/api/admin/users`, {
      headers: getHeaders(),
    });
    return response.json();
  },

  deleteUser: async (userId) => {
    const response = await fetch(`${AUTH_BASE_URL}/api/admin/users/${userId}`, {
      method: 'DELETE',
      headers: getHeaders(),
    });
    return response.json();
  },
};

export const staffApi = {
  getRestaurants: async () => {
    const response = await fetch(`${API_BASE_URL}/api/staff/restaurants`, {
      headers: getHeaders(),
    });
    return response.json();
  },

  getReservations: async (restaurantId) => {
    const response = await fetch(`${API_BASE_URL}/api/staff/restaurants/${restaurantId}/reservations`, {
      headers: getHeaders(),
    });
    return response.json();
  },

  confirmReservationPickup: async (reservationId) => {
    const response = await fetch(`${API_BASE_URL}/api/staff/reservations/${reservationId}/pickup`, {
      method: 'POST',
      headers: getHeaders(),
    });
    return response.json();
  },
}; 