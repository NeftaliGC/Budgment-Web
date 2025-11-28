import '@/styles/globals.css';
import { ActionBarList } from '@/components/actionbar/actionbar';

export default function EstadisticasPages() {

    const acciones = [
        {label: "📊 Mis Gastos"},
        {label: "📝️️ Historial"},
        {label: "🧮️ Presupuestos"},
    ]

    return (
        <div>
            <ActionBarList actions={acciones} />
        </div>
    );
}