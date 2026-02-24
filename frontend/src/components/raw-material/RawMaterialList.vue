<template>
  <BaseCard :title="$t('rawMaterial.title')" no-padding>
    <template #actions>
      <BaseButton @click="openCreateForm">
        + {{ $t('rawMaterial.addNew') }}
      </BaseButton>
    </template>

    <DataTable
      :columns="columns"
      :data="store.items"
      :loading="store.loading"
      @edit="openEditForm"
      @delete="confirmDelete"
    >
      <template #cell-stockQuantity="{ row }">
        {{ formatNumber(row.stockQuantity) }} {{ row.unit }}
      </template>
    </DataTable>
  </BaseCard>

  <BaseModal :visible="showForm" :title="editingItem ? $t('rawMaterial.edit') : $t('rawMaterial.addNew')" @close="closeForm">
    <RawMaterialForm
      :initial-data="editingItem"
      @saved="handleSaved"
      @cancelled="closeForm"
    />
  </BaseModal>

  <BaseModal :visible="showDeleteConfirm" :title="$t('common.confirm')" size="sm" @close="showDeleteConfirm = false">
    <p>{{ $t('rawMaterial.deleteConfirm') }}</p>
    <p class="text-muted mt-sm">{{ $t('rawMaterial.deleteWarning') }}</p>
    <template #footer>
      <BaseButton variant="secondary" @click="showDeleteConfirm = false">{{ $t('common.cancel') }}</BaseButton>
      <BaseButton variant="danger" :loading="deleting" @click="executeDelete">{{ $t('common.delete') }}</BaseButton>
    </template>
  </BaseModal>
</template>

<script setup>
// PT-BR: Componente de lista de materias-primas com tabela, formulario modal e
//        confirmacao de exclusao. Centraliza todas as operacoes CRUD em um unico
//        lugar, mantendo a experiencia do usuario fluida e previsivel.
// EN-US: Raw material list component with table, modal form, and delete confirmation.
//        Centralizes all CRUD operations in one place, keeping the user experience
//        smooth and predictable.
import { ref, computed, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { useRawMaterialStore } from '@/stores/rawMaterial'
import BaseCard from '@/components/ui/BaseCard.vue'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseModal from '@/components/ui/BaseModal.vue'
import DataTable from '@/components/ui/DataTable.vue'
import RawMaterialForm from './RawMaterialForm.vue'

const emit = defineEmits(['alert'])
const { t } = useI18n()
const store = useRawMaterialStore()

const showForm = ref(false)
const editingItem = ref(null)
const showDeleteConfirm = ref(false)
const deletingItem = ref(null)
const deleting = ref(false)

const columns = computed(() => [
  { key: 'code', label: t('rawMaterial.code'), sortable: true },
  { key: 'name', label: t('rawMaterial.name'), sortable: true },
  { key: 'stockQuantity', label: t('rawMaterial.stockQuantity'), sortable: true },
  { key: 'unit', label: t('rawMaterial.unit'), sortable: false }
])

onMounted(() => { store.fetchAll() })

function formatNumber(val) {
  return new Intl.NumberFormat('pt-BR', { maximumFractionDigits: 4 }).format(val)
}

function openCreateForm() {
  editingItem.value = null
  showForm.value = true
}

function openEditForm(row) {
  editingItem.value = { ...row }
  showForm.value = true
}

function closeForm() {
  showForm.value = false
  editingItem.value = null
}

async function handleSaved() {
  closeForm()
  emit('alert', { type: 'success', message: editingItem.value ? t('rawMaterial.updated') : t('rawMaterial.created') })
}

function confirmDelete(row) {
  deletingItem.value = row
  showDeleteConfirm.value = true
}

async function executeDelete() {
  if (!deletingItem.value) return
  deleting.value = true
  try {
    await store.remove(deletingItem.value.id)
    emit('alert', { type: 'success', message: t('rawMaterial.deleted') })
  } catch (e) {
    emit('alert', { type: 'error', message: e.response?.data?.error || t('common.error') })
  } finally {
    deleting.value = false
    showDeleteConfirm.value = false
    deletingItem.value = null
  }
}
</script>
