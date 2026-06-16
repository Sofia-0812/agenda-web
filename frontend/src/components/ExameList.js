import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { exameService } from '../services/api';

function ExameList() {
  const [exames, setExames] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => { carregar(); }, []);

  const carregar = async () => {
    try { const r = await exameService.listar(); setExames(r.data); }
    catch (e) { console.error(e); }
    finally { setLoading(false); }
  };

  const deletar = async (id) => {
    if (window.confirm('Confirma exclusão?')) { await exameService.deletar(id); carregar(); }
  };

  if (loading) return <p>Carregando...</p>;

  return (
    <div>
      <div className="header">
        <h2>🔬 Exames de Laboratório</h2>
        <Link to="/exames/novo" className="btn btn-primary">+ Novo Exame</Link>
      </div>
      <table>
        <thead><tr><th>Descrição</th><th>Posologia</th><th>Ações</th></tr></thead>
        <tbody>
          {exames.map(e => (
            <tr key={e.id}>
              <td>{e.descricao}</td>
              <td>{e.posologia || '-'}</td>
              <td>
                <Link to={`/exames/editar/${e.id}`} className="btn btn-sm">Editar</Link>
                <button onClick={() => deletar(e.id)} className="btn btn-danger btn-sm">Excluir</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {exames.length === 0 && <p className="empty">Nenhum exame cadastrado.</p>}
    </div>
  );
}

export default ExameList;
