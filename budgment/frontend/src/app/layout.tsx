
"use client";

import { Geist, Geist_Mono } from "next/font/google";
import InitialSplash from "@/components/InitialSplash";
import { useEffect, useState } from "react";
import "@/styles/globals.css";

const geistSans = Geist({
	variable: "--font-geist-sans",
	subsets: ["latin"],
});

const geistMono = Geist_Mono({
	variable: "--font-geist-mono",
	subsets: ["latin"],
});


export default function RootLayout({
	children,
}: Readonly<{
	children: React.ReactNode;  
}>) {

	const [splashVisible, setSplashVisible] = useState(true);

	useEffect(() => {
		const timer = setTimeout(() => setSplashVisible(false), 1500); // igual que splash showDuration + fade
		return () => clearTimeout(timer);
	}, []);

	return (  
		<html lang="es">
			<body className={`${geistSans.variable} ${geistMono.variable}`}>
				<InitialSplash />
				<div style={{ display: splashVisible ? "none" : "block" }}>
					{children}
				</div>
			</body>
		</html>
	);
}
