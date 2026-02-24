import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useRawMaterialStore } from '@/stores/rawMaterial'

// PT-BR: Mock do modulo de API para que os testes nao dependam do backend rodando.
//        Simulamos as respostas da API para testar a logica da store isoladamente.
// EN-US: Mock of the API module so tests don't depend on the backend running.
//        We simulate API responses to test the store logic in isolation.
vi.mock('@/api/rawMaterialApi', () => ({
  default: {
    getAll: vi.fn(),
    create: vi.fn(),
    update: vi.fn(),
    remove: vi.fn()
  }
}))

import rawMaterialApi from '@/api/rawMaterialApi'

describe('Raw Material Store', () => {
  beforeEach(() => {
    setActivePinia(createPinia())
    vi.clearAllMocks()
  })

  it('starts with empty items and no loading', () => {
    const store = useRawMaterialStore()
    expect(store.items).toEqual([])
    expect(store.loading).toBe(false)
    expect(store.error).toBeNull()
  })

  it('fetches all raw materials successfully', async () => {
    const mockData = [
      { id: 1, code: 'MP001', name: 'Flour', stockQuantity: 500, unit: 'g' },
      { id: 2, code: 'MP002', name: 'Sugar', stockQuantity: 300, unit: 'g' }
    ]
    rawMaterialApi.getAll.mockResolvedValue({ data: mockData })

    const store = useRawMaterialStore()
    await store.fetchAll()

    expect(store.items).toEqual(mockData)
    expect(store.loading).toBe(false)
    expect(store.error).toBeNull()
  })

  it('handles fetch error gracefully', async () => {
    rawMaterialApi.getAll.mockRejectedValue({
      response: { data: { error: 'Server error' } }
    })

    const store = useRawMaterialStore()
    await store.fetchAll()

    expect(store.items).toEqual([])
    expect(store.error).toBe('Server error')
    expect(store.loading).toBe(false)
  })

  it('creates a new raw material and refreshes list', async () => {
    rawMaterialApi.create.mockResolvedValue({ data: { id: 3, code: 'MP003' } })
    rawMaterialApi.getAll.mockResolvedValue({ data: [{ id: 3, code: 'MP003' }] })

    const store = useRawMaterialStore()
    const result = await store.create({ code: 'MP003', name: 'Butter', stockQuantity: 200, unit: 'g' })

    expect(rawMaterialApi.create).toHaveBeenCalledOnce()
    expect(rawMaterialApi.getAll).toHaveBeenCalled()
    expect(result.code).toBe('MP003')
  })

  it('removes a raw material and refreshes list', async () => {
    rawMaterialApi.remove.mockResolvedValue({})
    rawMaterialApi.getAll.mockResolvedValue({ data: [] })

    const store = useRawMaterialStore()
    await store.remove(1)

    expect(rawMaterialApi.remove).toHaveBeenCalledWith(1)
    expect(rawMaterialApi.getAll).toHaveBeenCalled()
  })
})
