
import Image from "next/image";
import LoginForm from "@/components/login/LoginForm";
import styles from "@/styles/login/login.module.css";

export default function LoginPage() {
    return (
        <div className={styles.loginPage}>
            <Image
                src="/logo.png"
                alt="Login illustration"
                width={100}
                height={100}
            />
            <h1 className={styles.loginTitle}>Budgment</h1>
            <p className={styles.loginMode}>Inicia sesión</p>
            <LoginForm />
        </div>
    );
}

