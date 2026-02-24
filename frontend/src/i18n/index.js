import { createI18n } from 'vue-i18n'
import ptBR from './locales/pt-BR.json'
import enUS from './locales/en-US.json'

// PT-BR: Configuracao de internacionalizacao. Portugues brasileiro e o idioma padrao,
//        com ingles americano como fallback. O usuario pode trocar a qualquer momento
//        pelo seletor de idioma no cabecalho.
// EN-US: Internationalization configuration. Brazilian Portuguese is the default language,
//        with American English as fallback. The user can switch at any time via the
//        language selector in the header.
const i18n = createI18n({
  legacy: false,
  locale: localStorage.getItem('locale') || 'pt-BR',
  fallbackLocale: 'en-US',
  messages: {
    'pt-BR': ptBR,
    'en-US': enUS
  }
})

export default i18n
