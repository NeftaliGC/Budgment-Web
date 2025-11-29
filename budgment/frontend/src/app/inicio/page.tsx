'use client';

import '@/styles/globals.css';
import { API_BASE } from '@/config/api';
import { userId } from '@/config/userId';
import { ActionBarList } from '@/components/actionbar/actionbar';
import ContentSection from '@/components/ContentSection';
import DisplayCard from '@/components/inicio/DisplayCard';
import BudgetCard from '@/components/transacciones/BudgetCard';
import Image from 'next/image';
import { useEffect, useState } from 'react';
import { FloatingScreen } from "@/components/FloatingScreen";
import { IngresoForm } from '@/components/inicio/IngresoForm';
import { GastoForm } from '@/components/inicio/GastoForm';
import { CuentaForm } from '@/components/inicio/CuentaForm';
import { PresupuestoForm } from '@/components/inicio/PresupuestoForm';
import { apiCrearIngreso } from '@/lib/api';
import { apiCrearGasto } from '@/lib/api';
import { apiCrearCuenta } from '@/lib/api';
import { apiCrearPresupuesto } from '@/lib/api';


export default function InicioPages() {

    const [openIngresoModal, setOpenIngresoModal] = useState(false);
    const [openGastoModal, setOpenGastoModal] = useState(false);
    const [openNuevaCuentaModal, setOpenNuevaCuentaModal] = useState(false);
    const [openNuevoPresupuestoModal, setOpenNuevoPresupuestoModal] = useState(false);

    /* ---------------------- ACTION BAR ---------------------- */

    const acciones = [
        {label: "➕️ Registrar Ingreso", onClick: () => setOpenIngresoModal(true)},
        {label: "➖️️ Registrar Gasto", onClick: () => setOpenGastoModal(true)},
        {label: "💰️ Nueva Cuenta", onClick: () => setOpenNuevaCuentaModal(true)},
        {label: "🧾 Nuevo Presupuesto", onClick: () => setOpenNuevoPresupuestoModal(true)},
    ]

    /* ---------------------- GENERAL ---------------------- */

    interface GeneralItem {
        title: string;
        value: string | null;
        logo: string;
        endpoint: string;
    }

    const generalBase: GeneralItem[] = [
        { title: "Saldo Disponible", value: null, logo: "/wallet.svg", endpoint: `/users/${userId}/accounts/total_balance` },
        { title: "Ingresos del mes", value: null, logo: "/up_stats.svg", endpoint: `/users/${userId}/accounts/last_month/income` },
        { title: "Gastos del mes", value: null, logo: "/down_stats.svg", endpoint: `/users/${userId}/accounts/last_month/expense` },
    ];

    const [general, setGeneral] = useState<GeneralItem[]>(generalBase);

    useEffect(() => {
        async function fetchGeneral() {
            try {
                const results = await Promise.all(
                    generalBase.map(async (item) => {
                        const res = await fetch(
                            `${API_BASE}${item.endpoint}`, 
                            {
                                method: 'GET',
                                headers: { 
                                    'Content-Type': 'application/json',
                                    'Authorization': `Bearer ${sessionStorage.getItem("token")}` 
                                }
                            }
                        );

                        const data = await res.json();

                        return {
                            ...item,
                            value: "$" + data.total
                        };
                    })
                );

                setGeneral(results);
            } catch (err) {
                console.error("Error cargando general:", err);
            }
        }

        fetchGeneral();
    }, []);

    /* ---------------------- CUENTAS ---------------------- */
    
    interface CuentaWithBalance {
        title: string;
        balance: string;
        logo: string;
    }
    
    const [cuentas, setCuentas] = useState<CuentaWithBalance[]>([]);

    useEffect(() => {
        async function fetchCuentas() {
            try {
                // 1️⃣ Obtener lista de cuentas del usuario
                const res = await fetch(
                    `${API_BASE}/users/${userId}/accounts`,
                    {
                        method: "GET",
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': `Bearer ${sessionStorage.getItem("token")}`
                        }
                    }
                );
                // 2️⃣ Por cada cuenta, obtener su balance
                interface Cuenta {
                    id: number | string;
                    name: string;
                    [key: string]: unknown;
                }

                interface BalanceResponse {
                    balance: number;
                    [key: string]: unknown;
                }

                const cuentasData: Cuenta[] = await res.json();  // Array dinámico

                // 2️⃣ Por cada cuenta, obtener su balance
                const cuentasWithBalance: CuentaWithBalance[] = await Promise.all(
                    cuentasData.map(async (cuenta: Cuenta) => {
                        const resBalance = await fetch(
                            `${API_BASE}/users/${userId}/accounts/${cuenta.id}/balance`,
                            {
                                method: "GET",
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization': `Bearer ${sessionStorage.getItem("token")}`
                                }
                            }
                        );

                        const dataBalance: BalanceResponse = await resBalance.json();

                        return {
                            title: cuenta.name,
                            balance: `$${dataBalance.balance}`,
                            logo: "/card.svg"
                        };
                    })
                );

                
                setCuentas(cuentasWithBalance);
            } catch (err) {
                console.error("Error cargando cuentas:", err);
            }
        }

        fetchCuentas();
    }, []);

    /* ---------------------- PRESUPUESTOS ---------------------- */
    interface BudgetWithSpent {
        id: number | string;
        name: string;
        limit: string;
        spent: string;
        percent: number;
        date: string;
    }
    const [presupuestos, setPresupuestos] = useState<BudgetWithSpent[]>([]);

    useEffect(() => {
        async function fetchPresupuestos() {
            try {
                // 1️⃣ Obtener lista de presupuestos
                const res = await fetch(`${API_BASE}/users/${userId}/budgets`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${sessionStorage.getItem("token")}`
                    }
                });
                // 2️⃣ Para cada presupuesto, obtener lo gastado
                interface BudgetApi {
                    id: number | string;
                    name: string;
                    amountLimit?: number;
                    startDate?: string;
                    endDate?: string;
                    [key: string]: unknown;
                }

                interface SpentResponse {
                    spent?: number;
                    [key: string]: unknown;
                }

                const budgets: BudgetApi[] = await res.json();

                const budgetsWithSpent: BudgetWithSpent[] = await Promise.all(
                    budgets.map(async (budget: BudgetApi) => {
                        const spentRes = await fetch(
                            `${API_BASE}/users/${userId}/budgets/${budget.id}/spent`,
                            {
                                method: "GET",
                                headers: {
                                    "Content-Type": "application/json",
                                    "Authorization": `Bearer ${sessionStorage.getItem("token")}`
                                }
                            }
                        );

                        const spentData: SpentResponse = await spentRes.json();

                        const spent = spentData.spent ?? 0;
                        const limit = budget.amountLimit ?? 1; // evitar division entre 0

                        return {
                            id: budget.id,
                            name: budget.name,
                            limit: `$${limit / 100}`, // si viene en minor units
                            spent: `$${spent / 100}`,
                            percent: Math.min(Math.round((spent / limit) * 100), 100),
                            date: `${budget.startDate} → ${budget.endDate}`
                        };
                    })
                );

                setPresupuestos(budgetsWithSpent);

            } catch (err) {
                console.error("Error cargando presupuestos:", err);
            }
        }

        fetchPresupuestos();
    }, []);

    /* ---------------- FETCH PARA SELECTS ---------------- */

    interface SelectOption {
        value: string | number;
        label: string;
    }

    const [selectCuentas, setSelectCuentas] = useState<SelectOption[]>([]);
    const [selectCategorias, setSelectCategorias] = useState<SelectOption[]>([]);
    const [selectedCuenta, setSelectedCuenta] = useState<string>("");
    const [selectedCategoria, setSelectedCategoria] = useState<string>("");

    useEffect(() => {
        async function fetchSelectData() {
            try {
                const token = sessionStorage.getItem("token");

                const [resCuentas, resCategorias] = await Promise.all([
                    fetch(`${API_BASE}/users/${userId}/accounts`, {
                        headers: { "Authorization": `Bearer ${token}` }
                    }),
                    fetch(`${API_BASE}/users/${userId}/categories`, {
                        headers: { "Authorization": `Bearer ${token}` }
                    }),
                ]);

                const cuentasData = await resCuentas.json();
                const categoriasData = await resCategorias.json();

                // 🔹 convertir a formato {value, label}
                setSelectCuentas(
                    cuentasData.map((c: { id: string | number; name: string; currency?: string }) => ({
                        value: c.id,
                        label: `${c.name} (${c.currency ?? ''})`
                    }))
                );

                setSelectCategorias(
                    categoriasData.map((cat: { id: string | number; name: string; type?: string }) => ({
                        value: cat.id,
                        label: `${cat.name} — ${cat.type ?? ''}`
                    }))
                );
            } catch (err) {
                console.error("Error cargando selects:", err);
            }
        }

        fetchSelectData();
    }, []);


    return (
        <div>
            <ActionBarList actions={acciones} />

            {/* DATOS GENERALES */}
            <ContentSection title="Datos Generales" layout="grid" columns={4} gap="1rem">
                {general.map((item, i) => (
                    <DisplayCard key={i} title={item.title} logo={<Image src={item.logo} alt={item.title} width={24} height={24} />}>
                        <p style={{ color: "var(--text)", fontWeight: "bold", textAlign: "right" }}>{item.value}</p>
                    </DisplayCard>
                ))}
            </ContentSection>

            {/* CUENTAS */}
            <ContentSection title="Mis Cuentas" layout="grid" columns={4} gap="1rem"
                actionButton={{
                    enabled: true,
                    text: "Transferir entre cuentas",
                    icon: <Image src="/transfer.svg" alt="Transfer" width={16} height={16} />,
                }}
            >
                {cuentas.map((cuenta, i) => (
                    <DisplayCard key={i} title={cuenta.title} logo={<Image src={cuenta.logo} alt={cuenta.title} width={24} height={24} />}>
                        <p style={{ color: "var(--text)", fontWeight: "bold", textAlign: "right" }}>{cuenta.balance}</p>
                    </DisplayCard>
                ))}
            </ContentSection>

            {/* PRESUPUESTOS */}
            <ContentSection title="Mis Presupuestos" layout="grid" columns={3} gap="1rem">
                {presupuestos.map((p) => (
                    <BudgetCard
                        key={p.id}
                        budget={{
                            id: String(p.id),
                            name: p.name,
                            limit: p.limit,
                            date: p.date,
                            percent: p.percent
                        }}
                    />
                ))}
            </ContentSection>

            {/* MODALES */}
            <FloatingScreen open={openIngresoModal} onClose={() => setOpenIngresoModal(false)}>
                <IngresoForm 
                    onSubmit={async (payload) => {
                        const res = await apiCrearIngreso(payload);
                        if (res.ok) {
                            alert("Ingreso registrado");
                            setOpenIngresoModal(false);
                        } else {
                            alert("Error registrando ingreso");
                        }
                    }} 
                />
            </FloatingScreen>

            <FloatingScreen open={openGastoModal} onClose={() => setOpenGastoModal(false)}>
                <GastoForm
                    onSubmit={async (payload) => {
                        const res = await apiCrearGasto(payload);
                        console.log(payload);
                        if (res.ok) {
                            alert("Gasto registrado");
                            setOpenGastoModal(false);
                        } else {
                            alert("Error registrando gasto");
                        }
                    }}
                />
            </FloatingScreen>
            
            <FloatingScreen open={openNuevaCuentaModal} onClose={() => setOpenNuevaCuentaModal(false)}>
                <CuentaForm
                    onSubmit={async (payload) => {
                        const res = await apiCrearCuenta(payload);
                        if (res.ok) {
                            alert("Cuenta creada");
                            setOpenNuevaCuentaModal(false);
                        } else {
                            alert("Error creando cuenta");
                        }
                    }}
                />
            </FloatingScreen>

            <FloatingScreen open={openNuevoPresupuestoModal} onClose={() => setOpenNuevoPresupuestoModal(false)}>
                <PresupuestoForm
                    onSubmit={async (payload) => {
                        const res = await apiCrearPresupuesto(payload);
                        if (res.ok) {
                            alert("Presupuesto creado");
                            setOpenNuevoPresupuestoModal(false);
                        } else {
                            alert("Error creando presupuesto");
                        }
                    }}
                />
            </FloatingScreen>
        </div>
    );
}
