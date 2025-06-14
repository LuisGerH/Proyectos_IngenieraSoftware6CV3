# models.py actualizado
from django.db import models
from django.contrib.auth.models import AbstractUser

class CustomUser(AbstractUser):
    email = models.EmailField(unique=True)
    first_name = models.CharField(max_length=30)
    last_name = models.CharField(max_length=30)
    phone = models.CharField(max_length=15, blank=True, null=True)
    date_of_birth = models.DateField(blank=True, null=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    # CAMBIADO: Usar username como campo de login (comportamiento estándar de Django)
    # USERNAME_FIELD = 'username'  # Esta es la configuración por defecto, no necesitas especificarla
    REQUIRED_FIELDS = ['email', 'first_name', 'last_name']  # Campos requeridos además del USERNAME_FIELD
    
    def __str__(self):
        return f"{self.first_name} {self.last_name} ({self.username})"
    
    class Meta:
        verbose_name = "Usuario"
        verbose_name_plural = "Usuarios"