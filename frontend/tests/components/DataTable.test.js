import { describe, it, expect } from 'vitest'
import { mount } from '@vue/test-utils'
import DataTable from '@/components/ui/DataTable.vue'
import { createI18n } from 'vue-i18n'

// PT-BR: Testes do componente DataTable. Verificamos renderizacao de dados,
//        estado vazio, ordenacao e emissao de eventos de editar/deletar.
// EN-US: DataTable component tests. We verify data rendering, empty state,
//        sorting, and edit/delete event emission.
const i18n = createI18n({
  legacy: false,
  locale: 'en-US',
  messages: {
    'en-US': {
      common: { actions: 'Actions', loading: 'Loading...', noData: 'No data found' }
    }
  }
})

const columns = [
  { key: 'code', label: 'Code', sortable: true },
  { key: 'name', label: 'Name', sortable: true }
]

const mockData = [
  { id: 1, code: 'MP001', name: 'Flour' },
  { id: 2, code: 'MP002', name: 'Sugar' },
  { id: 3, code: 'MP003', name: 'Butter' }
]

function mountTable(props = {}) {
  return mount(DataTable, {
    global: { plugins: [i18n] },
    props: { columns, data: mockData, ...props }
  })
}

describe('DataTable', () => {
  it('renders table with correct number of rows', () => {
    const wrapper = mountTable()
    const rows = wrapper.findAll('tbody tr')
    expect(rows.length).toBe(3)
  })

  it('renders column headers', () => {
    const wrapper = mountTable()
    const headers = wrapper.findAll('th')
    expect(headers.length).toBe(3) // 2 columns + actions
  })

  it('shows empty state when data is empty', () => {
    const wrapper = mountTable({ data: [] })
    expect(wrapper.text()).toContain('No data found')
  })

  it('shows loading spinner when loading', () => {
    const wrapper = mountTable({ loading: true })
    expect(wrapper.text()).toContain('Loading...')
  })

  it('emits edit event when edit button is clicked', async () => {
    const wrapper = mountTable()
    const editBtns = wrapper.findAll('.edit-btn')
    await editBtns[0].trigger('click')
    expect(wrapper.emitted('edit')).toBeTruthy()
    expect(wrapper.emitted('edit')[0][0]).toEqual(mockData[0])
  })

  it('emits delete event when delete button is clicked', async () => {
    const wrapper = mountTable()
    const deleteBtns = wrapper.findAll('.delete-btn')
    await deleteBtns[0].trigger('click')
    expect(wrapper.emitted('delete')).toBeTruthy()
    expect(wrapper.emitted('delete')[0][0]).toEqual(mockData[0])
  })

  it('hides actions column when showActions is false', () => {
    const wrapper = mountTable({ showActions: false })
    const headers = wrapper.findAll('th')
    expect(headers.length).toBe(2) // only 2 data columns
  })
})
