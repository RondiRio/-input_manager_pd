import axios from 'axios'

// PT-BR: Cliente HTTP centralizado. Todas as chamadas de API passam por aqui,
//        o que facilita adicionar headers, interceptors e tratar erros de forma
//        consistente em toda a aplicacao.
// EN-US: Centralized HTTP client. All API calls go through here, which makes it
//        easier to add headers, interceptors, and handle errors consistently
//        across the entire application.
const apiClient = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
})

// PT-BR: Interceptor de resposta para tratar erros globalmente.
//        Em vez de repetir o tratamento de erro em cada chamada,
//        centralizamos aqui e deixamos os componentes mais limpos.
// EN-US: Response interceptor for global error handling.
//        Instead of repeating error handling in every call,
//        we centralize it here and keep components cleaner.
apiClient.interceptors.response.use(
  response => response,
  error => {
    const message = error.response?.data?.error || error.message || 'An unexpected error occurred'
    console.error('[API Error]', message)
    return Promise.reject(error)
  }
)

export default apiClient
