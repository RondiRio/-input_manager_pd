import { createRouter, createWebHistory } from 'vue-router'

// PT-BR: Rotas da aplicacao com carregamento lazy para melhor performance.
//        A landing page e a rota raiz (/), publica, sem sidebar.
//        O dashboard e as paginas internas usam o MainLayout com sidebar.
// EN-US: Application routes with lazy loading for better performance.
//        The landing page is the root route (/), public, without sidebar.
//        The dashboard and internal pages use MainLayout with sidebar.
const routes = [
  {
    path: '/',
    name: 'landing',
    component: () => import('@/views/LandingView.vue'),
    meta: { title: 'landing.title', layout: 'none' }
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('@/views/LoginView.vue'),
    meta: { title: 'Login', layout: 'none' }
  },
  {
    path: '/dashboard',
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
  routes,
  // PT-BR: Scroll suave para links ancora (#features, #tech, #about) na landing page.
  // EN-US: Smooth scroll for anchor links (#features, #tech, #about) on the landing page.
  scrollBehavior(to) {
    if (to.hash) {
      return { el: to.hash, behavior: 'smooth' }
    }
    return { top: 0 }
  }
})

export default router
