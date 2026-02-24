<template>
  <div class="app-layout">
    <AppSidebar :collapsed="sidebarCollapsed" @toggle="sidebarCollapsed = !sidebarCollapsed" />

    <div class="main-area" :class="{ 'sidebar-collapsed': sidebarCollapsed }">
      <AppHeader @toggleSidebar="sidebarCollapsed = !sidebarCollapsed" />

      <main class="main-content">
        <slot />
      </main>
    </div>
  </div>
</template>

<script setup>
// PT-BR: Layout principal que organiza a estrutura da pagina com barra lateral fixa
//        e area de conteudo flexivel. O layout se adapta quando a barra lateral e
//        recolhida, dando mais espaco ao conteudo.
// EN-US: Main layout that organizes the page structure with a fixed sidebar and
//        flexible content area. The layout adapts when the sidebar is collapsed,
//        giving more space to the content.
import { ref } from 'vue'
import AppSidebar from './AppSidebar.vue'
import AppHeader from './AppHeader.vue'

const sidebarCollapsed = ref(false)
</script>

<style scoped>
.app-layout {
  display: flex;
  min-height: 100vh;
}

.main-area {
  flex: 1;
  margin-left: var(--sidebar-width);
  transition: margin-left var(--transition-normal);
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.main-area.sidebar-collapsed {
  margin-left: 64px;
}

.main-content {
  flex: 1;
  padding: var(--spacing-lg);
}

@media (max-width: 768px) {
  .main-area {
    margin-left: 0;
  }
}
</style>
