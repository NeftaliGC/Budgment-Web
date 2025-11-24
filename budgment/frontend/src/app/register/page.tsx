
import RegisterForm from "@/components/login/RegisterForm";
import Image from "next/image";
import styles from "@/styles/login/register.module.css";

export default function RegisterPage() {
    return (
        <div className={styles.registerPage}>
            <Image
                src="/logo.png"
                alt="Register illustration"
                width={100}
                height={100}
            />
            <h1 className={styles.registerTitle}>Budgment</h1>
            <p className={styles.registerMode}>Regístrate</p>
            <RegisterForm />
        </div>
    );
}
