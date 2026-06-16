import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { atendimentoService, profissionalService } from '../services/api';

function AtendimentoForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [form, setForm] = useState({ titulo: '', data: '', horario: '', linkVideoConferencia: '', receita: '', profissional: null });
  const [profissionais, setProfissionais] = useState([]);

  useEffect(() => {
    profissionalService.listar().then(r => setProfissionais(r.data));
    if (id) atendimentoService.buscar(id).then(r => setForm(r.data));
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) await atendimentoService.atualizar(id, form);
      else await atendimentoService.criar(form);
      navigate('/atendimentos');
    } catch (err) { console.error(err); }
  };

  return (
    <div>
      <h2>{id ? 'Editar Atendimento' : 'Novo Atendimento'}</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Título *</label>
          <input type="text" value={form.titulo} required onChange={e => setForm({...form, titulo: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Data *</label>
          <input type="date" value={form.data} required onChange={e => setForm({...form, data: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Horário</label>
          <input type="time" value={form.horario} onChange={e => setForm({...form, horario: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Link Videoconferência</label>
          <input type="url" value={form.linkVideoConferencia} onChange={e => setForm({...form, linkVideoConferencia: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Receita</label>
          <select value={form.receita || ''} onChange={e => setForm({...form, receita: e.target.value || null})}>
            <option value="">Sem receita</option>
            <option value="REMEDIO">Remédio (Médico)</option>
            <option value="ATIVIDADE_FISICA">Atividade Física (Fisio)</option>
            <option value="ATIVIDADE_MENTAL">Atividade Mental (Psicólogo)</option>
          </select>
        </div>
        <div className="form-group">
          <label>Profissional</label>
          <select value={form.profissional?.id || ''} onChange={e => setForm({...form, profissional: e.target.value ? {id: parseInt(e.target.value)} : null})}>
            <option value="">Selecione um profissional</option>
            {profissionais.map(p => <option key={p.id} value={p.id}>{p.nome} ({p.categoria})</option>)}
          </select>
        </div>
        <button type="submit" className="btn btn-primary">Salvar</button>
        <button type="button" className="btn" onClick={() => navigate('/atendimentos')}>Cancelar</button>
      </form>
    </div>
  );
}

export default AtendimentoForm;
