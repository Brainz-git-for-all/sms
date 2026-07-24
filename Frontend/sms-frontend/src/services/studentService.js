// SHARED FILE — I write getAll(), you write getById() and create()
//
// PATTERN (I show in getAll):
//   export const getAll = () => api.get('/api/students');
//
// YOUR TASKS:
//   getById(id)          → GET  /api/students/{id}        → api.get(`/api/students/${id}`)
//   create(studentData)  → POST /api/students             → api.post('/api/students', studentData)
//
// I BUILD:
//   update(id, data)     → PUT  /api/students/{id}
//   deleteStudent(id)    → DELETE /api/students/{id}

import api from './api';

// I write getAll() here first

// You write getById() and create() after seeing my example
