
import Link from "next/link";

export default function LoginForm() {
    return (
        <div className="login-form-container">
            <form className="login-form">
                <input type="email" placeholder="Usuario" required />
                <input type="password" placeholder="Contraseña" required />
                <button type="submit">Iniciar sesión</button>
                <p className="forgot-password">
                    <Link href="/forgot-password">¿Olvidaste tu contraseña?</Link>
                </p>
                <p className="register">
                    ¿No tienes cuenta? <Link href="/register">Regístrate</Link>
                </p>
            </form>
        </div>
    );
}
