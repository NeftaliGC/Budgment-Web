import Fondo from "@/components/Fondo";
import '@/styles/globals.css';

export default function InicioPages() {
    return Fondo({ children: <h1>Hola Mundo</h1>, color: "red" });
}