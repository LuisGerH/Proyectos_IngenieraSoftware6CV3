from django.shortcuts import render, redirect
from django.contrib.auth import login, authenticate
from django.contrib.auth.decorators import login_required
from django.contrib import messages
from django.http import HttpResponse
from .forms import CustomUserRegistrationForm

def index(request):
    """Vista principal - ahora con template"""
    return render(request, 'index.html')

def register_view(request):
    """Vista para el registro de usuarios"""
    if request.method == 'POST':
        form = CustomUserRegistrationForm(request.POST)
        if form.is_valid():
            user = form.save()
            # Autenticar y loguear al usuario automáticamente
            username = form.cleaned_data.get('username')
            password = form.cleaned_data.get('password1')
            user = authenticate(username=username, password=password)
            if user:
                login(request, user)
                messages.success(request, f'¡Bienvenido {user.first_name}! Tu cuenta ha sido creada exitosamente.')
                return redirect('dashboard')
            else:
                messages.success(request, 'Cuenta creada exitosamente. Por favor inicia sesión.')
                return redirect('login')
        else:
            messages.error(request, 'Por favor corrige los errores en el formulario.')
    else:
        form = CustomUserRegistrationForm()
    
    return render(request, 'registration/register.html', {'form': form})

@login_required
def dashboard_view(request):
    """Vista del dashboard para usuarios autenticados"""
    return render(request, 'dashboard.html', {
        'user': request.user
    })

def success_view(request):
    """Vista de éxito después del registro"""
    return render(request, 'registration/success.html')