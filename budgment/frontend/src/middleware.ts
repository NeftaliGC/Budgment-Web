import { NextResponse, NextRequest } from "next/server";

export function middleware(req: NextRequest) {
    const token = req.cookies.get("ACCESS_TOKEN")?.value;
    const path = req.nextUrl.pathname;

    const isAuthPath = path === "/login";
    const isProtected =
        path.startsWith("/inicio") ||
        path.startsWith("/transacciones") ||
        path.startsWith("/estadisticas") ||
        path.startsWith("/notificaciones") ||
        path.startsWith("/perfil");

    if (!token && isProtected) {
        const loginUrl = new URL("/login", req.url);
        return NextResponse.redirect(loginUrl);
    }

    if (token && isAuthPath) {
        const inicioUrl = new URL("/inicio", req.url);
        return NextResponse.redirect(inicioUrl);
    }

    return NextResponse.next();
}

export const config = {
    matcher: [
        "/inicio", 
        "/transacciones", 
        "/estadisticas", 
        "/notificaciones", 
        "/perfil"
    ],
};
