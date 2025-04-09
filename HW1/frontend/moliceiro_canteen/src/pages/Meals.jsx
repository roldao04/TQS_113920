import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { mealsApi } from '../services/api';

const MealsPage = () => {
  const { restaurantId } = useParams();
  const [restaurant, setRestaurant] = useState(null);
  const [meals, setMeals] = useState([]);
  // const [weather, setWeather] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState('');
  const [isReservationModalOpen, setIsReservationModalOpen] = useState(false);
  const [selectedMeal, setSelectedMeal] = useState(null);
  const [reservationDetails, setReservationDetails] = useState({
    numberOfPeople: 1,
    reservationTime: '',
    customerUsername: '',
  });

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        // Fetch restaurant details
        const restaurantData = await mealsApi.getRestaurantById(restaurantId);
        setRestaurant(restaurantData);

        // Fetch meals for the restaurant
        const mealsData = await mealsApi.getMealsByRestaurant(restaurantId);
        setMeals(mealsData);

        // Comment out weather fetch
        // const weatherData = await mealsApi.getWeather(new Date().toISOString().split('T')[0]);
        // setWeather(weatherData);

        setError(null);
      } catch (err) {
        console.error('Error fetching data:', err);
        setError('Failed to load data. Please try again later.');
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [restaurantId]);

  const handleReservation = async () => {
    try {
      if (!reservationDetails.customerUsername) {
        setError('Please enter your username');
        return;
      }

      const accessToken = localStorage.getItem('accessToken');
      if (!accessToken) {
        setError('Please log in to make a reservation');
        return;
      }

      const response = await mealsApi.createReservation({
        restaurantId: restaurant.id,
        mealId: selectedMeal.id,
        numberOfPeople: reservationDetails.numberOfPeople,
        reservationTime: reservationDetails.reservationTime,
        customerUsername: reservationDetails.customerUsername,
        accessToken: accessToken
      });

      // Close modal and show success message
      setIsReservationModalOpen(false);
      setSuccessMessage('Reservation created successfully! Your access token is: ' + response.accessToken);
      
      // Reset form
      setReservationDetails({
        numberOfPeople: 1,
        reservationTime: '',
        customerUsername: '',
      });
      setSelectedMeal(null);
      
      // Clear success message after 5 seconds
      setTimeout(() => {
        setSuccessMessage('');
      }, 5000);
    } catch (error) {
      console.error('Error creating reservation:', error);
      setError(error.message || 'Failed to create reservation');
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center min-h-screen">
        <div className="loading loading-spinner loading-lg"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="container mx-auto px-4 py-8">
        <div className="alert alert-error">
          <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span>{error}</span>
        </div>
      </div>
    );
  }

  return (
    <div className="container mx-auto px-4 py-8">
      {/* Success Message */}
      {successMessage && (
        <div className="alert alert-success mb-4">
          <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          <span>{successMessage}</span>
        </div>
      )}

      {/* Restaurant Header */}
      {restaurant && (
        <div className="mb-8">
          <h1 className="text-4xl font-bold mb-2">{restaurant.name}</h1>
          <p className="text-gray-600">{restaurant.description}</p>
          <div className="flex items-center mt-2">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 text-primary" viewBox="0 0 20 20" fill="currentColor">
              <path d="M2 3a1 1 0 011-1h2.153a1 1 0 01.986.836l.74 4.435a1 1 0 01-.54 1.06l-1.548.773a11.037 11.037 0 006.105 6.105l.774-1.548a1 1 0 011.059-.54l4.435.74a1 1 0 01.836.986V17a1 1 0 01-1 1h-2C7.82 18 2 12.18 2 5V3z" />
            </svg>
            <span className="ml-2">{restaurant.phoneNumber}</span>
          </div>
        </div>
      )}

      {/* Comment out Weather Display */}
      {/* {weather && (
        <div className="alert alert-info mb-8">
          <div className="flex items-center">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 15a4 4 0 004 4h9a5 5 0 10-.1-9.999 5.002 5.002 0 10-9.78 2.096A4.001 4.001 0 003 15z" />
            </svg>
            <span className="text-lg">Today's Weather: {weather.temperature}°C - {weather.description}</span>
          </div>
        </div>
      )} */}

      {/* Meals Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {meals.map((meal) => (
          <div key={meal.id} className="card bg-base-100 shadow-xl">
            <div className="card-body">
              <h2 className="card-title">{meal.name}</h2>
              <p className="text-gray-600">{meal.description}</p>
              <div className="flex items-center mt-2">
                <span className="text-primary font-bold text-xl">€{meal.price}</span>
                <span className="ml-2 text-sm text-gray-500">{meal.category}</span>
              </div>
              <div className="card-actions justify-end mt-4">
                <button 
                  className="btn btn-primary"
                  onClick={() => {
                    setSelectedMeal(meal);
                    setIsReservationModalOpen(true);
                    setError(null);
                  }}
                >
                  Make Reservation
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>

      {/* Reservation Modal */}
      {isReservationModalOpen && (
        <div className="modal modal-open">
          <div className="modal-box">
            <h3 className="font-bold text-lg mb-4">Make a Reservation</h3>
            {error && (
              <div className="alert alert-error mb-4">
                <svg xmlns="http://www.w3.org/2000/svg" className="stroke-current shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span>{error}</span>
              </div>
            )}
            <div className="form-control">
              <label className="label">
                <span className="label-text">Username</span>
              </label>
              <input
                type="text"
                className="input input-bordered"
                value={reservationDetails.customerUsername}
                onChange={(e) => setReservationDetails({
                  ...reservationDetails,
                  customerUsername: e.target.value
                })}
                placeholder="Enter your username"
              />
            </div>
            <div className="form-control mt-4">
              <label className="label">
                <span className="label-text">Number of People</span>
              </label>
              <input
                type="number"
                className="input input-bordered"
                min="1"
                value={reservationDetails.numberOfPeople}
                onChange={(e) => setReservationDetails({
                  ...reservationDetails,
                  numberOfPeople: parseInt(e.target.value)
                })}
              />
            </div>
            <div className="form-control mt-4">
              <label className="label">
                <span className="label-text">Date and Time</span>
              </label>
              <input
                type="datetime-local"
                className="input input-bordered"
                value={reservationDetails.reservationTime}
                onChange={(e) => setReservationDetails({
                  ...reservationDetails,
                  reservationTime: e.target.value
                })}
              />
            </div>
            <div className="modal-action">
              <button 
                className="btn btn-primary" 
                onClick={handleReservation}
                disabled={!reservationDetails.customerUsername || !reservationDetails.reservationTime}
              >
                Confirm Reservation
              </button>
              <button 
                className="btn btn-ghost"
                onClick={() => {
                  setIsReservationModalOpen(false);
                  setError(null);
                  setReservationDetails({
                    numberOfPeople: 1,
                    reservationTime: '',
                    customerUsername: '',
                  });
                }}
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default MealsPage; 