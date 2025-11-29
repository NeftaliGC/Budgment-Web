import { API_BASE } from "@/config/api";
import { userId } from "@/config/userId";

type TransactionData = { amount: number } & Record<string, unknown>;
type AccountData = Record<string, unknown>;
type BudgetData = Record<string, unknown>;

export async function apiCrearIngreso(data: TransactionData) {
    console.log("apiCrearIngreso data:", data);
    return fetch(`${API_BASE}/users/${userId}/transactions`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: JSON.stringify({
            ...data,
            amount: Math.abs(data.amount),  // ingresos son positivos
        })
    });
}

export async function apiCrearGasto(data: TransactionData) {

    console.log("Creando gasto con data:", data);
    return fetch(`${API_BASE}/users/${userId}/transactions`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: JSON.stringify({
            ...data,
            amount: -Math.abs(data.amount), // gastos son negativos
        })
    });
}

export async function apiCrearCuenta(data: AccountData) {
    return fetch(`${API_BASE}/users/${userId}/accounts`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: JSON.stringify(data)
    });
}

export async function apiCrearPresupuesto(data: BudgetData) {
    return fetch(`${API_BASE}/users/${userId}/budgets`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${sessionStorage.getItem("token")}`
        },
        body: JSON.stringify(data)
    });
}
