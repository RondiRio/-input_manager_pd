<template>
  <div class="optimization-result">
    <!-- PT-BR: Cartao de receita total com destaque visual grande -->
    <!-- EN-US: Total revenue card with large visual highlight -->
    <div class="revenue-card">
      <span class="revenue-label">{{ $t('optimization.totalRevenue') }}</span>
      <span class="revenue-value">{{ formatCurrency(result.totalRevenue) }}</span>
      <span class="computation-time">
        {{ $t('optimization.computationTime') }}: {{ result.computationTimeMs }}ms
      </span>
    </div>

    <!-- PT-BR: Plano de producao -->
    <!-- EN-US: Production plan -->
    <BaseCard :title="$t('optimization.productionPlan')">
      <div v-if="hasProduction" class="plan-table-container">
        <table class="plan-table">
          <thead>
            <tr>
              <th>{{ $t('product.code') }}</th>
              <th>{{ $t('product.name') }}</th>
              <th class="text-right">{{ $t('optimization.quantityToProduce') }}</th>
              <th class="text-right">{{ $t('optimization.unitPrice') }}</th>
              <th class="text-right">{{ $t('optimization.subtotal') }}</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="item in result.productionPlan"
              :key="item.productId"
              :class="{ 'zero-production': item.quantityToProduce === 0 }"
            >
              <td>{{ item.productCode }}</td>
              <td>{{ item.productName }}</td>
              <td class="text-right font-semibold">{{ item.quantityToProduce }}</td>
              <td class="text-right">{{ formatCurrency(item.unitPrice) }}</td>
              <td class="text-right font-semibold">{{ formatCurrency(item.subtotal) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-else class="empty-plan">
        <p class="text-muted">{{ $t('optimization.noProduction') }}</p>
      </div>
    </BaseCard>

    <!-- PT-BR: Uso de materiais com barras de progresso visuais -->
    <!-- EN-US: Material usage with visual progress bars -->
    <BaseCard :title="$t('optimization.materialUsage')">
      <div class="material-usage-list">
        <div v-for="mat in result.materialUsage" :key="mat.rawMaterialId" class="material-item">
          <div class="material-header">
            <span class="material-name">{{ mat.rawMaterialName }}</span>
            <span class="material-stats">
              {{ formatNumber(mat.totalUsed) }} / {{ formatNumber(mat.totalAvailable) }} {{ mat.unit }}
            </span>
          </div>
          <div class="progress-bar-container">
            <div
              class="progress-bar-fill"
              :class="getUsageColorClass(mat.usagePercentage)"
              :style="{ width: Math.min(mat.usagePercentage, 100) + '%' }"
            ></div>
          </div>
          <div class="material-footer">
            <span class="text-muted">
              {{ $t('optimization.remaining') }}: {{ formatNumber(mat.remaining) }} {{ mat.unit }}
            </span>
            <span class="usage-percentage" :class="getUsageColorClass(mat.usagePercentage)">
              {{ mat.usagePercentage.toFixed(1) }}%
            </span>
          </div>
        </div>
      </div>
    </BaseCard>
  </div>
</template>

<script setup>
// PT-BR: Componente que exibe o resultado da otimizacao de producao. Mostra a receita
//        total, o plano de producao detalhado e o uso de cada materia-prima com barras
//        de progresso coloridas (verde < 50%, amarelo 50-80%, vermelho > 80%).
//        A ideia e dar ao gerente da fabrica uma visao clara e imediata do que produzir.
// EN-US: Component displaying the production optimization result. Shows the total
//        revenue, detailed production plan, and usage of each raw material with colored
//        progress bars (green < 50%, yellow 50-80%, red > 80%).
//        The idea is to give the factory manager a clear and immediate view of what to produce.
import BaseCard from '@/components/ui/BaseCard.vue'
import { computed } from 'vue'

const props = defineProps({
  result: { type: Object, required: true }
})

const hasProduction = computed(() =>
  props.result.productionPlan?.some(item => item.quantityToProduce > 0)
)

function formatCurrency(val) {
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(val || 0)
}

function formatNumber(val) {
  return new Intl.NumberFormat('pt-BR', { maximumFractionDigits: 4 }).format(val || 0)
}

function getUsageColorClass(percentage) {
  if (percentage > 80) return 'usage-high'
  if (percentage > 50) return 'usage-medium'
  return 'usage-low'
}
</script>

<style scoped>
.optimization-result {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.revenue-card {
  background: linear-gradient(135deg, var(--color-identity) 0%, var(--color-identity-light) 100%);
  color: #FFFFFF;
  border-radius: var(--radius-lg);
  padding: var(--spacing-xl);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-sm);
}

.revenue-label {
  font-size: 14px;
  opacity: 0.8;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.revenue-value {
  font-size: 42px;
  font-weight: 700;
  letter-spacing: -0.02em;
}

.computation-time {
  font-size: 12px;
  opacity: 0.6;
}

.plan-table-container {
  overflow-x: auto;
}

.plan-table {
  width: 100%;
  border-collapse: collapse;
}

.plan-table th {
  text-align: left;
  padding: var(--spacing-sm) var(--spacing-md);
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  color: var(--color-text-secondary);
  border-bottom: 2px solid var(--color-border);
}

.plan-table td {
  padding: var(--spacing-md);
  border-bottom: 1px solid var(--color-border);
  font-size: 14px;
}

.plan-table .text-right { text-align: right; }

.zero-production {
  opacity: 0.4;
}

.empty-plan {
  padding: var(--spacing-xl);
  text-align: center;
}

.material-usage-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.material-item {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.material-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.material-name {
  font-weight: 500;
  font-size: 14px;
}

.material-stats {
  font-size: 13px;
  color: var(--color-text-secondary);
}

.progress-bar-container {
  width: 100%;
  height: 8px;
  background-color: var(--color-bg-primary);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.progress-bar-fill {
  height: 100%;
  border-radius: var(--radius-full);
  transition: width var(--transition-normal);
}

.progress-bar-fill.usage-low { background-color: var(--color-success); }
.progress-bar-fill.usage-medium { background-color: var(--color-warning); }
.progress-bar-fill.usage-high { background-color: var(--color-danger); }

.material-footer {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
}

.usage-percentage { font-weight: 600; }
.usage-percentage.usage-low { color: var(--color-success); }
.usage-percentage.usage-medium { color: var(--color-warning); }
.usage-percentage.usage-high { color: var(--color-danger); }
</style>
