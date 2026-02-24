import apiClient from './client'

// PT-BR: Modulo de API para produtos. Mesma abordagem do rawMaterialApi.
// EN-US: API module for products. Same approach as rawMaterialApi.
export default {
  getAll: () => apiClient.get('/products'),
  getById: (id) => apiClient.get(`/products/${id}`),
  create: (data) => apiClient.post('/products', data),
  update: (id, data) => apiClient.put(`/products/${id}`, data),
  remove: (id) => apiClient.delete(`/products/${id}`)
}
