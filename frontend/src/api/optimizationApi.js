import apiClient from './client'

// PT-BR: Modulo de API para otimizacao de producao.
// EN-US: API module for production optimization.
export default {
  optimize: () => apiClient.post('/optimization/optimize')
}
