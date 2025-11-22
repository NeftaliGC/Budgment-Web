"use client";

import React from "react";
import styles from "./transactioncard.module.css";

export type Transaction = {
  id: string;
  title: string;
  date: string;
  amount: string;
  category: string;
};

const TransactionCard: React.FC<{ item: Transaction }> = ({ item }) => {
  return (
    <div className={styles.card}>
      <div className={styles.left}>
        <div className={styles.title}>{item.title}</div>
        <div className={styles.amount}>
          Monto: <strong>{item.amount}</strong>
        </div>
      </div>
      <div className={styles.right}>
        <div className={styles.date}>{item.date}</div>
        <div className={styles.category}>Categoria {item.category}</div>
      </div>
    </div>
  );
};

export default TransactionCard;