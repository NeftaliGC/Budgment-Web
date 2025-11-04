// src/components/Sidebar/SidebarItem.tsx
"use client";

import Link from "next/link";
import Image from "next/image";
import styles from "./sidebar.module.css";

type Props = {
  href: string;
  icon: string;
  label: string;
};

export default function SidebarItem({ href, icon, label }: Props) {
  return (
    <Link href={href} className={styles.item}>
      <div className={styles.iconWrap}>
        <Image src={icon} alt={label} width={24} height={24} />
      </div>
      <span className={styles.label}>{label}</span>
    </Link>
  );
}
