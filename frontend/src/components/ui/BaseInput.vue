<template>
  <div class="form-group">
    <label v-if="label" class="form-label" :for="inputId">
      {{ label }}
      <span v-if="required" class="required-mark">*</span>
    </label>

    <select
      v-if="type === 'select'"
      :id="inputId"
      class="form-control"
      :class="{ 'has-error': error }"
      :value="modelValue"
      :disabled="disabled"
      @change="$emit('update:modelValue', $event.target.value)"
    >
      <option v-if="placeholder" value="" disabled>{{ placeholder }}</option>
      <option
        v-for="opt in options"
        :key="opt.value"
        :value="opt.value"
      >
        {{ opt.label }}
      </option>
    </select>

    <textarea
      v-else-if="type === 'textarea'"
      :id="inputId"
      class="form-control"
      :class="{ 'has-error': error }"
      :value="modelValue"
      :placeholder="placeholder"
      :disabled="disabled"
      :rows="rows"
      @input="$emit('update:modelValue', $event.target.value)"
    ></textarea>

    <input
      v-else
      :id="inputId"
      class="form-control"
      :class="{ 'has-error': error }"
      :type="type"
      :value="modelValue"
      :placeholder="placeholder"
      :disabled="disabled"
      :step="step"
      :min="min"
      @input="$emit('update:modelValue', $event.target.value)"
    />

    <p v-if="error" class="form-error">{{ error }}</p>
  </div>
</template>

<script setup>
// PT-BR: Campo de formulario reutilizavel que suporta input, select e textarea.
//        Inclui label, validacao visual de erro e marca de campo obrigatorio.
//        Usa v-model via modelValue/update:modelValue para two-way binding.
// EN-US: Reusable form field supporting input, select, and textarea.
//        Includes label, visual error validation, and required field mark.
//        Uses v-model via modelValue/update:modelValue for two-way binding.
import { computed } from 'vue'

const props = defineProps({
  modelValue: { type: [String, Number], default: '' },
  label: { type: String, default: '' },
  type: { type: String, default: 'text' },
  placeholder: { type: String, default: '' },
  error: { type: String, default: '' },
  required: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false },
  options: { type: Array, default: () => [] },
  step: { type: String, default: undefined },
  min: { type: String, default: undefined },
  rows: { type: Number, default: 3 }
})

defineEmits(['update:modelValue'])

const inputId = computed(() => 'input-' + Math.random().toString(36).substring(2, 9))
</script>

<style scoped>
.form-group {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
}

.form-label {
  font-size: 13px;
  font-weight: 500;
  color: var(--color-text-primary);
}

.required-mark {
  color: var(--color-danger);
}

.form-control {
  padding: 8px 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  font-size: 14px;
  color: var(--color-text-primary);
  background-color: var(--color-bg-card);
  outline: none;
  transition: border-color var(--transition-fast), box-shadow var(--transition-fast);
  width: 100%;
}

.form-control:focus {
  border-color: var(--color-border-focus);
  box-shadow: 0 0 0 3px var(--color-action-light);
}

.form-control:disabled {
  background-color: var(--color-bg-primary);
  cursor: not-allowed;
  opacity: 0.7;
}

.form-control.has-error {
  border-color: var(--color-danger);
}

.form-control.has-error:focus {
  box-shadow: 0 0 0 3px var(--color-danger-light);
}

select.form-control {
  cursor: pointer;
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%2364748B' d='M6 8L1 3h10z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 12px center;
  padding-right: 32px;
}

textarea.form-control {
  resize: vertical;
  min-height: 80px;
}

.form-error {
  font-size: 12px;
  color: var(--color-danger);
}
</style>
