# ---------- ETAPA 1: Build ----------
FROM node:22.15 AS builder

WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

# ---------- ETAPA 2: Producción ----------
FROM node:22.15-alpine AS production

WORKDIR /app
COPY --from=builder /app/package*.json ./
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public
COPY --from=builder /app/next.config.ts ./
EXPOSE 3000

CMD ["npm", "start"]
