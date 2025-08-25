
import Link from "next/link";

export default function RegisterForm() {
    return (
        <div className="register-form-container">
            <form className="register-form">
                <input type="text" placeholder="Nombre completo" required />
                <input type="text" placeholder="Nombre de usuario" required />
                <input type="password" placeholder="Contraseña" required />
                <button type="submit">Registrarse</button>
                <p className="login-link">
                    ¿Ya tienes cuenta? <Link href="/login">Inicia sesión</Link>
                </p>
            </form>
        </div>
    );
}
