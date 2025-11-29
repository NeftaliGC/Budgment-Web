'use client';
import { useState } from "react";

type CreateBudgetPayload = {
    name: string;
    amountLimit: number;
    scope: string;
    periodType: number;
    startDate: string;
    endDate: string;
};

export function PresupuestoForm({ onSubmit }: { onSubmit: (data: CreateBudgetPayload) => void }) {
    const [name, setName] = useState("");
    const [limit, setLimit] = useState("");
    const [scope, setScope] = useState("Global");
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [periodType, setPeriodType] = useState("");

    return (
        <div className="modal">
            <h2>Crear Presupuesto</h2>

            <input
                type="text"
                placeholder="Nombre del presupuesto"
                value={name}
                onChange={(e) => setName(e.target.value)}
            />

            <input
                type="number"
                placeholder="Monto límite (ej. 1500.00)"
                value={limit}
                onChange={(e) => setLimit(e.target.value)}
            />

            <select value={scope} onChange={(e) => setScope(e.target.value)}>
                <option value="Global">Global</option>
                <option value="Category">Por Categoría</option>
                <option value="Account">Por Cuenta</option>
            </select>

            <input
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
            />

            <input
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
            />

            <input
                type="number"
                placeholder="Periodo (ej. 1=Mensual, 2=Trimestral...)"
                value={periodType}
                onChange={(e) => setPeriodType(e.target.value)}
            />

            <button
                onClick={() => {
                    onSubmit({
                        name,
                        scope,
                        startDate,
                        endDate,
                        periodType: parseInt(periodType),
                        amountLimit: Math.round(parseFloat(limit) * 100)
                    });
                }}
            >
                Crear Presupuesto
            </button>
        </div>
    );
}
