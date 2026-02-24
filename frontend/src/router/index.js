import { createRouter, createWebHistory } from 'vue-router'

// PT-BR: Rotas da aplicacao com carregamento lazy para melhor performance.
//        Cada view e carregada somente quando o usuario navega ate ela.
// EN-US: Application routes with lazy loading for better performance.
//        Each view is loaded only when the user navigates to it.
const routes = [
  {
    path: '/',
    name: 'dashboard',
    component: () => import('@/views/DashboardView.vue'),
    meta: { title: 'nav.dashboard' }
  },
  {
    path: '/raw-materials',
    name: 'raw-materials',
    component: () => import('@/views/RawMaterialsView.vue'),
    meta: { title: 'nav.rawMaterials' }
  },
  {
    path: '/products',
    name: 'products',
    component: () => import('@/views/ProductsView.vue'),
    meta: { title: 'nav.products' }
  },
  {
    path: '/optimization',
    name: 'optimization',
    component: () => import('@/views/OptimizationView.vue'),
    meta: { title: 'nav.optimization' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
