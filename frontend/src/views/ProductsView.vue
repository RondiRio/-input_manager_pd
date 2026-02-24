<template>
  <div class="products-view">
    <BaseAlert
      v-if="alert.message"
      :type="alert.type"
      :message="alert.message"
      @dismiss="clearAlert"
    />
    <ProductList @alert="showAlert" />
  </div>
</template>

<script setup>
// PT-BR: View de produtos. Mesma estrutura da RawMaterialsView, mantendo consistencia
//        na experiencia do usuario entre as diferentes paginas do sistema.
// EN-US: Products view. Same structure as RawMaterialsView, keeping consistency
//        in the user experience across different system pages.
import { reactive } from 'vue'
import BaseAlert from '@/components/ui/BaseAlert.vue'
import ProductList from '@/components/product/ProductList.vue'

const alert = reactive({ type: 'info', message: '' })

function showAlert({ type, message }) {
  alert.type = type
  alert.message = message
  setTimeout(() => clearAlert(), 4000)
}

function clearAlert() {
  alert.message = ''
}
</script>

<style scoped>
.products-view {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}
</style>
