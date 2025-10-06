#!/bin/sh
# Generar JWT secret si no existe
: "${JWT_SECRET:=$(openssl rand -base64 32)}"

# Temporal print the JWT secret for debugging purposes
echo "Starting backend with JWT_SECRET: $JWT_SECRET"

#!/bin/sh
set -e

# Ruta donde la app espera la base de datos
DB_PATH="/app/data/budgment.sqlite"
SEED_FILE="/app/data/seed.sql"

mkdir -p /app/data

# Si no existe la BD, crearla desde el seed
if [ ! -f "$DB_PATH" ]; then
    echo "🧱 Base de datos no encontrada, creando desde seed.sql..."
    sqlite3 "$DB_PATH" < "$SEED_FILE"
    echo "✅ Base de datos creada correctamente."
else
    echo "📁 Base de datos existente, omitiendo creación."
fi

echo "✅ Base de datos lista, iniciando aplicación..."
java -jar /app/app.jar


# Ejecutar la aplicación con el JWT secret
exec java -jar /app/app.jar
