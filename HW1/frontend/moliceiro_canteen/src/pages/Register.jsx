import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { authApi } from '../services/api';

const Register = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    name: '',
    role: 'USER'
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await authApi.register(formData);
      if (response.success) {
        // Store user info and redirect
        localStorage.setItem('username', formData.username);
        localStorage.setItem('role', response.role);
        
        // Redirect based on role
        switch (response.role) {
          case 'ADMIN':
            navigate('/admin');
            break;
          case 'STAFF':
            navigate('/staff');
            break;
          default:
            navigate('/restaurants');
        }
      } else {
        alert(response.message);
      }
    } catch (error) {
      console.error('Registration error:', error);
      alert('Failed to register. Please try again.');
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  return (
    <div className="min-h-screen bg-base-100 flex items-center justify-center">
      <div className="card w-96 bg-base-100 shadow-xl">
        <div className="card-body">
          <h2 className="card-title text-3xl font-bold text-center text-primary mb-6">Register</h2>
          <form onSubmit={handleSubmit} className="space-y-4">
            {/* Username */}
            <div className="form-control">
              <label className="label">
                <span className="label-text">Username*</span>
              </label>
              <input
                type="text"
                name="username"
                placeholder="Enter username"
                className="input input-bordered w-full"
                value={formData.username}
                onChange={handleChange}
                required
              />
            </div>

            {/* Password */}
            <div className="form-control">
              <label className="label">
                <span className="label-text">Password*</span>
              </label>
              <input
                type="password"
                name="password"
                placeholder="Enter password"
                className="input input-bordered w-full"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>

            {/* Email */}
            <div className="form-control">
              <label className="label">
                <span className="label-text">Email</span>
              </label>
              <input
                type="email"
                name="email"
                placeholder="Enter email"
                className="input input-bordered w-full"
                value={formData.email}
                onChange={handleChange}
              />
            </div>

            {/* Name */}
            <div className="form-control">
              <label className="label">
                <span className="label-text">Full Name</span>
              </label>
              <input
                type="text"
                name="name"
                placeholder="Enter your full name"
                className="input input-bordered w-full"
                value={formData.name}
                onChange={handleChange}
              />
            </div>

            {/* Role Selection */}
            <div className="form-control">
              <label className="label">
                <span className="label-text">Role*</span>
              </label>
              <select
                name="role"
                className="select select-bordered w-full"
                value={formData.role}
                onChange={handleChange}
                required
              >
                <option value="USER">User</option>
                <option value="STAFF">Staff</option>
                <option value="ADMIN">Admin</option>
              </select>
            </div>

            {/* Submit Button */}
            <div className="form-control mt-6">
              <button type="submit" className="btn btn-primary">Register</button>
            </div>

            {/* Login Link */}
            <div className="text-center">
              <button
                type="button"
                className="btn btn-link"
                onClick={() => navigate('/login')}
              >
                Already have an account? Login
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default Register; 