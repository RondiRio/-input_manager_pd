<template>
  <!-- PT-BR: Footer da landing page com identidade visual escura (azul identidade).
              Tres colunas: marca, links e contato. Barra de copyright. -->
  <!-- EN-US: Landing page footer with dark visual identity (identity blue).
              Three columns: brand, links, and contact. Copyright bar. -->
  <footer class="footer">
    <div class="footer-container">
      <div class="footer-grid">
        <div class="footer-brand">
          <div class="footer-logo">
            <span class="footer-logo-icon">IM</span>
            <span class="footer-logo-text">Input Manager</span>
          </div>
          <p class="footer-description">{{ $t('landing.footer.description') }}</p>
        </div>

        <div class="footer-links">
          <h4 class="footer-heading">{{ $t('landing.footer.links') }}</h4>
          <a href="#features" class="footer-link">{{ $t('landing.nav.features') }}</a>
          <a href="#tech" class="footer-link">{{ $t('landing.nav.techStack') }}</a>
          <a href="#about" class="footer-link">{{ $t('landing.nav.about') }}</a>
          <RouterLink to="/dashboard" class="footer-link">{{ $t('landing.nav.enterApp') }}</RouterLink>
        </div>

        <div class="footer-contact">
          <h4 class="footer-heading">{{ $t('landing.footer.contact') }}</h4>
          <a href="https://netonerd.com" target="_blank" rel="noopener noreferrer" class="footer-link">
            netonerd.com
          </a>
        </div>
      </div>

      <div class="footer-bottom">
        <p>&copy; {{ currentYear }} Rondineli Oliveira. {{ $t('landing.footer.copyright') }}</p>
      </div>
    </div>
  </footer>
</template>

<script setup>
const currentYear = new Date().getFullYear()
</script>

<style scoped>
.footer {
  background: var(--color-identity);
  color: var(--color-identity-text);
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--spacing-lg);
}

.footer-grid {
  display: grid;
  grid-template-columns: 2fr 1fr 1fr;
  gap: var(--spacing-2xl);
  padding: var(--spacing-2xl) 0;
}

.footer-logo {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-md);
}

.footer-logo-icon {
  width: 32px;
  height: 32px;
  background: var(--color-action);
  border-radius: var(--radius);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  color: #FFFFFF;
}

.footer-logo-text {
  font-size: 16px;
  font-weight: 700;
}

.footer-description {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.6);
  line-height: 1.6;
  max-width: 320px;
}

.footer-heading {
  font-size: 13px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: rgba(255, 255, 255, 0.5);
  margin-bottom: var(--spacing-md);
}

.footer-links,
.footer-contact {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}

.footer-link {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  font-size: 14px;
  transition: color var(--transition-fast);
}

.footer-link:hover {
  color: #FFFFFF;
}

.footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  padding: var(--spacing-lg) 0;
  text-align: center;
}

.footer-bottom p {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.4);
}

@media (max-width: 768px) {
  .footer-grid {
    grid-template-columns: 1fr;
    gap: var(--spacing-xl);
    padding: var(--spacing-xl) 0;
  }
}
</style>
