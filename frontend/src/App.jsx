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

  const handleAction = async (action, data = {}) => {
    try {
      const endpoint = action === 'request' ? `request?floor=${data.floor}` :
                       action === 'load' ? `load?weight=${data.weight}` : action;
      
      await fetch(`http://localhost:8080/api/elevator/${endpoint}`, { method: 'POST' });
      
      // Obtener el estado inmediatamente para reaccionar
      const res = await fetch('http://localhost:8080/api/elevator/status');
      const latestStatus = await res.json();
      setStatus(latestStatus);

      // Si las puertas se cerraron correctamente, automatizamos el viaje
      if (action === 'close-doors' && latestStatus.state.includes('En movimiento')) {
        setTimeout(async () => {
          // Una vez termina la animación CSS del viaje (2.5s), llegamos al piso
          await fetch(`http://localhost:8080/api/elevator/move`, { method: 'POST' });
          fetchStatus(); // Refresca y abre puertas
        }, 2500); 
      }
    } catch (err) {
      console.error(`Error in action ${action}:`, err);
    }
  };

  const isOverload = status.state.includes('Sobrecarga');
  const isMoving = status.state.includes('En movimiento');

  const isDoorsClosed = status.state === 'En reposo' || status.state === 'En movimiento';
  
  // Mientras está en movimiento, calculamos la posición visual basándonos en el destino
  const visualFloor = isMoving ? status.targetFloor : status.floor;

  return (
    <div className="main-container">
      {/* Visual Section */}
      <div className="elevator-shaft">
        {[4, 3, 2, 1, 0].map(f => (
          <div key={f} className="floor-marker">Nivel {f}</div>
        ))}
        <div 
          className={`elevator-car ${isMoving ? 'moving' : ''} ${isOverload ? 'overload' : ''}`}
          style={{ bottom: `calc(${visualFloor * 18}% + 1.5rem)` }}
        >
          {/* Puertas animadas */}
          <div className={`door left-door ${isDoorsClosed ? 'closed' : 'open'}`}></div>
          <div className={`door right-door ${isDoorsClosed ? 'closed' : 'open'}`}></div>

          {/* Animación del paquete (re-renderiza cuando cambia el peso) */}
          <div key={`cargo-${status.weight}`} className={`cargo-visual ${status.weight > 0 ? 'visible dropping' : ''}`}></div>
          
          {/* HUD digital sobre las puertas */}
          <div className="elevator-hud">
            <span style={{fontSize: '1.4rem', fontWeight: '900', color: '#fff', lineHeight: '1.2'}}>{status.floor}</span>
            <span style={{fontSize: '0.85rem', color: 'rgba(255,255,255,0.9)', fontWeight: '600'}}>{status.weight} kg</span>
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
                disabled={isMoving}
                style={{flex: 1, padding: '0.75rem', borderRadius: '12px', background: '#0f172a', color: '#fff', border: '1px solid rgba(255,255,255,0.1)', opacity: isMoving ? 0.5 : 1}}
               >
                 {[0,1,2,3,4].map(f => <option key={f} value={f}>Ir al Piso {f}</option>)}
               </select>
               <button onClick={() => handleAction('request', { floor: targetFloorInput })} disabled={isMoving}>Solicitar</button>
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
                  disabled={isMoving}
                  onChange={(e) => setInputWeight(parseInt(e.target.value) || 0)}
                />
                <input
                  type="number"
                  min="0"
                  max="2000"
                  value={inputWeight}
                  disabled={isMoving}
                  onChange={(e) => setInputWeight(parseInt(e.target.value) || 0)}
                  style={{
                    width: '75px', 
                    padding: '0.4rem', 
                    borderRadius: '8px', 
                    background: '#0f172a', 
                    color: '#fff', 
                    border: '1px solid rgba(255,255,255,0.1)', 
                    textAlign: 'center', 
                    fontWeight: 'bold',
                    opacity: isMoving ? 0.5 : 1
                  }}
                />
                <span style={{fontWeight: 'bold', fontSize: '0.9rem', color: 'var(--text-secondary)'}}>kg</span>
                <button onClick={() => handleAction('load', { weight: inputWeight })} disabled={isMoving}>Cargar</button>
              </div>
            </div>

            <button onClick={() => handleAction('close-doors')} disabled={isMoving}>Cerrar Puertas y Viajar</button>
            <button className="full-width danger" onClick={() => handleAction('unload')} disabled={isMoving}>Reset / Vaciar</button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
