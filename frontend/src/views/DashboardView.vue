<template>
  <div class="dashboard">
    <!-- PT-BR: Cartoes de resumo com metricas principais -->
    <!-- EN-US: Summary cards with key metrics -->
    <div class="stats-grid">
      <div class="stat-card">
        <span class="stat-label">{{ $t('dashboard.totalRawMaterials') }}</span>
        <span class="stat-value">{{ rawMaterialStore.items.length }}</span>
      </div>
      <div class="stat-card">
        <span class="stat-label">{{ $t('dashboard.totalProducts') }}</span>
        <span class="stat-value">{{ productStore.items.length }}</span>
      </div>
      <div class="stat-card accent">
        <span class="stat-label">{{ $t('dashboard.totalStockValue') }}</span>
        <span class="stat-value">{{ formatCurrency(totalProductValue) }}</span>
      </div>
    </div>

    <!-- PT-BR: Acoes rapidas para as funcionalidades mais usadas -->
    <!-- EN-US: Quick actions for the most used features -->
    <BaseCard :title="$t('dashboard.quickActions')">
      <div class="quick-actions">
        <RouterLink to="/raw-materials" class="quick-action-btn">
          <span class="qa-icon">&#9650;</span>
          <span>{{ $t('dashboard.addRawMaterial') }}</span>
        </RouterLink>
        <RouterLink to="/products" class="quick-action-btn">
          <span class="qa-icon">&#9679;</span>
          <span>{{ $t('dashboard.addProduct') }}</span>
        </RouterLink>
        <RouterLink to="/optimization" class="quick-action-btn highlight">
          <span class="qa-icon">&#9733;</span>
          <span>{{ $t('dashboard.runOptimization') }}</span>
        </RouterLink>
      </div>
    </BaseCard>
  </div>
</template>

<script setup>
// PT-BR: Painel de controle com visao geral do sistema. Mostra o total de materias-primas,
//        produtos e o valor total em estoque. As acoes rapidas permitem ao usuario navegar
//        diretamente para as funcionalidades mais usadas.
// EN-US: Control panel with system overview. Shows the total raw materials, products, and
//        total stock value. Quick actions allow the user to navigate directly to the most
//        used features.
import { computed, onMounted } from 'vue'
import { useRawMaterialStore } from '@/stores/rawMaterial'
import { useProductStore } from '@/stores/product'
import BaseCard from '@/components/ui/BaseCard.vue'

const rawMaterialStore = useRawMaterialStore()
const productStore = useProductStore()

onMounted(() => {
  rawMaterialStore.fetchAll()
  productStore.fetchAll()
})

const totalProductValue = computed(() => {
  return productStore.items.reduce((sum, p) => sum + (p.salePrice || 0), 0)
})

function formatCurrency(val) {
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(val)
}
</script>

<style scoped>
.dashboard {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--spacing-md);
}

.stat-card {
  background-color: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: var(--spacing-lg);
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.stat-card.accent {
  background: linear-gradient(135deg, var(--color-identity) 0%, var(--color-identity-light) 100%);
  color: #FFFFFF;
  border: none;
}

.stat-label {
  font-size: 13px;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  opacity: 0.7;
}

.stat-value {
  font-size: 32px;
  font-weight: 700;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--spacing-md);
}

.quick-action-btn {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-sm);
  padding: var(--spacing-lg);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  color: var(--color-text-primary);
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  transition: all var(--transition-fast);
}

.quick-action-btn:hover {
  border-color: var(--color-action);
  background-color: var(--color-action-light);
  color: var(--color-action);
}

.quick-action-btn.highlight {
  border-color: var(--color-action);
  background-color: var(--color-action-light);
  color: var(--color-action);
}

.qa-icon {
  font-size: 24px;
}

@media (max-width: 768px) {
  .stats-grid,
  .quick-actions {
    grid-template-columns: 1fr;
  }
}
</style>
