import React from "react";

type LayoutMode = "grid" | "list";

interface ActionButtonProps {
    enabled?: boolean;
    icon?: React.ReactNode;
    text?: string;
    onClick?: () => void;
}

interface ContentSectionProps {
    title: string;
    layout?: LayoutMode;
    columns?: number;
    gap?: string;
    children: React.ReactNode;

    actionButton?: ActionButtonProps; // nuevo
}

const ContentSection: React.FC<ContentSectionProps> = ({
    title,
    layout = "list",
    columns = 3,
    gap = "1rem",
    children,
    actionButton,
}) => {
    const containerStyle: React.CSSProperties =
    layout === "grid"
        ? {
            display: "grid",
            gridTemplateColumns: `repeat(${columns}, 1fr)`,
            gap,
        }
    : {
        display: "flex",
        flexDirection: "column",
        gap,
    };

return (
    <div style={{ background: "transparent", width: "100%", padding: "1.5rem"}}>
      {/* Encabezado: botón + título */}
        <div
        style={{
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
            gap: "12px",
            marginBottom: "0.75rem",
        }}
    >
        {/* Título */}
        <h2 style={{ margin: 0 }}>{title}</h2>

        {/* Botón solo si enabled === true */}
        {actionButton?.enabled && (
            <button
                onClick={actionButton.onClick}
                style={{
                    display: "flex",
                    alignItems: "center",
                    gap: "6px",
                    padding: "6px 10px",
                    background: "var(--options-bar)",
                    borderRadius: "6px",
                    cursor: "pointer",
                }}
            >
                {actionButton.icon && (
                    <span style={{ display: "flex" }}>{actionButton.icon}</span>
                )}
                    {actionButton.text && <span>{actionButton.text}</span>}
            </button>
        )}

    </div>
        {/* Contenido */}
        <div style={containerStyle}>{children}</div>
    </div>
    );
};

export default ContentSection;
