<template>
  <!-- PT-BR: Secao de tecnologias utilizadas, agrupadas por categoria.
              Mostra as tecnologias do backend, frontend, banco de dados e ferramentas. -->
  <!-- EN-US: Tech stack section, grouped by category.
              Shows backend, frontend, database, and tools technologies. -->
  <section class="tech-stack">
    <div class="section-container">
      <div class="section-header reveal">
        <h2 class="section-title">{{ $t('landing.tech.sectionTitle') }}</h2>
        <p class="section-subtitle">{{ $t('landing.tech.sectionSubtitle') }}</p>
      </div>

      <div class="tech-grid">
        <div
          v-for="(category, index) in categories"
          :key="category.key"
          class="tech-category reveal"
          :style="{ transitionDelay: `${index * 0.1}s` }"
        >
          <div class="category-header">
            <span class="category-icon" v-html="category.icon"></span>
            <h3 class="category-title">{{ $t(`landing.tech.${category.key}`) }}</h3>
          </div>

          <div class="tech-list">
            <div
              v-for="tech in category.items"
              :key="tech.name"
              class="tech-badge"
            >
              <span class="tech-name">{{ tech.name }}</span>
              <span class="tech-desc">{{ tech.desc }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
// PT-BR: Tecnologias organizadas por categoria com nome e descricao curta.
// EN-US: Technologies organized by category with name and short description.
const categories = [
  {
    key: 'backend',
    icon: '&#9881;',
    items: [
      { name: 'Java 17', desc: 'LTS' },
      { name: 'Quarkus 3.17', desc: 'Supersonic' },
      { name: 'Hibernate Panache', desc: 'ORM' },
      { name: 'SmallRye JWT', desc: 'Auth' }
    ]
  },
  {
    key: 'frontend',
    icon: '&#9674;',
    items: [
      { name: 'Vue.js 3', desc: 'Composition API' },
      { name: 'Vite 6', desc: 'Build Tool' },
      { name: 'Pinia', desc: 'State Mgmt' },
      { name: 'Vue I18n', desc: 'PT / EN' }
    ]
  },
  {
    key: 'database',
    icon: '&#9744;',
    items: [
      { name: 'H2', desc: 'Development' },
      { name: 'PostgreSQL', desc: 'Production' }
    ]
  },
  {
    key: 'tools',
    icon: '&#9998;',
    items: [
      { name: 'Vitest', desc: 'Frontend Tests' },
      { name: 'REST Assured', desc: 'API Tests' },
      { name: 'Maven', desc: 'Build' },
      { name: 'JUnit 5', desc: 'Unit Tests' }
    ]
  }
]
</script>

<style scoped>
.tech-stack {
  padding: 96px 0;
  background: var(--color-bg-card);
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

.tech-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--spacing-xl);
}

.tech-category {
  background: var(--color-bg-primary);
  border-radius: var(--radius-lg);
  padding: var(--spacing-xl);
  border: 1px solid var(--color-border);
}

.category-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-lg);
}

.category-icon {
  width: 36px;
  height: 36px;
  background: var(--color-action-light);
  border-radius: var(--radius);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: var(--color-action);
}

.category-title {
  font-size: 15px;
  font-weight: 700;
  color: var(--color-identity);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.tech-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.tech-badge {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: var(--color-bg-card);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: var(--spacing-sm) var(--spacing-md);
  transition: all var(--transition-fast);
}

.tech-badge:hover {
  border-color: var(--color-action);
  box-shadow: var(--shadow-sm);
}

.tech-name {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text-primary);
}

.tech-desc {
  font-size: 12px;
  color: var(--color-text-muted);
  font-weight: 500;
}

@media (max-width: 768px) {
  .tech-stack {
    padding: 64px 0;
  }

  .tech-grid {
    grid-template-columns: 1fr;
  }
}
</style>
