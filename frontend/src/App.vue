<template>
  <!-- PT-BR: Layout condicional — páginas com meta.layout === 'none' (landing, login)
              renderizam sem o MainLayout (sidebar + header). Todas as outras usam. -->
  <!-- EN-US: Conditional layout — pages with meta.layout === 'none' (landing, login)
              render without MainLayout (sidebar + header). All others use it. -->
  <MainLayout v-if="requiresLayout">
    <RouterView />
  </MainLayout>
  <RouterView v-else />
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import MainLayout from '@/components/layout/MainLayout.vue'

const route = useRoute()
const requiresLayout = computed(() => route.meta.layout !== 'none')
</script>
