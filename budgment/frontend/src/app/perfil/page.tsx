"use client";

import React, { useState, useEffect } from "react";
import styles from "@/styles/perfil/perfil.module.css";
import ThemeToggle from "../../components/perfil/ThemeToggle";

export default function PerfilPage() {
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
  }, []);

  if (!mounted) return null;

  return (
      <div className={styles.panel}>
        {/* Header */}
        <header className={styles.header}>
          <div className={styles.headerTitle}>
            <span>👤</span> Perfil
          </div>
          <div className={styles.headerConfig}>
            <span>⚙️</span> Configuración
          </div>
        </header>

        {/* Content Grid */}
        <div className={styles.content}>
          {/* Left Section - Avatar y Cuentas */}
          <section className={styles.leftSection}>
            <h2 className={styles.sectionTitle}>Perfil</h2>
            <div className={styles.avatarWrap}>
              <div className={styles.avatar}></div>
            </div>

            <h3 className={styles.subsectionTitle}>Cuentas</h3>
            <div className={styles.accountCard}>
              <div className={styles.accountLabel}>Cuenta principal</div>
              <div className={styles.accountAmount}>$5.000,00</div>
            </div>
          </section>

          {/* Right Section - User Info */}
          <section className={styles.rightSection}>
            <div className={styles.formGroup}>
              <label className={styles.label}>Nombre</label>
              <div className={styles.value}>Alex Gómez</div>
            </div>

            <div className={styles.formGroup}>
              <label className={styles.label}>Nombre de usuario</label>
              <div className={styles.value}>alexgomez</div>
            </div>

            <div className={styles.formGroup}>
              <label className={styles.label}>Correo electrónico</label>
              <div className={styles.value}>alexgomez@email.com</div>
            </div>

            <div className={styles.logoutButton}>
              <a href="#" className={styles.logoutLink}>
                Carrar sesión
              </a>
            </div>
          </section>
        </div>

        {/* Theme Toggle Button */}
        <ThemeToggle />
      </div>
  );
}