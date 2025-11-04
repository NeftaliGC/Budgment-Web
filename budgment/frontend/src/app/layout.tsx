import "@/styles/globals.css";
import { Geist, Geist_Mono } from "next/font/google";
import LayoutClient from "./LayoutClient"; // importamos el cliente

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata = {
  title: "Budgment Web",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="es">
      <body className={`${geistSans.variable} ${geistMono.variable}`}>
        <LayoutClient>{children}</LayoutClient>
      </body>
    </html>
  );
}
