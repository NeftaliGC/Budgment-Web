import ContentSection from "@/components/ContentSection";
import { ActionBarList } from "@/components/actionbar/actionbar";

export default function TransaccionesPage() {

  const acciones = [
    {label: "🔍 Ver todas"},
  ]

  return (
    <div>
      <ActionBarList actions={acciones} />
      <ContentSection title="Transacciones">
        <p>Aquí puedes ver y gestionar todas tus transacciones financieras.</p>
      </ContentSection>

    </div>
  );
}