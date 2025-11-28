import React from "react";
import styles from "@/styles/actionbar/actionbar-buttons.module.css";

type Action = {
    label: React.ReactNode;
    onClick?: (e?: React.MouseEvent<HTMLButtonElement>) => void;
};

export function ActionBarList({
    actions = [],
    style = {},
}: {
    actions?: Action[];
    style?: React.CSSProperties;
}) {
    const baseStyle: React.CSSProperties = {
        display: "flex",
        gap: "0.5rem",
        flexDirection: "row",
        justifyContent: "space-around",
        padding: "0.5rem",
        background: "var(--options-bar)",
        border: "5px solid var(--surface)",
        position: "sticky",
        margin: "1rem",
        borderRadius: "1rem",
        top: 0,
        zIndex: 10,
        ...style,
    };

    return (
        <div style={baseStyle}>
        {actions.map((action, i) => (
            <button key={i} onClick={action.onClick} className={styles.optionButton}>
            {action.label}
            </button>
        ))}
        </div>
    );
}
