'use client';
import { useState, useEffect } from "react";
import { API_BASE } from "@/config/api";
import { userId } from "@/config/userId";

type GastoData = {
    amount: number;        // en centavos, negativo
    description: string;
    accountId: string;
    currency: string;
    date: string;
};

export function GastoForm({ onSubmit }: { onSubmit: (data: GastoData) => void }) {
    const [amount, setAmount] = useState("");
    const [description, setDescription] = useState("");
    const [accountId, setAccountId] = useState("");
    const [date, setDate] = useState("");

    // ⚠️ Aquí cargas las cuentas del backend
    const [cuentas, setCuentas] = useState<{ id: string; name: string; currency: string }[]>([]);

    useEffect(() => {
        async function fetchCuentas() {
            const res = await fetch(`${API_BASE}/users/${userId}/accounts`, {
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${sessionStorage.getItem("token")}`
                }
            });
            setCuentas(await res.json());
        }
        fetchCuentas();
    }, []);

    return (
        <div className="modal">
            <h2>Registrar Gasto</h2>

            <select value={accountId} onChange={(e) => setAccountId(e.target.value)}>
                <option value="">Selecciona una cuenta</option>
                {cuentas.map(c => (
                    <option key={c.id} value={c.id}>{c.name} ({c.currency})</option>
                ))}
            </select>

            <input 
                type="number"
                placeholder="Cantidad (ej. -150.00)"
                max={-1}
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
            />

            <input 
                type="text"
                placeholder="Descripción"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
            />

            <input 
                type="date"
                value={date}
                onChange={(e) => setDate(e.target.value)}
            />

            <button 
                onClick={() => {
                    onSubmit({
                        amount: parseFloat(amount) * 100, // convertir a centavos
                        description,
                        accountId,
                        currency: "MXN",
                        date
                    });
                }}
            >
                Registrar
            </button>
        </div>
    );
}
