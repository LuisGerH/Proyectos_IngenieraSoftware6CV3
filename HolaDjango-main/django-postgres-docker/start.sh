#!/bin/bash

# entrypoint.sh - Script para automatizar el inicio de Django

echo "🚀 Iniciando aplicación Django..."

# Con depends_on y healthcheck, PostgreSQL ya está listo
echo "✅ PostgreSQL está listo (verificado por Docker healthcheck)!"

# Crear migraciones para todos los apps
echo "📝 Creando migraciones..."
python manage.py makemigrations

# Aplicar migraciones automáticamente
echo "📊 Aplicando migraciones de base de datos..."
python manage.py migrate

# Verificar si hay un superusuario, si no crear uno
echo "👤 Verificando superusuario..."
python manage.py shell << 'EOF'
from django.contrib.auth import get_user_model
User = get_user_model()
try:
    if not User.objects.filter(is_superuser=True).exists():
        print("Creando superusuario por defecto...")
        User.objects.create_superuser('admin', 'admin@gmail.com', 'admin123')
        print("Superusuario creado: admin@gmail.com / admin123")
    else:
        print("Ya existe un superusuario")
except Exception as e:
    print(f"Error al crear superusuario: {e}")
EOF

# Recolectar archivos estáticos (opcional)
echo "📁 Recolectando archivos estáticos..."
python manage.py collectstatic --noinput

# Iniciar el servidor
echo "🌟 Iniciando servidor Django..."
python manage.py runserver 0.0.0.0:8080