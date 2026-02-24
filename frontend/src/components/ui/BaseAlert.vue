<template>
  <Transition name="alert">
    <div v-if="visible" class="alert" :class="`alert-${type}`">
      <span class="alert-icon">{{ iconMap[type] }}</span>
      <p class="alert-message">{{ message }}</p>
      <button v-if="dismissible" class="alert-close" @click="$emit('dismiss')">&times;</button>
    </div>
  </Transition>
</template>

<script setup>
// PT-BR: Componente de alerta para feedback ao usuario. Segue as diretrizes de design:
//        verde para sucesso (entrada de insumo), vermelho apenas para alertas criticos
//        (estoque critico). Nunca usamos essas cores em botoes comuns.
// EN-US: Alert component for user feedback. Follows the design guidelines:
//        green for success (input entry), red only for critical alerts
//        (critical stock). We never use these colors on regular buttons.
defineProps({
  type: { type: String, default: 'info', validator: v => ['success', 'error', 'warning', 'info'].includes(v) },
  message: { type: String, required: true },
  dismissible: { type: Boolean, default: true },
  visible: { type: Boolean, default: true }
})

defineEmits(['dismiss'])

const iconMap = {
  success: '!',
  error: '!',
  warning: '!',
  info: 'i'
}
</script>

<style scoped>
.alert {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  padding: var(--spacing-md) var(--spacing-lg);
  border-radius: var(--radius);
  font-size: 14px;
  margin-bottom: var(--spacing-md);
}

.alert-success {
  background-color: var(--color-success-light);
  color: #065F46;
  border: 1px solid #A7F3D0;
}

.alert-error {
  background-color: var(--color-danger-light);
  color: #991B1B;
  border: 1px solid #FECACA;
}

.alert-warning {
  background-color: var(--color-warning-light);
  color: #92400E;
  border: 1px solid #FDE68A;
}

.alert-info {
  background-color: var(--color-action-light);
  color: #1E40AF;
  border: 1px solid #BFDBFE;
}

.alert-icon {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 12px;
  flex-shrink: 0;
}

.alert-success .alert-icon { background-color: var(--color-success); color: #FFFFFF; }
.alert-error .alert-icon { background-color: var(--color-danger); color: #FFFFFF; }
.alert-warning .alert-icon { background-color: var(--color-warning); color: #FFFFFF; }
.alert-info .alert-icon { background-color: var(--color-action); color: #FFFFFF; }

.alert-message {
  flex: 1;
}

.alert-close {
  background: none;
  border: none;
  font-size: 18px;
  opacity: 0.6;
  padding: 0;
  line-height: 1;
  color: inherit;
  transition: opacity var(--transition-fast);
}

.alert-close:hover {
  opacity: 1;
}

.alert-enter-active,
.alert-leave-active {
  transition: all var(--transition-normal);
}

.alert-enter-from,
.alert-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
