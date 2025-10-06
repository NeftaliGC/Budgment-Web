#!/bin/sh
# Generar JWT secret si no existe
: "${JWT_SECRET:=$(openssl rand -base64 32)}"

# Temporal print the JWT secret for debugging purposes
echo "Starting backend with JWT_SECRET: $JWT_SECRET"

# Ejecutar la aplicación con el JWT secret
exec java -jar /app/app.jar
