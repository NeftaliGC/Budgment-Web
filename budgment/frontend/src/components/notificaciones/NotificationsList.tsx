"use client";

import React from "react";
import NotificationCard, { Notification } from "./NotificationCard";
import styles from "../../styles/notificaciones/notificationslist.module.css";

interface NotificationsListProps {
  notifications: Notification[];
  markedAsRead: Set<string>;
}

export default function NotificationsList({
  notifications,
  markedAsRead,
}: NotificationsListProps) {
  if (notifications.length === 0) {
    return (
      <div className={styles.emptyState}>
        <div className={styles.emptyIcon}>📭</div>
        <p className={styles.emptyText}>No hay notificaciones</p>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      {notifications.map((notif) => (
        <NotificationCard
          key={notif.id}
          notification={notif}
          isRead={markedAsRead.has(notif.id)}
        />
      ))}
    </div>
  );
}