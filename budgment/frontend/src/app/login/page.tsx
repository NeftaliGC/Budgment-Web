
import Image from "next/image";
import LoginForm from "@/components/login/LoginForm";
import "@/styles/login/login.css";

export default function LoginPage() {
    return (
        <div className="login-page">
            <Image
                src="/logo.png"
                alt="Login illustration"
                width={100}
                height={100}
            />
            <h1>Budgment</h1>
            <p>Inicia sesión</p>
            <LoginForm />
        </div>
    );
}

