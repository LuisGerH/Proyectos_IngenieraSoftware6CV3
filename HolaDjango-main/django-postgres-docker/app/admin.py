from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import CustomUser

@admin.register(CustomUser)
class CustomUserAdmin(UserAdmin):
    # Campos a mostrar en la lista de usuarios
    list_display = ('email', 'username', 'first_name', 'last_name', 'phone', 'is_staff', 'date_joined')
    list_filter = ('is_staff', 'is_superuser', 'is_active', 'date_joined')
    
    # Campos de búsqueda
    search_fields = ('email', 'username', 'first_name', 'last_name')
    
    # Ordenamiento
    ordering = ('-date_joined',)
    
    # Campos editables en el formulario de edición
    fieldsets = UserAdmin.fieldsets + (
        ('Información Adicional', {
            'fields': ('phone', 'date_of_birth')
        }),
    )
    
    # Campos para el formulario de creación
    add_fieldsets = UserAdmin.add_fieldsets + (
        ('Información Personal', {
            'fields': ('email', 'first_name', 'last_name', 'phone', 'date_of_birth')
        }),
    )