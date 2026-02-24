<template>
  <!-- PT-BR: Secao hero da landing page com gradiente de identidade visual.
              Headline em destaque + subtitulo + dois botoes de acao. -->
  <!-- EN-US: Landing page hero section with visual identity gradient.
              Bold headline + subtitle + two action buttons. -->
  <section class="hero">
    <div class="hero-bg-decoration">
      <div class="decoration-circle decoration-circle-1"></div>
      <div class="decoration-circle decoration-circle-2"></div>
      <div class="decoration-circle decoration-circle-3"></div>
    </div>

    <div class="hero-content">
      <h1 class="hero-headline">{{ $t('landing.hero.headline') }}</h1>
      <p class="hero-subtitle">{{ $t('landing.hero.subtitle') }}</p>

      <div class="hero-actions">
        <RouterLink to="/dashboard" class="hero-btn hero-btn-primary">
          {{ $t('landing.hero.cta') }}
          <span class="btn-arrow">&#8594;</span>
        </RouterLink>
        <a href="#features" class="hero-btn hero-btn-secondary">
          {{ $t('landing.hero.secondaryCta') }}
        </a>
      </div>

      <div class="hero-stats">
        <div class="stat-item">
          <span class="stat-value">B&B</span>
          <span class="stat-label">Algorithm</span>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <span class="stat-value">REST</span>
          <span class="stat-label">API</span>
        </div>
        <div class="stat-divider"></div>
        <div class="stat-item">
          <span class="stat-value">i18n</span>
          <span class="stat-label">PT / EN</span>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
</script>

<style scoped>
.hero {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #002D54 0%, #003D72 50%, #0052CC 100%);
  position: relative;
  overflow: hidden;
  padding: var(--spacing-2xl) var(--spacing-lg);
}

.hero-bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(0, 102, 255, 0.08);
}

.decoration-circle-1 {
  width: 600px;
  height: 600px;
  top: -200px;
  right: -100px;
}

.decoration-circle-2 {
  width: 400px;
  height: 400px;
  bottom: -100px;
  left: -80px;
}

.decoration-circle-3 {
  width: 200px;
  height: 200px;
  top: 50%;
  left: 60%;
  background: rgba(255, 255, 255, 0.03);
}

.hero-content {
  max-width: 720px;
  text-align: center;
  position: relative;
  z-index: 1;
}

.hero-headline {
  font-size: clamp(2rem, 5vw, 3.5rem);
  font-weight: 800;
  color: #FFFFFF;
  line-height: 1.15;
  margin-bottom: var(--spacing-lg);
  letter-spacing: -0.02em;
}

.hero-subtitle {
  font-size: clamp(1rem, 2vw, 1.2rem);
  color: rgba(255, 255, 255, 0.8);
  line-height: 1.7;
  margin-bottom: var(--spacing-xl);
  max-width: 600px;
  margin-left: auto;
  margin-right: auto;
}

.hero-actions {
  display: flex;
  gap: var(--spacing-md);
  justify-content: center;
  flex-wrap: wrap;
  margin-bottom: var(--spacing-2xl);
}

.hero-btn {
  padding: 14px 32px;
  border-radius: var(--radius);
  font-size: 16px;
  font-weight: 600;
  text-decoration: none;
  transition: all var(--transition-fast);
  display: inline-flex;
  align-items: center;
  gap: var(--spacing-sm);
}

.hero-btn-primary {
  background: #FFFFFF;
  color: var(--color-identity);
  box-shadow: 0 4px 14px rgba(0, 0, 0, 0.15);
}

.hero-btn-primary:hover {
  background: var(--color-action-light);
  color: var(--color-action);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
}

.btn-arrow {
  transition: transform var(--transition-fast);
  font-size: 18px;
}

.hero-btn-primary:hover .btn-arrow {
  transform: translateX(4px);
}

.hero-btn-secondary {
  background: transparent;
  color: #FFFFFF;
  border: 2px solid rgba(255, 255, 255, 0.4);
}

.hero-btn-secondary:hover {
  border-color: #FFFFFF;
  background: rgba(255, 255, 255, 0.1);
  transform: translateY(-2px);
}

.hero-stats {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-xl);
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 20px;
  font-weight: 700;
  color: #FFFFFF;
  letter-spacing: 0.05em;
}

.stat-label {
  display: block;
  font-size: 12px;
  color: rgba(255, 255, 255, 0.5);
  text-transform: uppercase;
  letter-spacing: 0.1em;
  margin-top: 2px;
}

.stat-divider {
  width: 1px;
  height: 32px;
  background: rgba(255, 255, 255, 0.2);
}

@media (max-width: 640px) {
  .hero {
    padding-top: 100px;
  }

  .hero-actions {
    flex-direction: column;
    align-items: center;
  }

  .hero-btn {
    width: 100%;
    max-width: 280px;
    justify-content: center;
  }

  .hero-stats {
    gap: var(--spacing-lg);
  }

  .stat-value {
    font-size: 16px;
  }
}
</style>
