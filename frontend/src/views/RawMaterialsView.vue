<template>
  <div class="raw-materials-view">
    <BaseAlert
      v-if="alert.message"
      :type="alert.type"
      :message="alert.message"
      @dismiss="clearAlert"
    />
    <RawMaterialList @alert="showAlert" />
  </div>
</template>

<script setup>
// PT-BR: View de materias-primas. Encapsula o componente de lista e gerencia os alertas
//        de feedback (sucesso/erro) que aparecem apos operacoes CRUD.
// EN-US: Raw materials view. Wraps the list component and manages feedback alerts
//        (success/error) that appear after CRUD operations.
import { reactive } from 'vue'
import BaseAlert from '@/components/ui/BaseAlert.vue'
import RawMaterialList from '@/components/raw-material/RawMaterialList.vue'

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
.raw-materials-view {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}
</style>
