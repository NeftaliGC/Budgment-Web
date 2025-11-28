"use client";
import { API_BASE } from "@/config/api";
import { useState } from "react";
import Link from "next/link";
import styles from "@/styles/login/login.module.css";

export default function LoginForm() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    interface LoginCredentials {
        username: string;
        password: string;
    }

    async function handleSubmit(e: React.FormEvent<HTMLFormElement>): Promise<void> {
        e.preventDefault();

        try {
            const payload: LoginCredentials = { username, password };

            const res: Response = await fetch(`${API_BASE}/users/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",   // ← MUY IMPORTANTE
                body: JSON.stringify(payload),
            });

            if (!res.ok) {
                setError("Usuario o contraseña incorrectos");
                return;
            }

            // guardar user id en el almacenamiento de sesión

            const data = await res.json();
            sessionStorage.setItem("userId", data.userId);

            // guardar token en el almacenamiento de sesión
            sessionStorage.setItem("token", data.accessToken);
            
            // la cookie HttpOnly ya queda guardada sola
            window.location.href = "/";

        } catch (err) {
            setError(`Error al conectar con el servidor: ${err}`);
        }
    }

    return (
        <div className={styles.loginFormContainer}>
            <form className={styles.loginForm} onSubmit={handleSubmit}>
                <input
                    type="text"
                    placeholder="Usuario"
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

                <button type="submit">Iniciar sesión</button>

                {error && <p className={styles.error}>{error}</p>}

                <p className={styles.forgotPassword}>
                    <Link href="/forgot-password">¿Olvidaste tu contraseña?</Link>
                </p>

                <p className={styles.register}>
                    ¿No tienes cuenta? <Link href="/register">Regístrate</Link>
                </p>
            </form>
        </div>
    );
}
