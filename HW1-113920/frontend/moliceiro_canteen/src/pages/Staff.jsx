import React, { useState, useEffect } from 'react';
import { staffApi } from '../services/api';

const StaffPage = () => {
  const [restaurants, setRestaurants] = useState([]);
  const [selectedRestaurant, setSelectedRestaurant] = useState(null);
  const [reservations, setReservations] = useState([]);
  const [isPickupModalOpen, setIsPickupModalOpen] = useState(false);
  const [selectedReservation, setSelectedReservation] = useState(null);

  useEffect(() => {
    fetchRestaurants();
  }, []);

  useEffect(() => {
    if (selectedRestaurant) {
      fetchReservations(selectedRestaurant.id);
    }
  }, [selectedRestaurant]);

  const fetchRestaurants = async () => {
    try {
      const data = await staffApi.getRestaurants();
      setRestaurants(data);
      if (data.length > 0) {
        setSelectedRestaurant(data[0]);
      }
    } catch (error) {
      console.error('Error fetching restaurants:', error);
    }
  };

  const fetchReservations = async (restaurantId) => {
    try {
      const data = await staffApi.getReservations(restaurantId);
      setReservations(data);
    } catch (error) {
      console.error('Error fetching reservations:', error);
    }
  };

  const handlePickupConfirmation = async () => {
    try {
      await staffApi.confirmReservationPickup(selectedReservation.id);
      setIsPickupModalOpen(false);
      fetchReservations(selectedRestaurant.id);
      // Show success message
    } catch (error) {
      console.error('Error confirming pickup:', error);
      // Show error message
    }
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">Staff Dashboard</h1>

      {/* Restaurant Selection */}
      <div className="mb-6">
        <select 
          className="select select-bordered w-full max-w-xs"
          value={selectedRestaurant?.id || ''}
          onChange={(e) => setSelectedRestaurant(restaurants.find(r => r.id === e.target.value))}
        >
          {restaurants.map((restaurant) => (
            <option key={restaurant.id} value={restaurant.id}>
              {restaurant.name}
            </option>
          ))}
        </select>
      </div>

      {/* Reservations Table */}
      <div className="overflow-x-auto">
        <table className="table w-full">
          <thead>
            <tr>
              <th>Reservation ID</th>
              <th>Customer Name</th>
              <th>Time</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {reservations.map((reservation) => (
              <tr key={reservation.id}>
                <td>{reservation.id}</td>
                <td>{reservation.customerName}</td>
                <td>{new Date(reservation.reservationTime).toLocaleString()}</td>
                <td>
                  <span className={`badge ${
                    reservation.status === 'CONFIRMED' ? 'badge-success' :
                    reservation.status === 'PENDING' ? 'badge-warning' :
                    reservation.status === 'COMPLETED' ? 'badge-info' :
                    'badge-error'
                  }`}>
                    {reservation.status}
                  </span>
                </td>
                <td>
                  {reservation.status === 'CONFIRMED' && (
                    <button
                      className="btn btn-sm btn-primary"
                      onClick={() => {
                        setSelectedReservation(reservation);
                        setIsPickupModalOpen(true);
                      }}
                    >
                      Confirm Pickup
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Pickup Confirmation Modal */}
      {isPickupModalOpen && (
        <div className="modal modal-open">
          <div className="modal-box">
            <h3 className="font-bold text-lg">Confirm Reservation Pickup</h3>
            <p>Are you sure you want to confirm pickup for reservation #{selectedReservation.id}?</p>
            <div className="modal-action">
              <button className="btn btn-primary" onClick={handlePickupConfirmation}>
                Confirm Pickup
              </button>
              <button 
                className="btn btn-ghost"
                onClick={() => setIsPickupModalOpen(false)}
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

export default StaffPage; 