#set text(lang: "es", font: "Fira Code")
#set align(center)
#set page(
  paper: "a4",
  margin: (x: 2.5cm, y: 6cm),
  numbering: none,
  header: align(center)[
    #grid(
      columns: (1fr, 1fr),
      align(left)[
        #image(
          "media/uaemexlogo.png",
          width: 50%,
        )
      ],
      align(right + bottom)[
        #image(
          "media/filogo.png",
          width: 80%
        ),
      ]
    )
  ],
)

#text(size: 13pt)[
  #grid(
    rows: (2em, auto),
    align(left)[
      #par(spacing: 10pt)[
        21-11-2025
      ]
    ],
    align(center)[
      #title()[Universidad Autónoma del Estado de México]
      #title()[Facultad de Ingeniería]
    ]
  )
]

#v(7em)

#text(size: 15pt)[
  Proyecto Tecnologías Computacionales I
  
  #title()[Budgment Web Version]
  
  Profesor: Jose Antonio Hernández Servin
]

#v(1fr)

#text(size: 14pt)[
  Equipo:
  - Dávila Villavicencio Francisco Javier
  - Guía Cruz Fabián Neftaly
  - Rodríguez Nava José Bryan
]



#pagebreak()
#set page(header: none)

#image("media/rubrica.png")

#pagebreak()

#set page(fill: rgb(0, 43, 36))
#grid(
  rows: (auto, 1fr),
  align(center)[
    #image("media/logo.png")
  ],
  align(center)[
    #text(size: 50pt, fill: white)[
      #pad(y: 1em)[
        #strong[Budgment]
      ]
    ]
  ]
)
#pagebreak()

#set page(
  paper: "a4",
  margin: (x:2.5cm, y: 2.5cm),
  fill: none
)
#set heading(numbering: "1.")
#set align(start)
#set text(size: 12pt)
#set par(justify: true, linebreaks: "optimized", first-line-indent: 1em, hanging-indent: 1em)
#show heading.where(level: 2): set align(right)

#title()[#strong[Contenido]]
#outline(title: none, )

#pagebreak()
#counter(page).update(1)
#set page(numbering: "1", number-align: (bottom + right))

= Objetivo <objetivo>
Diseñar y desarrollar una aplicación web (Budgment Web) que permita registrar, clasificar y visualizar transacciones financieras personales mediante una interfaz accesible y reportes interactivos, empleando Next.js en el frontend, Ktor (Kotlin) en el backend y SQLite para persistencia, con soporte de múltiples cuentas y monedas; de modo que el usuario pueda consultar su historial, detectar patrones de gasto y tomar decisiones informadas para optimizar su presupuesto.

= Introducción <introducción>
#strong[Budgment Web] es una aplicación orientada a facilitar la administración de finanzas personales. Muchas personas no llevan un control estructurado de sus ingresos y gastos porque las herramientas existentes son complejas, requieren sincronizaciones con terceros o imponen modelos de suscripción. Estudios recientes muestran que los usuarios valoran mucho la facilidad de uso, reportes visuales claros, y funcionalidades de categorización automática para poder identificar y analizar sus hábitos financieros @finance-app.

Además, investigaciones sobre tecnologías financieras indican que aspectos como la seguridad, la transparencia y el soporte de diversas monedas o cuentas son factores determinantes para adoptar este tipo de aplicaciones @personal-finance.

Budgment busca cubrir esa necesidad ofreciendo una interfaz sencilla y un flujo de trabajo claro: registrar transacciones, clasificarlas (automática o manualmente), y visualizar resúmenes y gráficas que permitan tomar decisiones informadas.

En este trabajo se plantea el problema de cómo transformar el registro financiero cotidiano en información útil y accionable. El enfoque adoptado es pragmático: una arquitectura de servicios desacoplados (frontend en Next.js y backend en Ktor) que garantiza portabilidad y seguridad; persistencia ligera mediante SQLite; y rutinas algorítmicas para clasificación de transacciones y agregación de datos para reportes.

== Términos clave: <términos-clave>
- #strong[Transacción:] registro atómico con campos (monto, categoría, fecha, cuenta, moneda, descripción).

- #strong[Cuenta:] contenedor de transacciones que puede tener moneda propia.

- #strong[Categoría:] etiqueta aplicada a una transacción (p.ej. “Alimentos”, “Transporte”).

- Este documento describe los objetivos, la introducción conceptual, el modelo de datos y los algoritmos principales que sustentan Budgment Web. Las decisiones tecnológicas priorizan mantenibilidad, facilidad de despliegue (Docker Compose) y protección de la API, disponible únicamente a través del frontend.

= Arquitectura y Modelos <arquitectura-y-modelos>
La aplicación web #strong[Budgment] está diseñada bajo una arquitectura de servicios separados, lo que permite mayor mantenibilidad, escalabilidad y seguridad en el flujo de datos.

== Modelo de Datos
#box(
  image("media/modelo_datos.png", height: 4.120138888888889in, width: 6.1375in),
) <una-captura-de-pantalla-de-un-celular-el-contenido-generado-por-ia-puede-ser-incorrecto.modelo-de-datos>

== Arquitectura del proyecto <cambios-realizados-en-el-modelo-para-alcanzar-el-objetivo>
- #strong[Elección de SQLite:] se priorizó una solución ligera y portable para usuarios individuales y despliegue sencillo; reduce complejidad operacional frente a un RDBMS cliente/servidor. (Cambio: del diseño inicial con DB remota a SQLite local por facilidad de despliegue.)

- #strong[Arquitectura separada (Next.js / Ktor):] se optó por separación de responsabilidades para mejorar seguridad y mantenibilidad; además facilita pruebas y escalado. (Cambio: modularizar para proteger API).

- #strong[Soporte de múltiples monedas:] añadido para usuarios con cuentas en diferentes divisas; implica normalización y almacenamiento de tasas al insertar transacciones.

- #strong[Interfaces y UX simplificada:] priorizar la simplicidad para bajar la fricción de registro, con el objetivo de aumentar la frecuencia de uso y facilitar la reducción de gastos.

= Estructura Backend
El backend de Budgment es un servidor REST API desarrollado en Kotlin con el framework Ktor, encargado de manejar la lógica de la aplicación de finanzas personales. Su función principal es recibir solicitudes del frontend, autenticarlas de forma segura y responder con datos procesados en formato JSON. Utiliza SQLite como base de datos embebida mediante Exposed ORM, lo que facilita la manipulación y consulta de información como usuarios, cuentas, transacciones y categorías. Para la autenticación implementa JWT, permitiendo un sistema sin sesiones en el servidor, mientras que las contraseñas se protegen con hashing BCrypt. La arquitectura está modularizada en capas: Routes para definir endpoints HTTP, Services para ejecutar reglas y validaciones de negocio, Repositories para abstraer el acceso a datos con operaciones CRUD y permitir cambiar la base de datos si se requiere, y una capa de seguridad que configura y valida tokens. Incluye funcionalidades operativas como logging automático, identificadores únicos por request para debugging, manejo centralizado de errores, y serialización JSON automática con Content Negotiation y CORS habilitado para comunicarse con el cliente. Es una arquitectura ligera, segura y escalable, diseñada para gestionar múltiples usuarios, controlar saldos y registrar movimientos financieros de manera eficiente.

= Estructura Frontend
El frontend de Budgment está desarrollado con Next.js 15, aprovechando el sistema de App Router, renderizado híbrido (SSR/CSR) y componentes de servidor para optimizar rendimiento. La interfaz se construye con React y componentes personalizados enfocados en usabilidad, simplicidad y consistencia visual. La comunicación con el backend se realiza mediante llamadas Fetch autenticadas con tokens JWT.


= Mapeo de Endpoints
== Notas generales

- Base URL por defecto: `http://localhost:8080`
- Autenticación: JWT (después de `/users/login`)
  - `accessToken` → usar como: `Authorization: Bearer <token>`
  - `refreshToken` → opcional para `/users/refresh`
  - `userId` → devuelto en el body del login
- Formato de montos:
  - Internamente centavos (minor units).
  - `12345` → `123.45`
- Fechas: `YYYY-MM-DD`
- `Content-Type: application/json`

== Usuarios

=== POST /users/sign_in
Registrar usuario.

`Body`
```json
{ "name": "Nombre Apellido", "username": "usuario", "password": "secreto" }
```

`201 Response`
```json
{ "id": "uuid", "name": "...", "username": "...", "createdAt": "...", "updatedAt": "..." }
```

=== POST /users/login
Login. Devuelve accessToken, refreshToken y userId.

`Body`
```json
{ "username": "usuario", "password": "secreto" }
```

`200 Response`
```json
{ "accessToken": "<jwt>", "refreshToken": "<jwt>", "userId": "3b1acdb5-..." }
```

=== GET /users/refresh (auth)
Regenera tokens usando refresh token en sesión.

`200 Response`
```json
{ "accessToken": "<jwt>", "refreshToken": "<jwt>", "userId": "..." }
```

== Cuentas (Accounts)

=== GET /users/{userId}/accounts (auth)
`200 Response`
```json
[
  { "id": "...", "userId": "...", "name": "Cuenta A", "currency": "USD",
    "createdAt": "...", "updatedAt": null, "deletedAt": null }
]
```

=== POST /users/{userId}/accounts (auth)
`Body`
```json
{ "name": "Cuenta A", "currency": "USD", "initialBalance": "150.50" }
```

`201 Response`
```json
{ "id": "...", "userId": "...", "name": "Cuenta A", "currency": "USD" }
```

=== GET /users/{userId}/accounts/{accountId}/balance (auth)
`200 Response`
```json
{
  "id": "<accountid>", "userId": "...", "name": "...",
  "currency": "USD", "balance": "150.50", "balanceMinorUnits": 15050
}
```

=== GET /users/{userId}/accounts/total_balance (auth)
```json
{ "userId": "...", "total": "500.25", "totalMinorUnits": 50025 }
```

=== GET /users/{userId}/accounts/last_month/income (auth)
```json
{ "userId": "...", "total": "1250.00", "totalMinorUnits": 125000 }
```

=== GET /users/{userId}/accounts/last_month/expense (auth)
```json
{ "userId": "...", "total": "420.65", "totalMinorUnits": 42065 }
```

== Transacciones

=== GET /users/{userId}/transactions (auth)
```json
[
  { "id": "...", "userId": "...", "accountId": "...",
    "categoryId": null, "amount": -12345, "currency": "USD",
    "description": "Compra", "date": "2025-11-10" }
]
```

=== POST /users/{userId}/transactions (auth)
```json
{
  "accountId": "<account-id>",
  "amount": -5000,
  "currency": "USD",
  "categoryId": "<category-id>",
  "description": "Pago supermercado",
  "date": "2025-11-28",
  "transferToAccountId": null
}
```

== Categorías

=== POST /users/{userId}/categories (auth)
```json
{ "name": "Comida", "type": "Gasto" }
```

== Presupuestos (Budgets)

=== POST /users/{userId}/budgets (auth)
```json
{
  "name": "Presupuesto Comida",
  "amountLimit": 250000,
  "scope": "Category",
  "periodType": 2,
  "startDate": "2025-11-01",
  "endDate": "2025-11-30"
}
```

= Pantallas
#align(center)[
  #image("media/p2.png", width: 80%)
  #image("media/pInicio.png", width: 80%)
  
]

= Como ejecutar

Budgment es una aplicación compuesta por dos partes:

- *Frontend:* Next.js + React  
- *Backend:* Ktor + Kotlin

Este documento explica cómo ejecutar ambos proyectos localmente.

== 🚀 Requisitos previos

=== Frontend
- Node.js 18+
- npm

=== Backend
- JDK 23 o superior
- Variable de entorno obligatoria: `JWT_SECRET`

== 1. Ejecución del Frontend (Next.js)

=== 📁 Ubicación

budgment/frontend


=== ▶️ Pasos

1. Entrar en la carpeta del frontend:

cd budgment/frontend


2. Instalar dependencias:

npm install


3. Ejecutar el servidor de desarrollo:

npm run dev


=== 🌐 URL de acceso  
Frontend: "http://localhost:3000"

---

== 2. Ejecución del Backend (Ktor)

=== 📁 Ubicación

budgment/backend


== 🔐 Configurar `JWT_SECRET`

El backend requiere una variable de entorno llamada `JWT_SECRET`. Puedes generar un valor seguro así:

=== Linux / macOS

openssl rand -hex 32


O sin openssl:

uuidgen


=== Windows (PowerShell)


=== Windows (CMD)

powershell -command "guid

::NewGuid().ToString()"


---

== 🧩 Establecer la variable de entorno

=== Linux / macOS — Temporal

export JWT_SECRET="tu_secreto_aqui"


=== Linux / macOS — Permanente

echo 'export JWT_SECRET="tu_secreto_aqui"' >> ~/.bashrc
source ~/.bashrc


=== Windows PowerShell — Permanente

setx JWT_SECRET "tu_secreto_aqui"


=== Windows CMD — Permanente

setx JWT_SECRET "tu_secreto_aqui"


=== Windows — Temporal (solo sesión actual)

PowerShell:

- env:JWT_SECRET="tu_secreto_aqui"


CMD:

set JWT_SECRET=tu_secreto_aqui


---

== ▶️ Ejecutar el backend

1. Entrar a la carpeta:

cd budgment/backend


2. Compilar:

./gradlew build

En Windows:

gradlew build


3. Ejecutar:

./gradlew run

En Windows:

gradlew run


=== 🌐 URL de la API  
Backend: "http://localhost:8080"

---

== 🎉 Final

Cuando ambas partes estén ejecutándose:

#align(center)[
  #table(
    columns: 2,
    [
      Componente
    ],
    [
      URL
    ],[Frontend], ["http://localhost:3000"],
    [Backend API], ["http://localhost:8080"]
  )
]

El frontend ya podrá comunicarse correctamente con la API.


= Dockerización
Se tienen tres contenedores que cumplen roles distintos pero complementarios:

[ Navegador ] → (HTTP) → [ nginx-proxy ] → [ frontend-test ] → (API calls) → [ backend-test ]

== nginx-proxy

Escucha en el puerto 80 público.

Es el reverse proxy que recibe todas las peticiones.

Redirige el tráfico al frontend según el dominio o configuración Docker.

== frontend (Next.js)

Corre en puerto interno 3000.

No está expuesto al exterior.

nginx-proxy envía aquí las peticiones del usuario.

Para llamar al backend, usa la red Docker, ej:

http://backend:8080

== backend (Ktor)

Corre en puerto interno 8080.

Solo accesible dentro de la red Docker.

Responde las solicitudes API del frontend.

#pagebreak()
= Publicación

El codigo fuente se encuentra en GitHub en la siguiente URL #link("https://github.com/NeftaliGC/Budgment-Web")

Ademas tambien puede ver la aplicación en producción en: #link("http://budgment.nintech.engineer")

= Referencias <referencias>
#bibliography("bibliography.yml", style: "apa", title: none)