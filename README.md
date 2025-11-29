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

## Budgment — Guía de Ejecución

Budgment es una aplicación compuesta por dos partes:

* **Frontend:** Next.js + React
* **Backend:** Ktor + Kotlin

Este documento explica cómo ejecutar ambos proyectos localmente.

---

## 🚀 Requisitos previos

### Frontend

* Node.js 18+
* npm

### Backend

* **JDK 23 o superior**
* Variable de entorno obligatoria: `JWT_SECRET`

---

# 1. Ejecución del Frontend (Next.js)

### 📁 Ubicación

```
budgment/frontend
```

### ▶️ Pasos

1. Entrar en la carpeta del frontend:

   ```sh
   cd budgment/frontend
   ```

2. Instalar dependencias:

   ```sh
   npm install
   ```

3. Ejecutar el servidor de desarrollo:

   ```sh
   npm run dev
   ```

### 🌐 URL de acceso

**Frontend:** [http://localhost:3000](http://localhost:3000)

---

# 2. Ejecución del Backend (Ktor)

### 📁 Ubicación

```
budgment/backend
```

## 🔐 Configurar `JWT_SECRET`

El backend requiere una variable de entorno llamada **JWT_SECRET**. Puedes generar un valor seguro así:

### Linux / macOS

```sh
openssl rand -hex 32
```

O sin openssl:

```sh
uuidgen
```

### Windows (PowerShell)

```powershell
[guid]::NewGuid().ToString()
```

### Windows (CMD)

```cmd
powershell -command "[guid]::NewGuid().ToString()"
```

---

## 🧩 Establecer la variable de entorno

### Linux / macOS — Temporal

```sh
export JWT_SECRET="tu_secreto_aqui"
```

### Linux / macOS — Permanente

```sh
echo 'export JWT_SECRET="tu_secreto_aqui"' >> ~/.bashrc
source ~/.bashrc
```

### Windows PowerShell — Permanente

```powershell
setx JWT_SECRET "tu_secreto_aqui"
```

### Windows CMD — Permanente

```cmd
setx JWT_SECRET "tu_secreto_aqui"
```

### Windows — Temporal (solo sesión actual)

PowerShell:

```powershell
$env:JWT_SECRET="tu_secreto_aqui"
```

CMD:

```cmd
set JWT_SECRET=tu_secreto_aqui
```

---

## ▶️ Ejecutar el backend

1. Entrar a la carpeta:

   ```sh
   cd budgment/backend
   ```

2. Compilar:

   ```sh
   ./gradlew build
   ```

   En Windows:

   ```cmd
   gradlew build
   ```

3. Ejecutar:

   ```sh
   ./gradlew run
   ```

   En Windows:

   ```cmd
   gradlew run
   ```

### 🌐 URL de la API

**Backend:** [http://localhost:8080](http://localhost:8080)

---

# 🎉 Final

Cuando ambas partes estén ejecutándose:

| Componente      | URL                                            |
| --------------- | ---------------------------------------------- |
| **Frontend**    | [http://localhost:3000](http://localhost:3000) |
| **Backend API** | [http://localhost:8080](http://localhost:8080) |

El frontend ya podrá comunicarse correctamente con la API.

---

