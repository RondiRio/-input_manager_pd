import { defineStore } from 'pinia'
import { ref } from 'vue'
import rawMaterialApi from '@/api/rawMaterialApi'

// PT-BR: Store de materias-primas. Centraliza o estado e as operacoes CRUD, garantindo
//        que todos os componentes vejam os mesmos dados. Quando uma materia-prima e
//        criada ou atualizada, a lista e recarregada automaticamente para manter tudo
//        sincronizado -- simples e confiavel, como um bom cafe da manha.
// EN-US: Raw materials store. Centralizes state and CRUD operations, ensuring all
//        components see the same data. When a raw material is created or updated, the
//        list is automatically reloaded to keep everything in sync -- simple and
//        reliable, like a good morning coffee.
export const useRawMaterialStore = defineStore('rawMaterial', () => {
  const items = ref([])
  const loading = ref(false)
  const error = ref(null)

  async function fetchAll() {
    loading.value = true
    error.value = null
    try {
      const response = await rawMaterialApi.getAll()
      items.value = response.data
    } catch (e) {
      error.value = e.response?.data?.error || 'Failed to load raw materials'
    } finally {
      loading.value = false
    }
  }

  async function create(data) {
    const response = await rawMaterialApi.create(data)
    await fetchAll()
    return response.data
  }

  async function update(id, data) {
    const response = await rawMaterialApi.update(id, data)
    await fetchAll()
    return response.data
  }

  async function remove(id) {
    await rawMaterialApi.remove(id)
    await fetchAll()
  }

  return { items, loading, error, fetchAll, create, update, remove }
})
