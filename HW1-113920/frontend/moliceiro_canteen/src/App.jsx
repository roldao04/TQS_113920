import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import RestaurantsPage from './pages/Restaurants';
import MealsPage from './pages/Meals';
import StaffPage from './pages/Staff';
import AdminPage from './pages/Admin';

// Protected Route wrapper
const ProtectedRoute = ({ children, allowedRoles }) => {
  const token = localStorage.getItem('username'); // Using username instead of token
  const userRole = localStorage.getItem('role');

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(userRole)) {
    return <Navigate to="/restaurants" replace />;
  }

  return children;
};

function App() {
  return (
    <Router>
      <div data-theme="moliceiro">
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={<Navigate to="/login" replace />} />
          
          {/* User Routes */}
          <Route
            path="/meals/:restaurantId"
            element={
              <ProtectedRoute>
                <MealsPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/restaurants"
            element={
              <ProtectedRoute>
                <RestaurantsPage />
              </ProtectedRoute>
            }
          />

          {/* Staff Routes */}
          <Route
            path="/staff"
            element={
              <ProtectedRoute allowedRoles={['STAFF', 'ADMIN']}>
                <StaffPage />
              </ProtectedRoute>
            }
          />

          {/* Admin Routes */}
          <Route
            path="/admin"
            element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <AdminPage />
              </ProtectedRoute>
            }
          />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
