import { onMounted, onUnmounted } from 'vue'

// PT-BR: Composable para animacoes de scroll usando IntersectionObserver.
//        Elementos com a classe 'reveal' comecam invisiveis e aparecem suavemente
//        quando entram no viewport. Zero dependencias externas.
// EN-US: Composable for scroll animations using IntersectionObserver.
//        Elements with the 'reveal' class start invisible and smoothly appear
//        when they enter the viewport. Zero external dependencies.
export function useScrollReveal(selector = '.reveal', options = {}) {
  let observer = null

  onMounted(() => {
    observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting) {
          entry.target.classList.add('revealed')
          observer.unobserve(entry.target)
        }
      })
    }, {
      threshold: options.threshold || 0.15,
      rootMargin: options.rootMargin || '0px'
    })

    document.querySelectorAll(selector).forEach(el => observer.observe(el))
  })

  onUnmounted(() => {
    if (observer) observer.disconnect()
  })
}
