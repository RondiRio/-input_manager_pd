<template>
  <div class="card">
    <div v-if="title" class="card-header">
      <h3 class="card-title">{{ title }}</h3>
      <div class="card-actions">
        <slot name="actions" />
      </div>
    </div>
    <div class="card-body" :class="{ 'no-padding': noPadding }">
      <slot />
    </div>
  </div>
</template>

<script setup>
// PT-BR: Cartao base com fundo branco sobre o cinza gelo, seguindo o design
//        de dashboard minimalista. Bordas arredondadas de 8px para o aspecto SaaS moderno.
// EN-US: Base card with white background over ice gray, following the minimalist
//        dashboard design. 8px rounded borders for the modern SaaS look.
defineProps({
  title: { type: String, default: '' },
  noPadding: { type: Boolean, default: false }
})
</script>

<style scoped>
.card {
  background-color: var(--color-bg-card);
  border-radius: var(--radius);
  box-shadow: var(--shadow-sm);
  border: 1px solid var(--color-border);
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-md) var(--spacing-lg);
  border-bottom: 1px solid var(--color-border);
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.card-actions {
  display: flex;
  gap: var(--spacing-sm);
}

.card-body {
  padding: var(--spacing-lg);
}

.card-body.no-padding {
  padding: 0;
}
</style>
