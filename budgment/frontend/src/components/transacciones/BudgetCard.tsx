"use client";

import React from "react";
import styles from "./budgetcard.module.css";

export type Budget = {
  id: string;
  name: string;
  limit: string;
  date: string;
  percent: number;
};

const BudgetCard: React.FC<{ budget: Budget }> = ({ budget }) => {
  return (
    <div className={styles.card}>
      <div className={styles.left}>
        <div className={styles.title}>{budget.name}</div>
        <div className={styles.limit}>
          Limite: <strong>{budget.limit}</strong>
        </div>
      </div>

      <div className={styles.right}>
        <div className={styles.date}>{budget.date}</div>
        <div className={styles.progressWrap}>
          <div className={styles.progressBar}>
            <div
              className={styles.progress}
              style={{ width: `${budget.percent}%` }}
            />
          </div>
          <div className={styles.percent}>{budget.percent}%</div>
        </div>
      </div>
    </div>
  );
};

export default BudgetCard;