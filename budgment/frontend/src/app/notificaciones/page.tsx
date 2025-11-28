"use client";

import React, { useState, useEffect } from "react";
import styles from "../../styles/notificaciones/notificaciones.module.css";
import NotificationsList from "../../components/notificaciones/NotificationsList";
import ActionButton from "../../components/notificaciones/ActionButton";

export type Notification = {
    id: string;
    type: "success" | "warning";
    title: string;
    description: string;
    time: string;
};

const NOTIFICATIONS_MOCK: Notification[] = [
    {
        id: "n1",
        type: "success",
        title: "Transacción completada",
        description: "Has completado exitosamente una transacción en el día de hoy.",
        time: "Hace 2 minutos",
    },
    {
        id: "n2",
        type: "warning",
        title: "Gasto significativo detectado",
        description: "Un gasto significativo ha sido identificado en tu cuenta.",
        time: "Hace 2 horas",
    },
    {
        id: "n3",
        type: "success",
        title: "Transacción completada",
        description: "Has completado exitosamente una transacción en el día de ayer.",
        time: "Hace 1 día",
    },
];

export default function NotificacionesPage() {
    const [mounted, setMounted] = useState(false);
    const [notifications, setNotifications] = useState<Notification[]>(
        NOTIFICATIONS_MOCK
    );
    const [markedAsRead, setMarkedAsRead] = useState<Set<string>>(new Set());

    useEffect(() => {
        setMounted(true);
    }, []);

    const handleClean = () => {
        setNotifications([]);
    };

    const handleMarkAsRead = () => {
        const allIds = new Set(notifications.map((n) => n.id));
        setMarkedAsRead(allIds);
    };

    const handleViewAll = () => {
        window.location.href = "/notificaciones/todas";
    };

    if (!mounted) return null;

    return (
        <main className={styles.page}>
            <div className={styles.panel}>
                {/* Header */}
                <header className={styles.header}>
                <span>🔔</span> Mis Notificaciones
                </header>

                {/* Controls */}
                <div className={styles.controls}>
                <ActionButton
                    label="Limpiar"
                    icon="🗑️"
                    onClick={handleClean}
                    variant="primary"
                />
                <ActionButton
                    label="Marcar como leído"
                    icon="✓"
                    onClick={handleMarkAsRead}
                    variant="secondary"
                />
                </div>

                {/* Notifications List */}
                <NotificationsList
                notifications={notifications}
                markedAsRead={markedAsRead}
                />

                {/* View All Button */}
                {notifications.length > 0 && (
                <div className={styles.viewAllButton}>
                    <button className={styles.viewAllLink} onClick={handleViewAll}>
                    Ver todas
                    </button>
                </div>
                )}
            </div>
        </main>
    );
}