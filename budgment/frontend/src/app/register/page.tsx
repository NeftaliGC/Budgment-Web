
import RegisterForm from "@/components/login/RegisterForm";
import Image from "next/image";
import "@/styles/login/register.css";

export default function RegisterPage() {
    return (
        <div className="register-page">
            <Image
                src="/logo.png"
                alt="Register illustration"
                width={100}
                height={100}
            />
            <h1>Budgment</h1>
            <p>Regístrate</p>
            <RegisterForm />
        </div>
    );
}
