import React from "react";

interface DisplayCardProps {
    title: string;
    logo: React.ReactNode; // SVG como componente o elemento
    children: React.ReactNode; // texto con formato
}

const DisplayCard: React.FC<DisplayCardProps> = ({ title, logo, children }) => {
    return (
        <div
        style={{
            width: "100%",
            background: "var(--options-bar)",
            borderRadius: "12px",
            padding: "16px",
            boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
            display: "flex",
            flexDirection: "column",
            gap: "12px",
        }}
        >
        {/* Título */}
        <div
            style={{
                fontSize: "1.1rem",
                fontWeight: 600,
            }}
        >
            {title}
        </div>

        {/* Contenido inferior */}
        <div
            style={{
                display: "flex",
                alignItems: "center",
                gap: "12px",
            }}
        >
            {/* Logo */}
            <div style={{ }}>{logo}</div>

            {/* Texto formateado */}
            <div style={{ flex: 1 }}>{children}</div>
        </div>
        </div>
    );
};

export default DisplayCard;
