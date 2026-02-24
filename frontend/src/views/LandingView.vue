<template>
  <!-- PT-BR: Landing page principal do Input Manager PD.
              Compoe todas as secoes da pagina: navbar, hero, funcionalidades,
              tecnologias, desenvolvedor e footer. Usa animacoes de scroll
              via IntersectionObserver para uma experiencia visual profissional. -->
  <!-- EN-US: Main landing page for Input Manager PD.
              Composes all page sections: navbar, hero, features, tech stack,
              developer, and footer. Uses scroll animations via IntersectionObserver
              for a professional visual experience. -->
  <div class="landing-page">
    <LandingNavbar />
    <HeroSection />
    <FeaturesSection id="features" />
    <TechStackSection id="tech" />
    <DeveloperSection id="about" />
    <LandingFooter />
  </div>
</template>

<script setup>
import { useScrollReveal } from '@/composables/useScrollReveal'
import LandingNavbar from '@/components/landing/LandingNavbar.vue'
import HeroSection from '@/components/landing/HeroSection.vue'
import FeaturesSection from '@/components/landing/FeaturesSection.vue'
import TechStackSection from '@/components/landing/TechStackSection.vue'
import DeveloperSection from '@/components/landing/DeveloperSection.vue'
import LandingFooter from '@/components/landing/LandingFooter.vue'

// PT-BR: Ativa as animacoes de scroll para todos os elementos com classe 'reveal'.
// EN-US: Activates scroll animations for all elements with the 'reveal' class.
useScrollReveal('.reveal')
</script>

<style scoped>
.landing-page {
  min-height: 100vh;
  background-color: var(--color-bg-primary);
  overflow-x: hidden;
}

/* PT-BR: Classes de animacao de scroll — 'reveal' define o estado inicial (invisivel)
          e 'revealed' define o estado final (visivel). A transicao e suave em 0.6s. */
/* EN-US: Scroll animation classes — 'reveal' defines the initial state (invisible)
          and 'revealed' defines the final state (visible). Smooth 0.6s transition. */
:deep(.reveal) {
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.6s ease, transform 0.6s ease;
}

:deep(.reveal.revealed) {
  opacity: 1;
  transform: translateY(0);
}
</style>
