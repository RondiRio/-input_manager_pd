<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-header">
        <div class="login-logo">IM</div>
        <h1>Input Manager</h1>
        <p class="text-muted">{{ $t('nav.dashboard') }} - Projedata</p>
      </div>

      <BaseAlert
        v-if="error"
        type="error"
        :message="error"
        @dismiss="error = ''"
      />

      <form @submit.prevent="handleLogin">
        <BaseInput
          v-model="username"
          label="Username"
          placeholder="admin"
          required
        />
        <div class="mt-md">
          <BaseInput
            v-model="password"
            type="password"
            label="Password"
            placeholder="admin123"
            required
          />
        </div>
        <div class="mt-lg">
          <BaseButton type="submit" :loading="loading" size="lg" style="width: 100%">
            Login
          </BaseButton>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
// PT-BR: Pagina de login simples. Para o MVP, as credenciais sao admin/admin123.
//        Apos login bem-sucedido, redireciona para o dashboard.
// EN-US: Simple login page. For the MVP, credentials are admin/admin123.
//        After successful login, redirects to the dashboard.
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseAlert from '@/components/ui/BaseAlert.vue'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')

async function handleLogin() {
  loading.value = true
  error.value = ''
  try {
    await authStore.login(username.value, password.value)
    router.push('/dashboard')
  } catch (e) {
    error.value = e.response?.data?.error || 'Login failed. Please check your credentials.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: var(--color-bg-primary);
}

.login-card {
  background-color: var(--color-bg-card);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow-lg);
  padding: var(--spacing-xl);
  width: 100%;
  max-width: 400px;
}

.login-header {
  text-align: center;
  margin-bottom: var(--spacing-xl);
}

.login-logo {
  width: 56px;
  height: 56px;
  background-color: var(--color-action);
  color: #FFFFFF;
  border-radius: var(--radius-lg);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  font-weight: 700;
  margin-bottom: var(--spacing-md);
}

.login-header h1 {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-text-primary);
}
</style>
