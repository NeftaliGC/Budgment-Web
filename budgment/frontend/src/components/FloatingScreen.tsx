// FloatingScreen.jsx
import React from "react";

type FloatingScreenProps = {
    open: boolean;
    onClose: () => void;
    children?: React.ReactNode;
};

export function FloatingScreen({ open, onClose, children }: FloatingScreenProps) {
    if (!open) return null;

    const overlayStyle: React.CSSProperties = {
        position: "fixed",
        top: 0,
        left: 0,
        width: "100vw",
        height: "100vh",
        background: "rgba(0,0,0,0.4)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        zIndex: 9999,
    };

    const modalStyle: React.CSSProperties = {
        background: "var(--surface)",
        display: "flex",
        flexDirection: "column",
        padding: "1.5rem",
        borderRadius: "8px",
        minWidth: "50%",
        maxWidth: "90%",
        boxShadow: "0 4px 12px rgba(0,0,0,0.3)",
    };

    return (
        <div style={overlayStyle} onClick={onClose}>
        <div style={modalStyle} onClick={(e) => e.stopPropagation()}>
            {children}
        </div>
        </div>
    );
}
