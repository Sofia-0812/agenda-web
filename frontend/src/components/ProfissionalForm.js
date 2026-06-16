import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { profissionalService } from '../services/api';

function ProfissionalForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [form, setForm] = useState({ nome: '', telefone: '', endereco: '', categoria: 'MEDICO' });

  useEffect(() => {
    if (id) profissionalService.buscar(id).then(r => setForm(r.data));
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (id) await profissionalService.alterar(id, form);
      else await profissionalService.inserir(form);
      navigate('/profissionais');
    } catch (err) { console.error(err); }
  };

  return (
    <div>
      <h2>{id ? 'Alterar Profissional' : 'Inserir Profissional'}</h2>
      <form onSubmit={handleSubmit} className="form">
        <div className="form-group">
          <label>Nome *</label>
          <input type="text" value={form.nome} required onChange={e => setForm({...form, nome: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Telefone</label>
          <input type="text" value={form.telefone} onChange={e => setForm({...form, telefone: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Endereço</label>
          <input type="text" value={form.endereco} onChange={e => setForm({...form, endereco: e.target.value})} />
        </div>
        <div className="form-group">
          <label>Categoria *</label>
          <select value={form.categoria} onChange={e => setForm({...form, categoria: e.target.value})}>
            <option value="MEDICO">Médico</option>
            <option value="FISIOTERAPEUTA">Fisioterapeuta</option>
            <option value="PSICOLOGO">Psicólogo</option>
          </select>
        </div>
        <button type="submit" className="btn btn-primary">Salvar</button>
        <button type="button" className="btn" onClick={() => navigate('/profissionais')}>Cancelar</button>
      </form>
    </div>
  );
}

export default ProfissionalForm;
