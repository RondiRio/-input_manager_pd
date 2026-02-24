<template>
  <!-- PT-BR: Secao sobre o desenvolvedor Rondineli Oliveira.
              Card centralizado com avatar, nome, funcao e link para netonerd.com. -->
  <!-- EN-US: Section about developer Rondineli Oliveira.
              Centered card with avatar, name, role, and link to netonerd.com. -->
  <section class="developer">
    <div class="section-container">
      <div class="section-header reveal">
        <h2 class="section-title">{{ $t('landing.developer.sectionTitle') }}</h2>
      </div>

      <div class="developer-card reveal">
        <div class="developer-avatar">RO</div>
        <h3 class="developer-name">{{ $t('landing.developer.name') }}</h3>
        <p class="developer-role">{{ $t('landing.developer.role') }}</p>
        <p class="developer-description">{{ $t('landing.developer.description') }}</p>
        <a
          href="https://netonerd.com"
          target="_blank"
          rel="noopener noreferrer"
          class="developer-link"
        >
          {{ $t('landing.developer.visitSite') }}
          <span class="link-arrow">&#8599;</span>
        </a>
      </div>
    </div>
  </section>
</template>

<script setup>
</script>

<style scoped>
.developer {
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
  margin-bottom: var(--spacing-xl);
}

.section-title {
  font-size: clamp(1.5rem, 3vw, 2.25rem);
  font-weight: 800;
  color: var(--color-identity);
}

.developer-card {
  max-width: 520px;
  margin: 0 auto;
  text-align: center;
  background: var(--color-bg-card);
  border-radius: var(--radius-lg);
  padding: var(--spacing-2xl) var(--spacing-xl);
  box-shadow: var(--shadow-md);
  border: 1px solid var(--color-border);
}

.developer-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: var(--color-identity);
  color: #FFFFFF;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  font-weight: 700;
  margin-bottom: var(--spacing-lg);
  letter-spacing: 0.02em;
}

.developer-name {
  font-size: 24px;
  font-weight: 800;
  color: var(--color-text-primary);
  margin-bottom: var(--spacing-xs);
}

.developer-role {
  font-size: 15px;
  color: var(--color-action);
  font-weight: 600;
  margin-bottom: var(--spacing-lg);
}

.developer-description {
  font-size: 15px;
  color: var(--color-text-secondary);
  line-height: 1.65;
  margin-bottom: var(--spacing-xl);
}

.developer-link {
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-sm);
  padding: 12px 28px;
  border: 2px solid var(--color-action);
  color: var(--color-action);
  border-radius: var(--radius);
  font-size: 15px;
  font-weight: 600;
  text-decoration: none;
  transition: all var(--transition-fast);
}

.developer-link:hover {
  background: var(--color-action);
  color: #FFFFFF;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 102, 255, 0.25);
}

.link-arrow {
  font-size: 18px;
  transition: transform var(--transition-fast);
}

.developer-link:hover .link-arrow {
  transform: translate(2px, -2px);
}

@media (max-width: 640px) {
  .developer {
    padding: 64px 0;
  }

  .developer-card {
    padding: var(--spacing-xl) var(--spacing-lg);
  }
}
</style>
