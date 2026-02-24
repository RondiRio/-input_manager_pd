<template>
  <div class="optimization-view">
    <BaseAlert
      v-if="store.error"
      type="error"
      :message="store.error"
      @dismiss="store.clearResult()"
    />

    <BaseCard>
      <div class="optimization-header">
        <div>
          <h3>{{ $t('optimization.title') }}</h3>
          <p class="text-muted mt-sm">{{ $t('optimization.description') }}</p>
        </div>
        <BaseButton
          size="lg"
          :loading="store.loading"
          @click="store.runOptimization()"
        >
          {{ store.loading ? $t('optimization.running') : $t('optimization.run') }}
        </BaseButton>
      </div>
    </BaseCard>

    <OptimizationResult v-if="store.result" :result="store.result" />
  </div>
</template>

<script setup>
// PT-BR: View de otimizacao de producao. O usuario clica no botao, o algoritmo Branch
//        and Bound roda no backend e o resultado aparece com o plano de producao otimo.
//        Simples e direto -- como toda boa ferramenta deveria ser.
// EN-US: Production optimization view. The user clicks the button, the Branch and Bound
//        algorithm runs on the backend, and the result appears with the optimal production
//        plan. Simple and straightforward -- like every good tool should be.
import { useOptimizationStore } from '@/stores/optimization'
import BaseCard from '@/components/ui/BaseCard.vue'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseAlert from '@/components/ui/BaseAlert.vue'
import OptimizationResult from '@/components/optimization/OptimizationResult.vue'

const store = useOptimizationStore()
</script>

<style scoped>
.optimization-view {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.optimization-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: var(--spacing-lg);
}

.optimization-header h3 {
  font-size: 18px;
  font-weight: 600;
}

@media (max-width: 768px) {
  .optimization-header {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
