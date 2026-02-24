<template>
  <!-- PT-BR: Secao de funcionalidades com 4 cards em grid responsivo.
              Cada card destaca uma capacidade principal do sistema. -->
  <!-- EN-US: Features section with 4 cards in a responsive grid.
              Each card highlights a key system capability. -->
  <section class="features">
    <div class="section-container">
      <div class="section-header reveal">
        <h2 class="section-title">{{ $t('landing.features.sectionTitle') }}</h2>
        <p class="section-subtitle">{{ $t('landing.features.sectionSubtitle') }}</p>
      </div>

      <div class="features-grid">
        <div
          v-for="(feature, index) in features"
          :key="feature.key"
          class="feature-card reveal"
          :style="{ transitionDelay: `${index * 0.1}s` }"
        >
          <div class="feature-icon" v-html="feature.icon"></div>
          <h3 class="feature-title">{{ $t(`landing.features.${feature.key}.title`) }}</h3>
          <p class="feature-description">{{ $t(`landing.features.${feature.key}.description`) }}</p>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
// PT-BR: Dados das funcionalidades com icones Unicode para consistencia
//        com o restante do design system (sem biblioteca externa de icones).
// EN-US: Feature data with Unicode icons for consistency with the rest
//        of the design system (no external icon library).
const features = [
  { key: 'stockManagement', icon: '&#128230;' },
  { key: 'productionOptimization', icon: '&#9733;' },
  { key: 'compositionManagement', icon: '&#9776;' },
  { key: 'realtimeDashboard', icon: '&#9632;' }
]
</script>

<style scoped>
.features {
  padding: 96px 0;
  background: var(--color-bg-primary);
}

.section-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--spacing-lg);
}

.section-header {
  text-align: center;
  margin-bottom: var(--spacing-2xl);
}

.section-title {
  font-size: clamp(1.5rem, 3vw, 2.25rem);
  font-weight: 800;
  color: var(--color-identity);
  margin-bottom: var(--spacing-md);
}

.section-subtitle {
  font-size: 1.05rem;
  color: var(--color-text-secondary);
  max-width: 560px;
  margin: 0 auto;
  line-height: 1.6;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--spacing-lg);
}

.feature-card {
  background: var(--color-bg-card);
  border-radius: var(--radius-lg);
  padding: var(--spacing-xl);
  border: 1px solid var(--color-border);
  transition: all var(--transition-normal);
  cursor: default;
}

.feature-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-lg);
  border-color: var(--color-action);
}

.feature-icon {
  width: 52px;
  height: 52px;
  background: var(--color-action-light);
  border-radius: var(--radius);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-bottom: var(--spacing-md);
  color: var(--color-action);
}

.feature-title {
  font-size: 17px;
  font-weight: 700;
  color: var(--color-text-primary);
  margin-bottom: var(--spacing-sm);
}

.feature-description {
  font-size: 14px;
  color: var(--color-text-secondary);
  line-height: 1.65;
}

@media (max-width: 1024px) {
  .features-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .features {
    padding: 64px 0;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }
}
</style>
