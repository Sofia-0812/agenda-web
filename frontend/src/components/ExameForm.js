import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { exameService, atendimentoService } from '../services/api';

function ExameForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [form, setForm] = useState({ descricao: '', posologia: '', atendimento: null });
  const [atendimentos, setAtendimentos] = useState([]);

  useEffect(() => {
    atendimentoService.listar().then(r => setAtendimentos(r.data));
    if (id) exameService.buscar(id).then(r => setForm(r.data));
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) await exameService.atualizar(id, form);
      else await exameService.criar(form);
      navigate('/exames');
    } catch (err) { console.error(err); }
  };

  return (
    <div>
      <h2>{id ? 'Editar Exame' : 'Novo Exame de Laboratório'}</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Descrição *</label>
          <textarea value={form.descricao} required rows={3} onChange={e => setForm({...form, descricao: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Posologia</label>
          <textarea value={form.posologia} rows={3} onChange={e => setForm({...form, posologia: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Atendimento vinculado</label>
          <select value={form.atendimento?.id || ''} onChange={e => setForm({...form, atendimento: e.target.value ? {id: parseInt(e.target.value)} : null})}>
            <option value="">Selecione um atendimento</option>
            {atendimentos.map(a => <option key={a.id} value={a.id}>{a.titulo} - {a.data}</option>)}
          </select>
        </div>
        <button type="submit" className="btn btn-primary">Salvar</button>
        <button type="button" className="btn" onClick={() => navigate('/exames')}>Cancelar</button>
      </form>
    </div>
  );
}

export default ExameForm;
