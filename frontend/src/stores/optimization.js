import { defineStore } from 'pinia'
import { ref } from 'vue'
import optimizationApi from '@/api/optimizationApi'

// PT-BR: Store de otimizacao. Guarda o resultado da ultima otimizacao executada.
//        O resultado inclui o plano de producao, uso de materiais e a receita total.
//        Enquanto o algoritmo roda, mostramos um indicador de carregamento para que
//        o usuario saiba que algo bom esta sendo calculado nos bastidores.
// EN-US: Optimization store. Keeps the result of the last optimization run.
//        The result includes the production plan, material usage, and total revenue.
//        While the algorithm runs, we show a loading indicator so the user knows
//        something good is being calculated behind the scenes.
export const useOptimizationStore = defineStore('optimization', () => {
  const result = ref(null)
  const loading = ref(false)
  const error = ref(null)

  async function runOptimization() {
    loading.value = true
    error.value = null
    result.value = null
    try {
      const response = await optimizationApi.optimize()
      result.value = response.data
    } catch (e) {
      // PT-BR: Mostramos a mensagem real do servidor (se disponivel) ou a mensagem
      //        de rede do axios. Isso ajuda o usuario a entender o que deu errado
      //        (ex: backend nao esta rodando vs. erro de dados).
      // EN-US: We show the actual server message (if available) or the axios network
      //        message. This helps the user understand what went wrong
      //        (e.g., backend not running vs. data error).
      if (e.response?.data?.error) {
        error.value = e.response.data.error
      } else if (e.code === 'ERR_NETWORK' || !e.response) {
        error.value = 'Could not connect to the server. Make sure the backend is running.'
      } else {
        error.value = e.message || 'Optimization failed'
      }
    } finally {
      loading.value = false
    }
  }

  function clearResult() {
    result.value = null
    error.value = null
  }

  return { result, loading, error, runOptimization, clearResult }
})
