<template>
  <header class="app-header">
    <div class="header-left">
      <button class="menu-toggle" @click="$emit('toggleSidebar')">
        &#9776;
      </button>
      <h2 class="page-title">{{ pageTitle }}</h2>
    </div>

    <div class="header-right">
      <select
        class="language-select"
        :value="locale"
        @change="changeLocale($event.target.value)"
      >
        <option value="pt-BR">PT-BR</option>
        <option value="en-US">EN-US</option>
      </select>
    </div>
  </header>
</template>

<script setup>
// PT-BR: Cabecalho da aplicacao com titulo dinamico baseado na rota atual
//        e seletor de idioma para alternar entre portugues e ingles.
// EN-US: Application header with dynamic title based on the current route
//        and language selector to switch between Portuguese and English.
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useI18n } from 'vue-i18n'

defineEmits(['toggleSidebar'])

const route = useRoute()
const { t, locale } = useI18n()

const pageTitle = computed(() => {
  const titleKey = route.meta.title
  return titleKey ? t(titleKey) : ''
})

function changeLocale(newLocale) {
  locale.value = newLocale
  localStorage.setItem('locale', newLocale)
}
</script>

<style scoped>
.app-header {
  height: var(--header-height);
  background-color: var(--color-bg-card);
  border-bottom: 1px solid var(--color-border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 var(--spacing-lg);
  position: sticky;
  top: 0;
  z-index: 50;
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.menu-toggle {
  display: none;
  background: none;
  border: none;
  font-size: 20px;
  color: var(--color-text-secondary);
  padding: var(--spacing-sm);
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.language-select {
  padding: var(--spacing-sm) var(--spacing-md);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background-color: var(--color-bg-card);
  color: var(--color-text-primary);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  outline: none;
  transition: border-color var(--transition-fast);
}

.language-select:focus {
  border-color: var(--color-action);
}

@media (max-width: 768px) {
  .menu-toggle {
    display: block;
  }
}
</style>
