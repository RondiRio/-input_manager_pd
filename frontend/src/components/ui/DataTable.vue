<template>
  <div class="table-container">
    <div v-if="loading" class="table-loading">
      <span class="spinner"></span>
      <span>{{ $t('common.loading') }}</span>
    </div>

    <table v-else-if="data.length > 0" class="data-table">
      <thead>
        <tr>
          <th
            v-for="col in columns"
            :key="col.key"
            :class="{ sortable: col.sortable, sorted: sortKey === col.key }"
            :style="col.width ? { width: col.width } : {}"
            @click="col.sortable && toggleSort(col.key)"
          >
            {{ col.label }}
            <span v-if="col.sortable" class="sort-indicator">
              {{ sortKey === col.key ? (sortOrder === 'asc' ? '&#9650;' : '&#9660;') : '&#9670;' }}
            </span>
          </th>
          <th v-if="showActions" class="actions-col">{{ $t('common.actions') }}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(row, index) in sortedData" :key="row.id || index">
          <td v-for="col in columns" :key="col.key">
            <slot :name="`cell-${col.key}`" :row="row" :value="row[col.key]">
              {{ formatValue(row[col.key], col) }}
            </slot>
          </td>
          <td v-if="showActions" class="actions-cell">
            <button class="action-btn edit-btn" @click="$emit('edit', row)" :title="$t('common.edit')">
              &#9998;
            </button>
            <button class="action-btn delete-btn" @click="$emit('delete', row)" :title="$t('common.delete')">
              &#10005;
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <div v-else class="table-empty">
      <p>{{ $t('common.noData') }}</p>
    </div>
  </div>
</template>

<script setup>
// PT-BR: Tabela de dados reutilizavel com ordenacao, estado vazio e acoes por linha.
//        Aceita colunas e dados via props, e emite eventos de editar/deletar para que
//        o componente pai decida o que fazer. Isso mantem a tabela flexivel e desacoplada.
// EN-US: Reusable data table with sorting, empty state, and per-row actions.
//        Accepts columns and data via props, and emits edit/delete events so the
//        parent component decides what to do. This keeps the table flexible and decoupled.
import { ref, computed } from 'vue'

const props = defineProps({
  columns: { type: Array, required: true },
  data: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  showActions: { type: Boolean, default: true }
})

defineEmits(['edit', 'delete'])

const sortKey = ref('')
const sortOrder = ref('asc')

function toggleSort(key) {
  if (sortKey.value === key) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc'
  } else {
    sortKey.value = key
    sortOrder.value = 'asc'
  }
}

const sortedData = computed(() => {
  if (!sortKey.value) return props.data

  return [...props.data].sort((a, b) => {
    let valA = a[sortKey.value]
    let valB = b[sortKey.value]

    if (typeof valA === 'number' && typeof valB === 'number') {
      return sortOrder.value === 'asc' ? valA - valB : valB - valA
    }

    valA = String(valA || '').toLowerCase()
    valB = String(valB || '').toLowerCase()
    const result = valA.localeCompare(valB)
    return sortOrder.value === 'asc' ? result : -result
  })
})

function formatValue(value, col) {
  if (value == null) return '-'
  if (col.format === 'currency') {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value)
  }
  if (col.format === 'number') {
    return new Intl.NumberFormat('pt-BR').format(value)
  }
  return value
}
</script>

<style scoped>
.table-container {
  width: 100%;
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th {
  text-align: left;
  padding: var(--spacing-md) var(--spacing-lg);
  background-color: var(--color-identity);
  color: var(--color-identity-text);
  font-size: 13px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  white-space: nowrap;
  user-select: none;
}

.data-table th:first-child {
  border-top-left-radius: var(--radius);
}

.data-table th:last-child {
  border-top-right-radius: var(--radius);
}

.data-table th.sortable {
  cursor: pointer;
  transition: background-color var(--transition-fast);
}

.data-table th.sortable:hover {
  background-color: var(--color-identity-light);
}

.sort-indicator {
  font-size: 10px;
  margin-left: var(--spacing-xs);
  opacity: 0.6;
}

.data-table th.sorted .sort-indicator {
  opacity: 1;
}

.data-table td {
  padding: var(--spacing-md) var(--spacing-lg);
  border-bottom: 1px solid var(--color-border);
  font-size: 14px;
}

.data-table tbody tr {
  transition: background-color var(--transition-fast);
}

.data-table tbody tr:hover {
  background-color: var(--color-bg-hover);
}

.actions-col {
  width: 100px;
  text-align: center;
}

.actions-cell {
  display: flex;
  gap: var(--spacing-sm);
  justify-content: center;
}

.action-btn {
  width: 32px;
  height: 32px;
  border: none;
  border-radius: var(--radius-sm);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  transition: all var(--transition-fast);
}

.edit-btn {
  background-color: var(--color-action-light);
  color: var(--color-action);
}

.edit-btn:hover {
  background-color: var(--color-action);
  color: #FFFFFF;
}

.delete-btn {
  background-color: var(--color-danger-light);
  color: var(--color-danger);
}

.delete-btn:hover {
  background-color: var(--color-danger);
  color: #FFFFFF;
}

.table-loading,
.table-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-2xl);
  color: var(--color-text-secondary);
  gap: var(--spacing-md);
}

.spinner {
  width: 24px;
  height: 24px;
  border: 3px solid var(--color-border);
  border-top-color: var(--color-action);
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
