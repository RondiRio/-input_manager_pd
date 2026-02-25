<template>
  <div class="composition-editor">
    <label class="editor-label">{{ $t('product.composition') }}</label>

    <div v-if="items.length === 0" class="empty-state">
      <p class="text-muted">{{ $t('product.noComposition') }}</p>
    </div>

    <div v-for="(item, index) in items" :key="index" class="composition-row">
      <BaseInput
        v-model="item.rawMaterialId"
        type="select"
        :label="index === 0 ? $t('rawMaterial.name') : ''"
        :placeholder="$t('product.selectMaterial')"
        :options="availableMaterials(index)"
      />

      <BaseInput
        v-model="item.requiredQuantity"
        type="number"
        :label="index === 0 ? $t('product.requiredQuantity') : ''"
        placeholder="0.0000"
        step="0.0001"
        min="0.0001"
      />

      <div class="unit-display" :class="{ 'with-label': index === 0 }">
        <span v-if="index === 0" class="unit-label-spacer">&nbsp;</span>
        {{ getUnitForMaterial(item.rawMaterialId) }}
      </div>

      <button
        class="remove-btn"
        :class="{ 'with-label': index === 0 }"
        @click="removeItem(index)"
        type="button"
      >
        &#10005;
      </button>
    </div>

    <BaseButton variant="secondary" size="sm" @click="addItem" type="button">
      + {{ $t('product.addMaterial') }}
    </BaseButton>
  </div>
</template>

<script setup>
// PT-BR: Editor de composicao do produto -- a parte mais interativa do formulario.
//        O usuario pode adicionar e remover linhas de materia-prima, cada uma com uma
//        quantidade. Impedimos duplicatas: se uma materia-prima ja foi selecionada em
//        outra linha, ela nao aparece no dropdown. Isso evita erros bobos e torna
//        a experiencia mais intuitiva.
//
//        IMPORTANTE: usamos a flag `isEmitting` para evitar um loop circular entre os
//        watchers de `items` e `modelValue`. Sem ela, qualquer mudanca no usuario causa
//        items->emit->modelValue->items->emit->... infinitamente, perdendo dados.
//
// EN-US: Product composition editor -- the most interactive part of the form.
//        The user can add and remove raw material lines, each with a quantity.
//        We prevent duplicates: if a raw material is already selected in another
//        line, it doesn't show in the dropdown. This avoids silly mistakes and
//        makes the experience more intuitive.
//
//        IMPORTANT: we use the `isEmitting` flag to prevent a circular loop between
//        the `items` and `modelValue` watchers. Without it, any user change causes
//        items->emit->modelValue->items->emit->... infinitely, losing data.
import { ref, watch, onMounted, nextTick } from 'vue'
import { useRawMaterialStore } from '@/stores/rawMaterial'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const props = defineProps({
  modelValue: { type: Array, default: () => [] }
})

const emit = defineEmits(['update:modelValue'])
const rawMaterialStore = useRawMaterialStore()

const items = ref([])
const isEmitting = ref(false)

onMounted(() => {
  if (rawMaterialStore.items.length === 0) {
    rawMaterialStore.fetchAll()
  }
})

// PT-BR: Recebe dados do pai (modelValue) e popula items locais.
//        Ignora quando a mudanca veio do proprio emit (flag isEmitting).
// EN-US: Receives data from parent (modelValue) and populates local items.
//        Ignores when the change came from our own emit (isEmitting flag).
watch(() => props.modelValue, (val) => {
  if (isEmitting.value) return
  if (val && val.length > 0) {
    items.value = val.map(c => ({
      rawMaterialId: String(c.rawMaterialId || ''),
      requiredQuantity: c.requiredQuantity != null ? String(c.requiredQuantity) : ''
    }))
  } else {
    items.value = []
  }
}, { immediate: true, deep: true })

// PT-BR: Quando o usuario muda algo nos items, emite para o pai.
//        A flag isEmitting impede que o watcher do modelValue responda
//        e recrie os items, causando loop infinito.
// EN-US: When the user changes something in items, emits to parent.
//        The isEmitting flag prevents the modelValue watcher from responding
//        and recreating items, causing an infinite loop.
watch(items, (val) => {
  isEmitting.value = true
  emit('update:modelValue', val.map(item => ({
    rawMaterialId: item.rawMaterialId ? Number(item.rawMaterialId) : null,
    requiredQuantity: item.requiredQuantity ? Number(item.requiredQuantity) : null
  })))
  nextTick(() => { isEmitting.value = false })
}, { deep: true })

function availableMaterials(currentIndex) {
  const usedIds = items.value
    .filter((_, i) => i !== currentIndex)
    .map(item => String(item.rawMaterialId))

  return rawMaterialStore.items
    .filter(m => !usedIds.includes(String(m.id)))
    .map(m => ({ value: String(m.id), label: `${m.code} - ${m.name}` }))
}

function getUnitForMaterial(rawMaterialId) {
  const mat = rawMaterialStore.items.find(m => String(m.id) === String(rawMaterialId))
  return mat ? mat.unit : ''
}

function addItem() {
  items.value.push({ rawMaterialId: '', requiredQuantity: '' })
}

function removeItem(index) {
  items.value.splice(index, 1)
}
</script>

<style scoped>
.composition-editor {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
}

.editor-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.empty-state {
  padding: var(--spacing-md);
  text-align: center;
  border: 1px dashed var(--color-border);
  border-radius: var(--radius);
}

.composition-row {
  display: grid;
  grid-template-columns: 2fr 1fr auto auto;
  gap: var(--spacing-sm);
  align-items: end;
}

.unit-display {
  padding: 8px 0;
  font-size: 13px;
  color: var(--color-text-secondary);
  min-width: 30px;
  display: flex;
  align-items: flex-end;
}

.unit-display.with-label {
  padding-top: 24px;
}

.unit-label-spacer {
  display: block;
}

.remove-btn {
  width: 32px;
  height: 36px;
  border: none;
  border-radius: var(--radius-sm);
  background-color: var(--color-danger-light);
  color: var(--color-danger);
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--transition-fast);
}

.remove-btn.with-label {
  margin-top: 18px;
}

.remove-btn:hover {
  background-color: var(--color-danger);
  color: #FFFFFF;
}
</style>
