"use client";
import { API_BASE } from "@/config/api";
import { useState } from "react";
import Link from "next/link";
import styles from "@/styles/login/register.module.css"

export default function RegisterForm() {
    const [name, setName] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    async function handleSubmit(e: React.FormEvent<HTMLFormElement>): Promise<void> {
        e.preventDefault();

        try {
            const res = await fetch(`${API_BASE}/users/sign_in`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include", // ← si el backend crea cookie al registrarse
                body: JSON.stringify({
                    name,
                    username,
                    password,
                }),
            });

            if (!res.ok) {
                const json = await res.json().catch(() => ({}));
                setError(json.error || "Error al registrarse");
                return;
            }

            // registro exitoso → redirige a login o inicio
            window.location.href = "/login";

        } catch (err) {
            setError("Error al conectar con el servidor");
        }
    }

    return (
        <div className={styles.registerFormContainer}>
            <form className={styles.registerForm} onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Nombre completo"
                    required
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                />

                <input
                    type="text"
                    placeholder="Nombre de usuario"
                    required
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                />

                <input
                    type="password"
                    placeholder="Contraseña"
                    required
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <button type="submit">Registrarse</button>

                {error && <p className={styles.error}>{error}</p>}

                <p className={styles.loginLink}>
                    ¿Ya tienes cuenta? <Link href="/login">Inicia sesión</Link>
                </p>
            </form>
        </div>
    );
}
