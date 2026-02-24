<template>
  <form @submit.prevent="handleSubmit">
    <div class="form-grid">
      <BaseInput
        v-model="form.code"
        :label="$t('rawMaterial.code')"
        :placeholder="'MP001'"
        :error="errors.code"
        required
      />

      <BaseInput
        v-model="form.name"
        :label="$t('rawMaterial.name')"
        :placeholder="'Farinha de Trigo'"
        :error="errors.name"
        required
      />

      <BaseInput
        v-model="form.stockQuantity"
        type="number"
        :label="$t('rawMaterial.stockQuantity')"
        :placeholder="'500'"
        step="0.0001"
        min="0"
        :error="errors.stockQuantity"
        required
      />

      <BaseInput
        v-model="form.unit"
        type="select"
        :label="$t('rawMaterial.unit')"
        :options="unitOptions"
        :error="errors.unit"
        required
      />
    </div>

    <div class="form-actions">
      <BaseButton variant="secondary" @click="$emit('cancelled')">{{ $t('common.cancel') }}</BaseButton>
      <BaseButton type="submit" :loading="saving">{{ $t('common.save') }}</BaseButton>
    </div>
  </form>
</template>

<script setup>
// PT-BR: Formulario de materia-prima com validacao no lado do cliente.
//        Pre-preenche os campos quando estamos editando um item existente.
//        A validacao local da feedback instantaneo ao usuario antes de enviar
//        para o servidor, economizando tempo e evitando frustracao.
// EN-US: Raw material form with client-side validation.
//        Pre-fills fields when editing an existing item.
//        Local validation gives instant feedback to the user before sending
//        to the server, saving time and avoiding frustration.
import { ref, reactive, watch } from 'vue'
import { useRawMaterialStore } from '@/stores/rawMaterial'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const props = defineProps({
  initialData: { type: Object, default: null }
})

const emit = defineEmits(['saved', 'cancelled'])
const store = useRawMaterialStore()
const saving = ref(false)

const form = reactive({
  code: '',
  name: '',
  stockQuantity: '',
  unit: 'g'
})

const errors = reactive({
  code: '',
  name: '',
  stockQuantity: '',
  unit: ''
})

const unitOptions = [
  { value: 'g', label: 'g (gramas)' },
  { value: 'kg', label: 'kg (quilogramas)' },
  { value: 'ml', label: 'ml (mililitros)' },
  { value: 'L', label: 'L (litros)' },
  { value: 'un', label: 'un (unidades)' }
]

watch(() => props.initialData, (data) => {
  if (data) {
    form.code = data.code || ''
    form.name = data.name || ''
    form.stockQuantity = data.stockQuantity || ''
    form.unit = data.unit || 'g'
  } else {
    form.code = ''
    form.name = ''
    form.stockQuantity = ''
    form.unit = 'g'
  }
}, { immediate: true })

function validate() {
  let valid = true
  errors.code = ''
  errors.name = ''
  errors.stockQuantity = ''
  errors.unit = ''

  if (!form.code.trim()) { errors.code = 'Code is required'; valid = false }
  if (!form.name.trim()) { errors.name = 'Name is required'; valid = false }
  if (!form.stockQuantity || Number(form.stockQuantity) < 0) {
    errors.stockQuantity = 'Stock quantity must be zero or positive'
    valid = false
  }
  if (!form.unit) { errors.unit = 'Unit is required'; valid = false }
  return valid
}

async function handleSubmit() {
  if (!validate()) return

  saving.value = true
  try {
    const payload = {
      code: form.code.trim(),
      name: form.name.trim(),
      stockQuantity: Number(form.stockQuantity),
      unit: form.unit
    }

    if (props.initialData?.id) {
      await store.update(props.initialData.id, payload)
    } else {
      await store.create(payload)
    }
    emit('saved')
  } catch (e) {
    const serverError = e.response?.data?.error
    if (serverError && serverError.includes('code')) {
      errors.code = serverError
    }
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--spacing-md);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-sm);
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-md);
  border-top: 1px solid var(--color-border);
}
</style>
