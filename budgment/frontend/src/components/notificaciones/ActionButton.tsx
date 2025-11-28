"use client";

import React from "react";
import styles from "../../styles/notificaciones/actionbutton.module.css";

interface ActionButtonProps {
  label: string;
  icon: string;
  onClick: () => void;
  variant?: "primary" | "secondary";
}

export default function ActionButton({
  label,
  icon,
  onClick,
  variant = "primary",
}: ActionButtonProps) {
  return (
    <button
      className={`${styles.button} ${styles[variant]}`}
      onClick={onClick}
    >
      <span className={styles.icon}>{icon}</span>
      <span>{label}</span>
    </button>
  );
}