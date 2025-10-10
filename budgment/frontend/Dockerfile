# ---------- ETAPA 1: Build ----------
FROM node:22.15 AS builder

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos package.json y package-lock.json primero para aprovechar el cache de Docker
COPY package*.json ./

# Instalamos dependencias
RUN npm ci

# Copiamos el resto de los archivos
COPY . .

# Construimos la aplicación Next.js
RUN npm run build

# ---------- ETAPA 2: Producción ----------
FROM node:22.15-alpine AS production

WORKDIR /app

# Copiamos solo lo necesario del builder
COPY --from=builder /app/package*.json ./
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public
COPY --from=builder /app/next.config.ts ./

# Exponemos el puerto en el que Next.js corre por defecto
EXPOSE 3000

# Comando para arrancar la app en producción
CMD ["npm", "start"]
