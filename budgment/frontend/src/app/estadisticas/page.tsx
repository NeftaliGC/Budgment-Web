'use client';

import '@/styles/globals.css';
import { ActionBarList } from '@/components/actionbar/actionbar';
import { useState } from 'react';

export default function EstadisticasPages() {
    const [activeSection, setActiveSection] = useState<string | null>(null);

    const acciones = [
        { label: "📊 Mis Gastos", onClick: () => setActiveSection(activeSection === 'gastos' ? null : 'gastos') },
        { label: "📝️️ Historial", onClick: () => setActiveSection(activeSection === 'historial' ? null : 'historial') },
        { label: "🧮️ Presupuestos", onClick: () => setActiveSection(activeSection === 'presupuestos' ? null : 'presupuestos') },
    ]

    return (
        <div>
            <ActionBarList actions={acciones} />
            
            {activeSection === 'gastos' && (
                <div style={{
                    padding: '1.5rem',
                    margin: '1rem',
                    border: '2px solid var(--surface)',
                    borderRadius: '0.5rem',
                    backgroundColor: 'var(--background)',
                    animation: 'slideDown 0.3s ease-out'
                }}>
                    <h3>📊 Mis Gastos</h3>
                    <p>Aquí irá el contenido de tus gastos</p>
                </div>
            )}
            
            {activeSection === 'historial' && (
                <div style={{
                    padding: '1.5rem',
                    margin: '1rem',
                    border: '2px solid var(--surface)',
                    borderRadius: '0.5rem',
                    backgroundColor: 'var(--background)',
                    animation: 'slideDown 0.3s ease-out'
                }}>
                    <h3>📝 Historial</h3>
                    <p>Aquí irá el historial de transacciones</p>
                </div>
            )}
            
            {activeSection === 'presupuestos' && (
                <div style={{
                    padding: '1.5rem',
                    margin: '1rem',
                    border: '2px solid var(--surface)',
                    borderRadius: '0.5rem',
                    backgroundColor: 'var(--background)',
                    animation: 'slideDown 0.3s ease-out'
                }}>
                    <h3>🧮 Presupuestos</h3>
                    <p>Aquí irán tus presupuestos</p>
                </div>
            )}

            <style>{`
                @keyframes slideDown {
                    from {
                        opacity: 0;
                        transform: translateY(-10px);
                    }
                    to {
                        opacity: 1;
                        transform: translateY(0);
                    }
                }
            `}</style>
        </div>
    );
}