import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import i18n from './i18n'
import App from './App.vue'
import './assets/styles/main.css'

// PT-BR: Ponto de entrada da aplicacao. Montamos o Vue com Pinia (gerenciamento de
//        estado), Vue Router (navegacao) e Vue I18n (internacionalizacao).
// EN-US: Application entry point. We set up Vue with Pinia (state management),
//        Vue Router (navigation), and Vue I18n (internationalization).
const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(i18n)
app.mount('#app')
