<template>
  <!-- PT-BR: Barra de navegacao fixa da landing page com logo, links ancora,
              seletor de idioma e botao CTA. Responsiva com hamburger no mobile. -->
  <!-- EN-US: Fixed landing page navigation bar with logo, anchor links,
              language selector, and CTA button. Responsive with hamburger on mobile. -->
  <header class="navbar" :class="{ scrolled: isScrolled }">
    <div class="navbar-container">
      <div class="navbar-brand">
        <span class="brand-icon">IM</span>
        <span class="brand-text">Input Manager</span>
      </div>

      <button class="hamburger" :class="{ open: menuOpen }" @click="menuOpen = !menuOpen">
        <span></span>
        <span></span>
        <span></span>
      </button>

      <nav class="navbar-nav" :class="{ open: menuOpen }">
        <a href="#features" class="nav-link" @click="closeMenu">{{ $t('landing.nav.features') }}</a>
        <a href="#tech" class="nav-link" @click="closeMenu">{{ $t('landing.nav.techStack') }}</a>
        <a href="#about" class="nav-link" @click="closeMenu">{{ $t('landing.nav.about') }}</a>

        <div class="nav-divider"></div>

        <select class="lang-select" :value="locale" @change="changeLocale($event.target.value)">
          <option value="pt-BR">PT-BR</option>
          <option value="en-US">EN-US</option>
        </select>

        <RouterLink to="/dashboard" class="nav-cta" @click="closeMenu">
          {{ $t('landing.nav.enterApp') }}
        </RouterLink>
      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useI18n } from 'vue-i18n'

const { locale } = useI18n()
const menuOpen = ref(false)
const isScrolled = ref(false)

function changeLocale(newLocale) {
  locale.value = newLocale
  localStorage.setItem('locale', newLocale)
}

function closeMenu() {
  menuOpen.value = false
}

function handleScroll() {
  isScrolled.value = window.scrollY > 20
}

onMounted(() => window.addEventListener('scroll', handleScroll))
onUnmounted(() => window.removeEventListener('scroll', handleScroll))
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  background: transparent;
  transition: all var(--transition-normal);
  padding: var(--spacing-md) 0;
}

.navbar.scrolled {
  background: var(--color-bg-card);
  box-shadow: var(--shadow-sm);
  padding: var(--spacing-sm) 0;
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 var(--spacing-lg);
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.navbar-brand {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  text-decoration: none;
}

.brand-icon {
  width: 36px;
  height: 36px;
  background: var(--color-action);
  border-radius: var(--radius);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #FFFFFF;
  font-size: 14px;
  font-weight: 700;
}

.brand-text {
  font-size: 18px;
  font-weight: 700;
  color: #FFFFFF;
  transition: color var(--transition-normal);
}

.scrolled .brand-text {
  color: var(--color-identity);
}

.navbar-nav {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.nav-link {
  color: rgba(255, 255, 255, 0.85);
  text-decoration: none;
  font-size: 14px;
  font-weight: 500;
  transition: color var(--transition-fast);
}

.nav-link:hover {
  color: #FFFFFF;
}

.scrolled .nav-link {
  color: var(--color-text-secondary);
}

.scrolled .nav-link:hover {
  color: var(--color-action);
}

.nav-divider {
  width: 1px;
  height: 20px;
  background: rgba(255, 255, 255, 0.3);
}

.scrolled .nav-divider {
  background: var(--color-border);
}

.lang-select {
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.3);
  color: rgba(255, 255, 255, 0.85);
  padding: 4px 8px;
  border-radius: var(--radius-sm);
  font-size: 12px;
  font-weight: 600;
  cursor: pointer;
}

.scrolled .lang-select {
  border-color: var(--color-border);
  color: var(--color-text-secondary);
}

.lang-select option {
  background: var(--color-bg-card);
  color: var(--color-text-primary);
}

.nav-cta {
  background: #FFFFFF;
  color: var(--color-identity);
  padding: var(--spacing-sm) var(--spacing-lg);
  border-radius: var(--radius);
  font-size: 14px;
  font-weight: 600;
  text-decoration: none;
  transition: all var(--transition-fast);
}

.nav-cta:hover {
  background: var(--color-action-light);
  color: var(--color-action);
}

.scrolled .nav-cta {
  background: var(--color-action);
  color: #FFFFFF;
}

.scrolled .nav-cta:hover {
  background: var(--color-action-hover);
}

.hamburger {
  display: none;
  flex-direction: column;
  gap: 5px;
  background: none;
  border: none;
  cursor: pointer;
  padding: var(--spacing-sm);
}

.hamburger span {
  width: 24px;
  height: 2px;
  background: #FFFFFF;
  transition: all var(--transition-fast);
  border-radius: 2px;
}

.scrolled .hamburger span {
  background: var(--color-identity);
}

.hamburger.open span:nth-child(1) {
  transform: rotate(45deg) translate(5px, 5px);
}

.hamburger.open span:nth-child(2) {
  opacity: 0;
}

.hamburger.open span:nth-child(3) {
  transform: rotate(-45deg) translate(5px, -5px);
}

@media (max-width: 768px) {
  .hamburger {
    display: flex;
  }

  .navbar-nav {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: var(--color-identity);
    flex-direction: column;
    justify-content: center;
    gap: var(--spacing-xl);
    opacity: 0;
    pointer-events: none;
    transition: opacity var(--transition-normal);
    z-index: -1;
  }

  .navbar-nav.open {
    opacity: 1;
    pointer-events: all;
  }

  .navbar-nav .nav-link {
    font-size: 20px;
    color: rgba(255, 255, 255, 0.85);
  }

  .navbar-nav .nav-link:hover {
    color: #FFFFFF;
  }

  .nav-divider {
    width: 40px;
    height: 1px;
  }

  .lang-select {
    color: rgba(255, 255, 255, 0.85);
    border-color: rgba(255, 255, 255, 0.3);
    font-size: 14px;
  }

  .nav-cta {
    font-size: 16px;
  }
}
</style>
