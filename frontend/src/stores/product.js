import { defineStore } from 'pinia'
import { ref } from 'vue'
import productApi from '@/api/productApi'

// PT-BR: Store de produtos. Mesma abordagem da store de materias-primas.
//        O estado e compartilhado entre todos os componentes que precisam de
//        dados de produtos, evitando chamadas duplicadas a API.
// EN-US: Products store. Same approach as the raw materials store.
//        The state is shared among all components that need product data,
//        avoiding duplicate API calls.
export const useProductStore = defineStore('product', () => {
  const items = ref([])
  const loading = ref(false)
  const error = ref(null)

  async function fetchAll() {
    loading.value = true
    error.value = null
    try {
      const response = await productApi.getAll()
      items.value = response.data
    } catch (e) {
      error.value = e.response?.data?.error || 'Failed to load products'
    } finally {
      loading.value = false
    }
  }

  async function create(data) {
    const response = await productApi.create(data)
    await fetchAll()
    return response.data
  }

  async function update(id, data) {
    const response = await productApi.update(id, data)
    await fetchAll()
    return response.data
  }

  async function remove(id) {
    await productApi.remove(id)
    await fetchAll()
  }

  return { items, loading, error, fetchAll, create, update, remove }
})
