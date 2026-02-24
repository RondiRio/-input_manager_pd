<template>
  <aside class="sidebar" :class="{ collapsed: collapsed }">
    <div class="sidebar-header">
      <h1 class="sidebar-logo">
        <span class="logo-icon">IM</span>
        <span v-if="!collapsed" class="logo-text">Input Manager</span>
      </h1>
    </div>

    <nav class="sidebar-nav">
      <RouterLink
        v-for="item in navItems"
        :key="item.path"
        :to="item.path"
        class="nav-item"
        :class="{ active: $route.path === item.path }"
      >
        <span class="nav-icon" v-html="item.icon"></span>
        <span v-if="!collapsed" class="nav-label">{{ $t(item.label) }}</span>
      </RouterLink>
    </nav>

    <button class="sidebar-toggle" @click="$emit('toggle')">
      <span v-html="collapsed ? '&#9654;' : '&#9664;'"></span>
    </button>
  </aside>
</template>

<script setup>
// PT-BR: Barra lateral de navegacao com a identidade visual da Projedata (azul escuro).
//        Pode ser recolhida para dar mais espaco ao conteudo principal.
// EN-US: Navigation sidebar with Projedata's visual identity (dark blue).
//        Can be collapsed to give more space to the main content.
defineProps({
  collapsed: {
    type: Boolean,
    default: false
  }
})

defineEmits(['toggle'])

const navItems = [
  { path: '/', label: 'nav.dashboard', icon: '&#9632;' },
  { path: '/raw-materials', label: 'nav.rawMaterials', icon: '&#9650;' },
  { path: '/products', label: 'nav.products', icon: '&#9679;' },
  { path: '/optimization', label: 'nav.optimization', icon: '&#9733;' }
]
</script>

<style scoped>
.sidebar {
  width: var(--sidebar-width);
  height: 100vh;
  background-color: var(--color-identity);
  color: var(--color-identity-text);
  display: flex;
  flex-direction: column;
  position: fixed;
  left: 0;
  top: 0;
  transition: width var(--transition-normal);
  z-index: 100;
}

.sidebar.collapsed {
  width: 64px;
}

.sidebar-header {
  padding: var(--spacing-lg);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.sidebar-logo {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: 18px;
  font-weight: 700;
  white-space: nowrap;
  overflow: hidden;
}

.logo-icon {
  width: 32px;
  height: 32px;
  background-color: var(--color-action);
  border-radius: var(--radius);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  font-weight: 700;
  flex-shrink: 0;
}

.logo-text {
  overflow: hidden;
}

.sidebar-nav {
  flex: 1;
  padding: var(--spacing-md) 0;
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.nav-item {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  padding: var(--spacing-md) var(--spacing-lg);
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  transition: all var(--transition-fast);
  border-left: 3px solid transparent;
  white-space: nowrap;
  overflow: hidden;
}

.nav-item:hover {
  background-color: var(--color-identity-light);
  color: #FFFFFF;
}

.nav-item.active {
  background-color: rgba(0, 102, 255, 0.15);
  color: #FFFFFF;
  border-left-color: var(--color-action);
}

.nav-icon {
  font-size: 16px;
  width: 20px;
  text-align: center;
  flex-shrink: 0;
}

.nav-label {
  font-size: 14px;
  font-weight: 500;
}

.sidebar-toggle {
  padding: var(--spacing-md);
  background: none;
  border: none;
  color: rgba(255, 255, 255, 0.5);
  font-size: 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  transition: color var(--transition-fast);
}

.sidebar-toggle:hover {
  color: #FFFFFF;
}
</style>
