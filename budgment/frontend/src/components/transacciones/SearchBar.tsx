"use client";

import React from "react";
import styles from "./searchbar.module.css";

type Props = {
  value: string;
  onChange: (v: string) => void;
  onToggleView: () => void;
  viewMode: "grid" | "list";
  onFilterClick: (f: string) => void;
  activeFilter: string | null;
};

const SearchBar: React.FC<Props> = ({
  value,
  onChange,
  onToggleView,
  viewMode,
  onFilterClick,
  activeFilter,
}) => {
  return (
    <div className={styles.searchRow}>
      <div className={styles.inputWrap}>
        <input
          placeholder="Buscar transacción..."
          value={value}
          onChange={(e) => onChange(e.target.value)}
          className={styles.input}
        />
      </div>

      <div className={styles.actions}>
        <button className={styles.btnPrimary}>Buscar</button>
        <button className={styles.btn}>Filtro 1</button>
        <button className={styles.btn}>Filtro 2</button>
        <div className={styles.filtersInline}>
          <button
            className={`${styles.filterBtn} ${
              activeFilter === "Categoria 1" ? styles.active : ""
            }`}
            onClick={() => onFilterClick("Categoria 1")}
          >
            Categoria 1
          </button>
          <button
            className={`${styles.filterBtn} ${
              activeFilter === "Categoria 2" ? styles.active : ""
            }`}
            onClick={() => onFilterClick("Categoria 2")}
          >
            Categoria 2
          </button>
        </div>
        <button
          className={styles.viewToggle}
          onClick={onToggleView}
          aria-label="Toggle view"
        >
          {viewMode === "grid" ? "🔲" : "📋"}
        </button>
      </div>
    </div>
  );
};

export default SearchBar;