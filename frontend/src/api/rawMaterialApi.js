import apiClient from './client'

// PT-BR: Modulo de API para materias-primas. Encapsula todas as chamadas HTTP
//        relacionadas a esse recurso em funcoes simples e reutilizaveis.
// EN-US: API module for raw materials. Encapsulates all HTTP calls related to
//        this resource in simple, reusable functions.
export default {
  getAll: () => apiClient.get('/raw-materials'),
  getById: (id) => apiClient.get(`/raw-materials/${id}`),
  create: (data) => apiClient.post('/raw-materials', data),
  update: (id, data) => apiClient.put(`/raw-materials/${id}`, data),
  remove: (id) => apiClient.delete(`/raw-materials/${id}`)
}
