'use client';
import { useState } from "react";

type CuentaData = {
    name: string;
    currency: string;
    initialBalance: number; // en centavos
};

export function CuentaForm({ onSubmit }: { onSubmit: (data: CuentaData) => void }) {
    const [name, setName] = useState("");
    const [currency, setCurrency] = useState("MXN");
    const [initialBalance, setInitialBalance] = useState("");

    return (
        <div className="modal">
            <h2>Crear Cuenta</h2>

            <input
                type="text"
                placeholder="Nombre de la cuenta"
                value={name}
                onChange={(e) => setName(e.target.value)}
            />

            <select value={currency} onChange={(e) => setCurrency(e.target.value)}>
                <option value="MXN">MXN (Peso Mexicano)</option>
                <option value="USD">USD (Dólar)</option>
                <option value="EUR">EUR (Euro)</option>
            </select>

            <input
                type="number"
                placeholder="Saldo inicial (opcional)"
                value={initialBalance}
                onChange={(e) => setInitialBalance(e.target.value)}
            />

            <button
                onClick={() => {
                    onSubmit({
                        name,
                        currency,
                        initialBalance: initialBalance
                            ? parseFloat(initialBalance)
                            : 0
                    });
                }}
            >
                Crear Cuenta
            </button>
        </div>
    );
}
