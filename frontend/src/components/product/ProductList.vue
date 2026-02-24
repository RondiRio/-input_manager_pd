<template>
  <BaseCard :title="$t('product.title')" no-padding>
    <template #actions>
      <BaseButton @click="openCreateForm">
        + {{ $t('product.addNew') }}
      </BaseButton>
    </template>

    <DataTable
      :columns="columns"
      :data="store.items"
      :loading="store.loading"
      @edit="openEditForm"
      @delete="confirmDelete"
    >
      <template #cell-salePrice="{ row }">
        {{ formatCurrency(row.salePrice) }}
      </template>
      <template #cell-compositions="{ row }">
        <span class="composition-badge">
          {{ row.compositions ? row.compositions.length : 0 }} {{ row.compositions?.length === 1 ? 'item' : 'itens' }}
        </span>
      </template>
    </DataTable>
  </BaseCard>

  <BaseModal
    :visible="showForm"
    :title="editingItem ? $t('product.edit') : $t('product.addNew')"
    size="lg"
    @close="closeForm"
  >
    <ProductForm
      :initial-data="editingItem"
      @saved="handleSaved"
      @cancelled="closeForm"
    />
  </BaseModal>

  <BaseModal :visible="showDeleteConfirm" :title="$t('common.confirm')" size="sm" @close="showDeleteConfirm = false">
    <p>{{ $t('product.deleteConfirm') }}</p>
    <template #footer>
      <BaseButton variant="secondary" @click="showDeleteConfirm = false">{{ $t('common.cancel') }}</BaseButton>
      <BaseButton variant="danger" :loading="deleting" @click="executeDelete">{{ $t('common.delete') }}</BaseButton>
    </template>
  </BaseModal>
</template>

<script setup>
// PT-BR: Componente de lista de produtos, similar ao RawMaterialList mas com a adicao
//        de exibir o numero de itens na composicao e formatar precos em BRL.
// EN-US: Product list component, similar to RawMaterialList but with the addition of
//        displaying the number of composition items and formatting prices in BRL.
import { ref, computed, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { useProductStore } from '@/stores/product'
import BaseCard from '@/components/ui/BaseCard.vue'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseModal from '@/components/ui/BaseModal.vue'
import DataTable from '@/components/ui/DataTable.vue'
import ProductForm from './ProductForm.vue'

const emit = defineEmits(['alert'])
const { t } = useI18n()
const store = useProductStore()

const showForm = ref(false)
const editingItem = ref(null)
const showDeleteConfirm = ref(false)
const deletingItem = ref(null)
const deleting = ref(false)

const columns = computed(() => [
  { key: 'code', label: t('rawMaterial.code'), sortable: true },
  { key: 'name', label: t('rawMaterial.name'), sortable: true },
  { key: 'salePrice', label: t('product.salePrice'), sortable: true },
  { key: 'compositions', label: t('product.composition'), sortable: false }
])

onMounted(() => { store.fetchAll() })

function formatCurrency(val) {
  return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(val)
}

function openCreateForm() { editingItem.value = null; showForm.value = true }
function openEditForm(row) { editingItem.value = { ...row }; showForm.value = true }
function closeForm() { showForm.value = false; editingItem.value = null }

async function handleSaved() {
  closeForm()
  emit('alert', { type: 'success', message: editingItem.value ? t('product.updated') : t('product.created') })
}

function confirmDelete(row) { deletingItem.value = row; showDeleteConfirm.value = true }

async function executeDelete() {
  if (!deletingItem.value) return
  deleting.value = true
  try {
    await store.remove(deletingItem.value.id)
    emit('alert', { type: 'success', message: t('product.deleted') })
  } catch (e) {
    emit('alert', { type: 'error', message: e.response?.data?.error || t('common.error') })
  } finally {
    deleting.value = false
    showDeleteConfirm.value = false
    deletingItem.value = null
  }
}
</script>

<style scoped>
.composition-badge {
  display: inline-block;
  padding: 2px 8px;
  background-color: var(--color-action-light);
  color: var(--color-action);
  border-radius: var(--radius-full);
  font-size: 12px;
  font-weight: 500;
}
</style>
