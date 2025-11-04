// src/components/Sidebar/Sidebar.tsx
"use client";

import Link from "next/link";
import Image from "next/image";
import styles from "./sidebar.module.css";
import SidebarItem from "./SidebarItem";

export default function Sidebar() {
  return (
    <aside className={styles.sidebar}>
      <div className={styles.logoWrap}>
        <Image src="/logo.png" alt="Budgment Web" width={48} height={48} />
        <span className={styles.logoText}>Budgment Web</span>
      </div>

      <nav className={styles.nav}>
        <SidebarItem href="/inicio" icon="/Home.svg" label="Inicio" />
        <SidebarItem href="/transacciones" icon="/transacciones.svg" label="Transacciones" />
        <SidebarItem href="/estadisticas" icon="/estadisticas.svg" label="Estadísticas" />
        <SidebarItem href="/notificaciones" icon="/notificacion.svg" label="Notificaciones" />
        <SidebarItem href="/perfil" icon="/perfil.svg" label="Perfil" />
      </nav>

      <div className={styles.footer}>
        {/* aquí puedes poner versión, user avatar, logout */}
      </div>
    </aside>
  );
}
