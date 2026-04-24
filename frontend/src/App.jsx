import React, { useState, useEffect } from 'react';

const API_BASE = 'http://localhost:8080/api/elevator';

function App() {
  const [status, setStatus] = useState({
    state: 'IDLE (En reposo)',
    floor: 0,
    weight: 0,
    targetFloor: 0,
    maxWeight: 1000
  });
  const [inputWeight, setInputWeight] = useState(500);
  const [targetFloorInput, setTargetFloorInput] = useState(0);

  const fetchStatus = async () => {
    try {
      const response = await fetch(`${API_BASE}/status`);
      const data = await response.json();
      setStatus(data);
    } catch (err) {
      console.error('Error fetching status:', err);
    }
  };

  useEffect(() => {
    const interval = setInterval(fetchStatus, 500);
    return () => clearInterval(interval);
  }, []);

  const handleAction = async (endpoint, params = {}) => {
    const url = new URL(`${API_BASE}/${endpoint}`);
    Object.keys(params).forEach(key => url.searchParams.append(key, params[key]));
    
    try {
      await fetch(url, { method: 'POST' });
      fetchStatus();
    } catch (err) {
      console.error(`Error in ${endpoint}:`, err);
    }
  };

  const isOverload = status.state.includes('OVERLOAD');
  const isMoving = status.state.includes('MOVING');

  return (
    <div className="main-container">
      {/* Visual Section */}
      <div className="elevator-shaft">
        {[4, 3, 2, 1, 0].map(f => (
          <div key={f} className="floor-marker">Nivel {f}</div>
        ))}
        <div 
          className={`elevator-car ${isMoving ? 'moving' : ''} ${isOverload ? 'overload' : ''}`}
          style={{ bottom: `calc(${status.floor * 18}% + 1.5rem)` }}
        >
          <div className={`cargo-visual ${status.weight > 0 ? 'visible' : ''}`}></div>
          <div style={{display: 'flex', flexDirection: 'column', alignItems: 'center'}}>
            <span style={{fontSize: '0.8rem', fontWeight: 'bold', color: '#fff'}}>{status.floor}</span>
            <span style={{fontSize: '0.6rem', color: 'rgba(255,255,255,0.7)'}}>{status.weight} kg</span>
          </div>
        </div>
      </div>

      {/* Controls Section */}
      <div className="control-panel">
        <header>
          <h1>Control de Ascensor</h1>
          <p style={{color: 'var(--text-secondary)', fontSize: '0.9rem'}}>Sistema de Carga de Misión Crítica</p>
        </header>

        <div className="status-card">
          <div className="status-grid">
            <div className="status-item">
              <span className="status-label">Estado</span>
              <span className="status-value" style={{color: isOverload ? 'var(--danger)' : 'var(--success)', fontSize: '1rem'}}>
                {status.state}
              </span>
            </div>
            <div className="status-item">
              <span className="status-label">Piso Actual</span>
              <span className="status-value">{status.floor}</span>
            </div>
            <div className="status-item">
              <span className="status-label">Carga</span>
              <span className="status-value" style={{fontSize: '1rem'}}>{status.weight} / {status.maxWeight} kg</span>
            </div>
            <div className="status-item">
              <span className="status-label">Destino</span>
              <span className="status-value">{status.targetFloor}</span>
            </div>
          </div>
        </div>

        <div className={`alert-box ${isOverload ? 'visible' : ''}`}>
          <span>⚠️</span>
          <div>
            <strong>SOBRECARGA DETECTADA</strong><br/>
            El sistema se ha bloqueado por seguridad. Retire peso.
          </div>
        </div>

        <div className="status-card">
          <h2 style={{fontSize: '0.9rem', marginBottom: '1.25rem', textTransform: 'uppercase', color: 'var(--text-secondary)'}}>Operaciones</h2>
          <div className="action-grid">
            <div className="full-width" style={{display: 'flex', gap: '0.75rem', marginBottom: '0.5rem'}}>
               <select 
                value={targetFloorInput} 
                onChange={(e) => setTargetFloorInput(parseInt(e.target.value))}
                style={{flex: 1, padding: '0.75rem', borderRadius: '12px', background: '#0f172a', color: '#fff', border: '1px solid rgba(255,255,255,0.1)'}}
               >
                 {[0,1,2,3,4].map(f => <option key={f} value={f}>Ir al Piso {f}</option>)}
               </select>
               <button onClick={() => handleAction('request', { floor: targetFloorInput })}>Solicitar</button>
            </div>

            <div className="full-width" style={{display: 'flex', flexDirection: 'column', gap: '0.75rem', marginBottom: '1rem'}}>
              <span className="status-label">Simulador de Peso</span>
              <div className="weight-input-group">
                <input 
                  type="range" 
                  min="0" 
                  max="2000" 
                  step="50" 
                  value={inputWeight} 
                  onChange={(e) => setInputWeight(parseInt(e.target.value) || 0)}
                />
                <input
                  type="number"
                  min="0"
                  max="2000"
                  value={inputWeight}
                  onChange={(e) => setInputWeight(parseInt(e.target.value) || 0)}
                  style={{
                    width: '75px', 
                    padding: '0.4rem', 
                    borderRadius: '8px', 
                    background: '#0f172a', 
                    color: '#fff', 
                    border: '1px solid rgba(255,255,255,0.1)', 
                    textAlign: 'center', 
                    fontWeight: 'bold'
                  }}
                />
                <span style={{fontWeight: 'bold', fontSize: '0.9rem', color: 'var(--text-secondary)'}}>kg</span>
                <button onClick={() => handleAction('load', { weight: inputWeight })}>Cargar</button>
              </div>
            </div>

            <button onClick={() => handleAction('close-doors')} disabled={isMoving}>Cerrar Puertas</button>
            <button onClick={() => handleAction('move')} disabled={isMoving || isOverload}>Iniciar Viaje</button>
            <button onClick={() => handleAction('arrive')} disabled={!isMoving}>Llegar</button>
            <button className="danger" onClick={() => handleAction('unload')}>Reset / Vaciar</button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
