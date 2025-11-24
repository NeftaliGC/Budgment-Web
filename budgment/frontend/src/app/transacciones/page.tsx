"use client";

import React, { useMemo, useState } from "react";
import styles from "@/styles/transacciones/transacciones.module.css";
import SearchBar from "../../components/transacciones/SearchBar";
import TransactionsGrid from "../../components/transacciones/TransactionsGrid";
import BudgetCard from "../../components/transacciones/BudgetCard";
import { TRANSACTIONS_MOCK, BUDGETS_MOCK } from "../../components/transacciones/TransaccionesMocks";

export default function TransaccionesPage(): React.ReactElement {
  const [query, setQuery] = useState("");
  const [view, setView] = useState<"grid" | "list">("grid");
  const [activeFilter, setActiveFilter] = useState<string | null>(null);

  const transactions = useMemo(() => {
    const q = query.trim().toLowerCase();
    return TRANSACTIONS_MOCK.filter((t) => {
      const matchesQuery =
        q === "" ||
        t.title.toLowerCase().includes(q) ||
        t.category.toLowerCase().includes(q) ||
        t.amount.toString().includes(q);
      const matchesFilter = !activeFilter || t.category === activeFilter;
      return matchesQuery && matchesFilter;
    });
  }, [query, activeFilter]);

  const budgets = BUDGETS_MOCK;

  return (
    <main className={styles.page}>
      <div className={styles.panel}>
        <header className={styles.topbar}>
          <div className={styles.filtersRow}>
            <div className={styles.sectionTitle}>
              <span className={styles.titleBadge}>📋</span>
              Buscar Transacción
            </div>
            <div className={styles.sectionTitleRight}>
              <span className={styles.titleBadge}>📑</span>
              Buscar Presupuesto
            </div>
          </div>

          <SearchBar
            value={query}
            onChange={(v) => setQuery(v)}
            onToggleView={() => setView((s) => (s === "grid" ? "list" : "grid"))}
            viewMode={view}
            onFilterClick={(f) => setActiveFilter((cur) => (cur === f ? null : f))}
            activeFilter={activeFilter}
          />
        </header>

        <section className={styles.section}>
          <h3 className={styles.sectionHeader}>Últimas 6 transacciones:</h3>
          <TransactionsGrid items={transactions.slice(0, 6)} view={view} />
        </section>

        <section className={styles.section}>
          <h3 className={styles.sectionHeader}>Mis Presupuestos:</h3>
          <div className={styles.budgetsRow}>
            {budgets.map((b) => (
              <BudgetCard key={b.id} budget={b} />
            ))}
          </div>
        </section>
      </div>
    </main>
  );
}