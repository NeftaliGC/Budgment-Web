"use client";

import React from "react";
import styles from "../../styles/notificaciones/notificationcard.module.css";

export type Notification = {
  id: string;
  type: "success" | "warning";
  title: string;
  description: string;
  time: string;
};

interface NotificationCardProps {
  notification: Notification;
  isRead?: boolean;
}

export default function NotificationCard({
  notification,
  isRead = false,
}: NotificationCardProps) {
  return (
    <div
      className={`${styles.card} ${styles[notification.type]} ${
        isRead ? styles.read : ""
      }`}
    >
      <div className={`${styles.icon} ${styles[notification.type]}`}>
        {notification.type === "success" ? "✓" : "!"}
      </div>
      <div className={styles.content}>
        <div className={styles.title}>{notification.title}</div>
        <div className={styles.description}>{notification.description}</div>
        <div className={styles.time}>{notification.time}</div>
      </div>
    </div>
  );
}