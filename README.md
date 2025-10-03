# Budgment - Web Application
Budgment Web es una aplicación web diseñada para ayudar a los usuarios a gestionar sus finanzas personales de manera eficiente. La aplicación permite a los usuarios registrar ingresos y gastos, categorizar transacciones, y visualizar informes financieros detallados.

Este proyecto academico es una version web simplificada de la aplicacion movil *Budgment*, propiedad intelectual de [Fabian Neftaly Guia Cruz (NeftaliGC)](https://github.com/NeftaliGC). Se comparte unicamente con fines academicos. Todos los derechos comerciales y de marca pertenecen al autor original. Leer el archivo [LICENCE.md](./LICENCE.md) para mas detalles.

## Características Principales
- Registro de ingresos y gastos.
- Categorización de transacciones.
- Visualización de informes financieros.
- Registro manual de cuentas bancarias y dinero en efectivo (mas simple que un estado de cuenta).

## Tecnologías Utilizadas
- Frontend: Next.js 15, React con TypeScript
- Backend: Ktor con Kotlin
- Base de Datos: SQLite

## Instalación y Configuración
> Debes tener instalado Node.js y npm para el frontend, y JDK 23 para el backend.
1. Clona este repositorio:
2. ```bash
   git clone https://github.com/NeftaliGC/Budgment-Web.git
   cd Budgment-Web
   cd budgment
   cd frontend
   npm install
   npm run dev
   cd ..
   cd backend
   ./gradlew build
   ./gradlew run
   ```
