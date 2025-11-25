"use client";

import React, { useState, useEffect } from "react";
import styles from "../../styles/perfil/themetoggle.module.css";

type Theme = "light" | "dark";

export default function ThemeToggle() {
  const [theme, setTheme] = useState<Theme>("light");
  const [mounted, setMounted] = useState(false);

  useEffect(() => {
    setMounted(true);
    // Detectar tema guardado
    const savedTheme = localStorage.getItem("theme") as Theme | null;
    const initialTheme = savedTheme || "light";
    
    setTheme(initialTheme);
    applyTheme(initialTheme);
  }, []);

  const applyTheme = (newTheme: Theme) => {
    const root = document.documentElement;
    
    if (newTheme === "dark") {
      root.style.colorScheme = "dark";
      root.style.setProperty("--bg", "#0d1111");
      root.style.setProperty("--surface", "#1a2625");
      root.style.setProperty("--text", "#f3fff9");
      root.style.setProperty("--foreground", "#f3fff9");
      root.style.setProperty("--action-bar", "#01796f");
      root.style.setProperty("--options-bar", "#01796f");
      root.style.setProperty("--highlight", "#ff9800");
      root.style.setProperty("--card-bg", "#0b5d44");
      root.style.setProperty("--card-text", "#f3fff9");
      root.style.setProperty("--muted", "#8ee5c1");
    } else {
      root.style.colorScheme = "light";
      root.style.setProperty("--bg", "#f2fbf7");
      root.style.setProperty("--surface", "#fffefc");
      root.style.setProperty("--text", "#0d1111");
      root.style.setProperty("--foreground", "#0d1111");
      root.style.setProperty("--action-bar", "#0d7b69");
      root.style.setProperty("--options-bar", "#0d7b69");
      root.style.setProperty("--highlight", "#ffa114");
      root.style.setProperty("--card-bg", "#0e7c69");
      root.style.setProperty("--card-text", "#ffffff");
      root.style.setProperty("--muted", "#2b6b63");
    }
    
    localStorage.setItem("theme", newTheme);
  };

  const toggleTheme = () => {
    const newTheme = theme === "light" ? "dark" : "light";
    setTheme(newTheme);
    applyTheme(newTheme);
  };

  if (!mounted) return null;

  return (
    <button
      className={`${styles.themeToggle} ${styles[theme]}`}
      onClick={toggleTheme}
      aria-label="Toggle theme"
      title={`Cambiar a tema ${theme === "light" ? "oscuro" : "claro"}`}
    >
      {theme === "light" ? "🌙" : "☀️"}
    </button>
  );
}