import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath } from 'node:url'

// PT-BR: Configuracao do Vite com proxy para o backend Quarkus em desenvolvimento.
//        O proxy redireciona chamadas /api para o servidor backend, evitando problemas
//        de CORS durante o desenvolvimento local.
// EN-US: Vite configuration with proxy to the Quarkus backend in development.
//        The proxy redirects /api calls to the backend server, avoiding CORS issues
//        during local development.
export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  build: {
    outDir: 'dist',
    sourcemap: false
  }
})
