<template>
  <form @submit.prevent="handleSubmit">
    <div class="form-grid">
      <BaseInput
        v-model="form.code"
        :label="$t('product.code')"
        placeholder="PROD001"
        :error="errors.code"
        required
      />

      <BaseInput
        v-model="form.name"
        :label="$t('product.name')"
        placeholder="Bolo de Chocolate"
        :error="errors.name"
        required
      />

      <BaseInput
        v-model="form.salePrice"
        type="number"
        :label="$t('product.salePrice') + ' (R$)'"
        placeholder="45.00"
        step="0.01"
        min="0.01"
        :error="errors.salePrice"
        required
      />
    </div>

    <div class="composition-section">
      <CompositionEditor v-model="form.compositions" />
    </div>

    <div v-if="errors.general" class="form-error-banner">
      {{ errors.general }}
    </div>

    <div class="form-actions">
      <BaseButton variant="secondary" @click="$emit('cancelled')">{{ $t('common.cancel') }}</BaseButton>
      <BaseButton type="submit" :loading="saving">{{ $t('common.save') }}</BaseButton>
    </div>
  </form>
</template>

<script setup>
// PT-BR: Formulario de produto com editor de composicao embutido. Quando salvamos,
//        enviamos o produto e sua receita completa em uma unica chamada de API.
//        Isso simplifica a experiencia: o usuario nao precisa salvar o produto primeiro
//        e depois adicionar ingredientes separadamente.
// EN-US: Product form with embedded composition editor. When saving, we send the
//        product and its complete recipe in a single API call. This simplifies the
//        experience: the user doesn't need to save the product first and then add
//        ingredients separately.
import { ref, reactive, watch } from 'vue'
import { useProductStore } from '@/stores/product'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'
import CompositionEditor from './CompositionEditor.vue'

const props = defineProps({
  initialData: { type: Object, default: null }
})

const emit = defineEmits(['saved', 'cancelled'])
const store = useProductStore()
const saving = ref(false)

const form = reactive({
  code: '',
  name: '',
  salePrice: '',
  compositions: []
})

const errors = reactive({
  code: '',
  name: '',
  salePrice: '',
  general: ''
})

watch(() => props.initialData, (data) => {
  if (data) {
    form.code = data.code || ''
    form.name = data.name || ''
    form.salePrice = data.salePrice || ''
    form.compositions = data.compositions ? data.compositions.map(c => ({
      rawMaterialId: c.rawMaterialId,
      requiredQuantity: c.requiredQuantity
    })) : []
  } else {
    form.code = ''
    form.name = ''
    form.salePrice = ''
    form.compositions = []
  }
}, { immediate: true })

function validate() {
  let valid = true
  errors.code = ''
  errors.name = ''
  errors.salePrice = ''
  errors.general = ''

  if (!form.code.trim()) { errors.code = 'Code is required'; valid = false }
  if (!form.name.trim()) { errors.name = 'Name is required'; valid = false }
  if (!form.salePrice || Number(form.salePrice) <= 0) {
    errors.salePrice = 'Sale price must be greater than zero'
    valid = false
  }
  return valid
}

async function handleSubmit() {
  if (!validate()) return

  saving.value = true
  try {
    const payload = {
      code: form.code.trim(),
      name: form.name.trim(),
      salePrice: Number(form.salePrice),
      compositions: form.compositions
        .filter(c => c.rawMaterialId && c.requiredQuantity)
        .map(c => ({
          rawMaterialId: Number(c.rawMaterialId),
          requiredQuantity: Number(c.requiredQuantity)
        }))
    }

    if (props.initialData?.id) {
      await store.update(props.initialData.id, payload)
    } else {
      await store.create(payload)
    }
    emit('saved')
  } catch (e) {
    // PT-BR: Exibe erros do servidor para o usuario. Erros sobre codigo duplicado
    //        vao para o campo 'code'; todos os outros aparecem no banner geral.
    // EN-US: Shows server errors to the user. Duplicate code errors go to the
    //        'code' field; all others appear in the general banner.
    const serverError = e.response?.data?.error
    if (serverError) {
      if (serverError.toLowerCase().includes('code')) {
        errors.code = serverError
      } else {
        errors.general = serverError
      }
    } else {
      errors.general = e.message || 'An unexpected error occurred'
    }
  } finally {
    saving.value = false
  }
}
</script>

<style scoped>
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  gap: var(--spacing-md);
}

.composition-section {
  margin-top: var(--spacing-lg);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--color-border);
}

.form-error-banner {
  margin-top: var(--spacing-md);
  padding: var(--spacing-sm) var(--spacing-md);
  background-color: #fef2f2;
  border: 1px solid #fecaca;
  border-radius: var(--radius-md);
  color: #dc2626;
  font-size: 14px;
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
