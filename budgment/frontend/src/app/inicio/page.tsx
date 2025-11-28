import '@/styles/globals.css';
import { ActionBarList } from '@/components/actionbar/actionbar';
import ContentSection from '@/components/ContentSection';
import DisplayCard from '@/components/inicio/DisplayCard';
import BudgetCard from '@/components/transacciones/BudgetCard';
import Image from 'next/image';

export default function InicioPages() {

    const acciones = [
        {label: "➕️ Registrar Ingreso"},
        {label: "➖️️ Registrar Gasto"},
        {label: "💰️ Nueva Cuenta"},
        {label: "🧾 Nuevo Presupuesto"},
    ]

    const general = [
        {title: "Saldo Disponible", value: "$5,000.00", logo: "/wallet.svg"},
        {title: "Ingresos del mes", value: "$5,000.00", logo: "/up_stats.svg"},
        {title: "Gastos del mes", value: "$5,000.00", logo: "/down_stats.svg"},
    ]

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