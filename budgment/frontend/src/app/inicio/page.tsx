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

export default function InicioPages() {

    const acciones = [
        {label: "➕️ Registrar Ingreso"},
        {label: "➖️️ Registrar Gasto"},
        {label: "💰️ Nueva Cuenta"},
        {label: "🧾 Nuevo Presupuesto"},
    ]

    const generalBase = [
        { title: "Saldo Disponible", value: null, logo: "/wallet.svg", endpoint: `/users/${userId}/accounts/total_balance` },
        { title: "Ingresos del mes", value: null, logo: "/up_stats.svg", endpoint: `/users/${userId}/accounts/last_month/income` },
        { title: "Gastos del mes", value: null, logo: "/down_stats.svg", endpoint: `/users/${userId}/accounts/last_month/expense` },
    ];

    const [general, setGeneral] = useState(generalBase);

    useEffect(() => {
        async function fetchGeneral() {
            try {
                const results = await Promise.all(
                    generalBase.map(async (item) => {
                        // peticion con headers
                        const res = await fetch(
                            `${API_BASE}${item.endpoint}`, 
                            { method: 'GET', 
                                headers: { 
                                    'Content-Type': 'application/json', 
                                    'Authorization': `Bearer ${sessionStorage.getItem("token")}` 
                                } 
                            }
                        );
                        const data = await res.json();
                        return {
                            ...item,
                            value: data.total   // << 🔹 solo actualización del campo value
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

    const cuentas = [
        {title: "Cuenta A", balance: "$1,200.00", logo: "/card.svg"},
        {title: "Cuenta B", balance: "$3,800.00", logo: "/card.svg"},
    ]

    const presupuestos = [
        {title: "Alimentación", limit: "$500.00"},
        {title: "Transporte", limit: "$300.00"},
        {title: "Ocio", limit: "$200.00"},
        {title: "Salud", limit: "$400.00"},
    ]

    return (
        <div>
            <ActionBarList actions={acciones} />

            <ContentSection title="Datos Generales" layout="grid" columns={4} gap="1rem">
                {general.map((item, i) => (
                    <DisplayCard key={i} title={item.title} logo={<Image src={item.logo} alt={item.title} width={24} height={24} />}>
                        <p style={{ color: "var(--text)", fontWeight: "bold", textAlign: "right" }}>{item.value}</p>
                    </DisplayCard>
                ))}
            </ContentSection>

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

            <ContentSection title="Mis Presupuestos" layout="grid" columns={3} gap="1rem">
                {presupuestos.map((presupuesto, i) => (
                    <BudgetCard 
                        key={i}
                        budget={{
                            id: `budget-${i}`,
                            name: presupuesto.title,
                            limit: presupuesto.limit,
                            date: "30 Sep 2024",
                            percent: Math.floor(Math.random() * 100),
                        }}
                    />
                ))}
            </ContentSection>
        </div>
    );
}