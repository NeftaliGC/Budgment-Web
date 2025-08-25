// src/components/InitialSplash.tsx
"use client";

import { useEffect, useState } from "react";
import Image from "next/image";
import "@/styles/InitialSplash.css";

export default function InitialSplash() {
    // iniciar en false para evitar mismatch SSR/CSR
    const [visible, setVisible] = useState(false);
    const [fading, setFading] = useState(false);

    useEffect(() => {
        // Si ya se mostró en esta sesión, no hacemos nada
        if (sessionStorage.getItem("splashShown")) return;

        // Mostrar splash ahora que estamos en cliente
        setVisible(true);

        const showDuration = 2200; // ms que se ve el splash antes de empezar a fade
        const fadeDuration = 300;  // ms del fade

        const t1 = window.setTimeout(() => setFading(true), showDuration);
        const t2 = window.setTimeout(() => {
        setVisible(false);
        sessionStorage.setItem("splashShown", "1");
        }, showDuration + fadeDuration);

        return () => {
        clearTimeout(t1);   
        clearTimeout(t2);
        };
    }, []);

    if (!visible) return null;

    return (
        <div className={`splash-overlay ${fading ? "fade" : ""}`} role="dialog" aria-labelledby="splash-title" aria-modal="true">
            <div className="splash-card" aria-hidden={fading}>
                {/* Coloca tu logo en public/logo.png o ajusta la ruta */}
                <Image src="/logo.png" alt="Logo del sitio" className="splash-logo" width={300} height={300} />
                <h1 className="splash-title">Budgment</h1>

                {/* Spinner accesible */}
                <div className="spinner" role="status" aria-label="Cargando"></div>
            </div>
        </div>
    );
}
