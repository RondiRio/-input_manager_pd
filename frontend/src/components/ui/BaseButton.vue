<template>
  <button
    class="btn"
    :class="[`btn-${variant}`, `btn-${size}`, { 'btn-loading': loading }]"
    :disabled="disabled || loading"
    @click="$emit('click', $event)"
  >
    <span v-if="loading" class="btn-spinner"></span>
    <slot />
  </button>
</template>

<script setup>
// PT-BR: Botao base reutilizavel com variantes de estilo e estado de carregamento.
//        O botao primario usa a cor de acao (#0066FF) para guiar o olho do usuario.
// EN-US: Reusable base button with style variants and loading state.
//        The primary button uses the action color (#0066FF) to guide the user's eye.
defineProps({
  variant: { type: String, default: 'primary', validator: v => ['primary', 'secondary', 'danger', 'ghost'].includes(v) },
  size: { type: String, default: 'md', validator: v => ['sm', 'md', 'lg'].includes(v) },
  loading: { type: Boolean, default: false },
  disabled: { type: Boolean, default: false }
})

defineEmits(['click'])
</script>

<style scoped>
.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
  border: none;
  border-radius: var(--radius);
  font-weight: 500;
  transition: all var(--transition-fast);
  white-space: nowrap;
}

.btn:disabled { opacity: 0.6; cursor: not-allowed; }

.btn-sm { padding: 6px 12px; font-size: 13px; }
.btn-md { padding: 8px 16px; font-size: 14px; }
.btn-lg { padding: 12px 24px; font-size: 16px; }

.btn-primary {
  background-color: var(--color-action);
  color: #FFFFFF;
}
.btn-primary:hover:not(:disabled) {
  background-color: var(--color-action-hover);
}

.btn-secondary {
  background-color: var(--color-bg-primary);
  color: var(--color-text-primary);
  border: 1px solid var(--color-border);
}
.btn-secondary:hover:not(:disabled) {
  background-color: var(--color-bg-hover);
}

.btn-danger {
  background-color: var(--color-danger);
  color: #FFFFFF;
}
.btn-danger:hover:not(:disabled) {
  background-color: #DC2626;
}

.btn-ghost {
  background: none;
  color: var(--color-text-secondary);
}
.btn-ghost:hover:not(:disabled) {
  background-color: var(--color-bg-hover);
  color: var(--color-text-primary);
}

.btn-loading { cursor: wait; }

.btn-spinner {
  width: 14px;
  height: 14px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: #FFFFFF;
  border-radius: 50%;
  animation: spin 0.6s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}
</style>
