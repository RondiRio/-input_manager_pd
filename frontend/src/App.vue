<template>
  <MainLayout>
    <RouterView />
  </MainLayout>
</template>

<script setup>
// PT-BR: Componente raiz da aplicacao. Envolve todas as rotas no layout principal
//        que inclui a barra lateral e o cabecalho.
// EN-US: Root application component. Wraps all routes in the main layout that
//        includes the sidebar and header.
import MainLayout from '@/components/layout/MainLayout.vue'
</script>
