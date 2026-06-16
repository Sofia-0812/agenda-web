import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { profissionalService } from '../services/api';

const categoriaBadge = (cat) => {
  const map = { MEDICO: ['badge-medico','Médico'], FISIOTERAPEUTA: ['badge-fisio','Fisioterapeuta'], PSICOLOGO: ['badge-psico','Psicólogo'] };
  const [cls, label] = map[cat] || ['',''];
  return <span className={`badge ${cls}`}>{label}</span>;
};

function ProfissionalList() {
  const [profissionais, setProfissionais] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => { carregar(); }, []);

  const carregar = async () => {
    try { const r = await profissionalService.listar(); setProfissionais(r.data); }
    catch (e) { console.error(e); }
    finally { setLoading(false); }
  };

  const excluir = async (id) => {
    if (window.confirm('Confirma exclusão?')) {
      await profissionalService.excluir(id);
      carregar();
    }
  };

  if (loading) return <p>Carregando...</p>;

  return (
    <div>
      <div className="header">
        <h2>👨‍⚕️ Profissionais de Saúde</h2>
        <Link to="/profissionais/novo" className="btn btn-primary">+ Novo Profissional</Link>
      </div>
      <table>
        <thead><tr><th>Nome</th><th>Telefone</th><th>Categoria</th><th>Endereço</th><th>Ações</th></tr></thead>
        <tbody>
          {profissionais.map(p => (
            <tr key={p.id}>
              <td>{p.nome}</td>
              <td>{p.telefone || '-'}</td>
              <td>{categoriaBadge(p.categoria)}</td>
              <td>{p.endereco || '-'}</td>
              <td>
                <Link to={`/profissionais/editar/${p.id}`} className="btn btn-sm">Editar</Link>
                <button onClick={() => excluir(p.id)} className="btn btn-danger btn-sm">Excluir</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {profissionais.length === 0 && <p className="empty">Nenhum profissional cadastrado.</p>}
    </div>
  );
}

export default ProfissionalList;
