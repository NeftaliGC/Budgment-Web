"use client";

import { useEffect, useState } from "react";
import InitialSplash from "@/components/InitialSplash";
import Sidebar from "@/components/Sidebar/Sidebar";

export default function LayoutClient({
  children,
}: {
  children: React.ReactNode;
}) {
  const [splashVisible, setSplashVisible] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => setSplashVisible(false), 1500);
    return () => clearTimeout(timer);
  }, []);

  if (splashVisible) {
    return <InitialSplash />;
  }

  return (
    <div
      style={{
        display: "flex",
        height: "100vh", // <- importante: limita a la altura visible
        overflow: "hidden", // <- evita scrolls innecesarios
        backgroundColor: "#002b24ff",
      }}
    >
      <Sidebar />
      <main
        style={{
          flex: 1,
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          overflow: "hidden", // <- también bloquea el scroll interno
        }}
      >
        <div
          style={{
            backgroundColor: "#f3fff9ff",
            width: "98%",
            height: "98%",
            borderRadius: "20px",
            overflow: "hidden", // <- permite scroll solo dentro del div si hay mucho contenido
          }}
        >
          {children}
        </div>
      </main>
    </div>
  );
}
