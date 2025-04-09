import React, { useState, useEffect } from 'react';
import { adminApi } from '../services/api';

const AdminPage = () => {
  const [users, setUsers] = useState([]);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      const data = await adminApi.getUsers();
      setUsers(data);
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  };

  const handleDeleteUser = async () => {
    try {
      await adminApi.deleteUser(selectedUser.id);
      setIsDeleteModalOpen(false);
      fetchUsers();
      // Show success message
    } catch (error) {
      console.error('Error deleting user:', error);
      // Show error message
    }
  };

  return (
    <div className="container mx-auto px-4 py-8">
      <h1 className="text-2xl font-bold mb-6">Admin Dashboard</h1>

      {/* Users Table */}
      <div className="overflow-x-auto">
        <table className="table w-full">
          <thead>
            <tr>
              <th>ID</th>
              <th>Username</th>
              <th>Email</th>
              <th>Role</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.username}</td>
                <td>{user.email}</td>
                <td>
                  <span className={`badge ${
                    user.role === 'ADMIN' ? 'badge-primary' :
                    user.role === 'STAFF' ? 'badge-secondary' :
                    'badge-accent'
                  }`}>
                    {user.role}
                  </span>
                </td>
                <td>
                  <button
                    className="btn btn-sm btn-error"
                    onClick={() => {
                      setSelectedUser(user);
                      setIsDeleteModalOpen(true);
                    }}
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {/* Delete Confirmation Modal */}
      {isDeleteModalOpen && (
        <div className="modal modal-open">
          <div className="modal-box">
            <h3 className="font-bold text-lg">Delete User</h3>
            <p>Are you sure you want to delete user {selectedUser.username}?</p>
            <div className="modal-action">
              <button className="btn btn-error" onClick={handleDeleteUser}>
                Delete
              </button>
              <button 
                className="btn btn-ghost"
                onClick={() => setIsDeleteModalOpen(false)}
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

export default AdminPage; 