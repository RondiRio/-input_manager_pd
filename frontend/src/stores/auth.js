import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import apiClient from '@/api/client'

// PT-BR: Store de autenticacao. Gerencia o token JWT e o estado de login do usuario.
//        O token e persistido no localStorage para sobreviver a recarregamentos da
//        pagina, mas e limpo ao fazer logout.
// EN-US: Authentication store. Manages the JWT token and user login state.
//        The token is persisted in localStorage to survive page reloads,
//        but is cleared on logout.
export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('jwt_token') || null)
  const username = ref(localStorage.getItem('username') || null)

  const isAuthenticated = computed(() => !!token.value)

  async function login(user, password) {
    const response = await apiClient.post('/auth/login', { username: user, password })
    token.value = response.data.token
    username.value = response.data.username
    localStorage.setItem('jwt_token', response.data.token)
    localStorage.setItem('username', response.data.username)
    return response.data
  }

  function logout() {
    token.value = null
    username.value = null
    localStorage.removeItem('jwt_token')
    localStorage.removeItem('username')
  }

  return { token, username, isAuthenticated, login, logout }
})
