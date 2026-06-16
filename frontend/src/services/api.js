import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || '/api';

const api = axios.create({
  baseURL: API_URL,
  headers: { 'Content-Type': 'application/json' }
});

export const profissionalService = {
  listar: () => api.get('/profissionais'),
  buscar: (id) => api.get(`/profissionais/${id}`),
  inserir: (p) => api.post('/profissionais', p),
  alterar: (id, p) => api.put(`/profissionais/${id}`, p),
  excluir: (id) => api.delete(`/profissionais/${id}`),
  consultarPorNome: (nome) => api.get(`/profissionais/buscar-nome?nome=${nome}`),
  consultarPorCategoria: (cat) => api.get(`/profissionais/buscar-categoria?categoria=${cat}`)
};

export const atendimentoService = {
  listar: () => api.get('/atendimentos'),
  buscar: (id) => api.get(`/atendimentos/${id}`),
  criar: (a) => api.post('/atendimentos', a),
  atualizar: (id, a) => api.put(`/atendimentos/${id}`, a),
  deletar: (id) => api.delete(`/atendimentos/${id}`)
};

export const exameService = {
  listar: () => api.get('/exames'),
  buscar: (id) => api.get(`/exames/${id}`),
  criar: (e) => api.post('/exames', e),
  atualizar: (id, e) => api.put(`/exames/${id}`, e),
  deletar: (id) => api.delete(`/exames/${id}`)
};

export default api;
