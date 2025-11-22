"use client";

import React from "react";
import TransactionCard, { Transaction } from "./TransactionCard";
import styles from "./transactionsgrid.module.css";

export default function TransactionsGrid({
  items,
  view,
}: {
  items: Transaction[];
  view: "grid" | "list";
}) {
  return (
    <div className={view === "grid" ? styles.grid : styles.list}>
      {items.map((it) => (
        <TransactionCard key={it.id} item={it} />
      ))}
    </div>
  );
}