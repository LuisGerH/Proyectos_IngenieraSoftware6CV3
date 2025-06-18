from django.shortcuts import render, redirect
from django.contrib.auth import login, authenticate
from django.contrib.auth.decorators import login_required
from django.contrib import messages
from django.http import HttpResponse, JsonResponse
from .forms import CustomUserRegistrationForm
import requests
import json

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

@login_required
def anime_search_view(request):
    """Vista para búsqueda de anime usando la API de Jikan"""
    animes = []
    query = ''
    error_message = ''
    
    if request.method == 'GET' and 'q' in request.GET:
        query = request.GET.get('q', '').strip()
        
        if query:
            try:
                # Hacer petición a la API de Jikan
                url = f'https://api.jikan.moe/v4/anime'
                params = {
                    'q': query,
                    'limit': 20  # Limitar a 20 resultados por página
                }
                
                response = requests.get(url, params=params, timeout=10)
                
                if response.status_code == 200:
                    data = response.json()
                    animes = data.get('data', [])
                    
                    if not animes:
                        error_message = f'No se encontraron animes para "{query}"'
                else:
                    error_message = 'Error al conectar con la API de anime. Intenta más tarde.'
                    
            except requests.exceptions.Timeout:
                error_message = 'La búsqueda tardó demasiado. Intenta con un término más específico.'
            except requests.exceptions.RequestException:
                error_message = 'Error de conexión. Verifica tu conexión a internet.'
            except Exception as e:
                error_message = 'Ocurrió un error inesperado durante la búsqueda.'
    
    context = {
        'animes': animes,
        'query': query,
        'error_message': error_message,
        'user': request.user
    }
    
    return render(request, 'anime_search.html', context)

@login_required
def anime_detail_view(request, anime_id):
    """Vista para mostrar detalles de un anime específico"""
    anime = None
    error_message = ''
    
    try:
        url = f'https://api.jikan.moe/v4/anime/{anime_id}'
        response = requests.get(url, timeout=10)
        
        if response.status_code == 200:
            data = response.json()
            anime = data.get('data')
        else:
            error_message = 'No se pudo obtener la información del anime.'
            
    except requests.exceptions.Timeout:
        error_message = 'La búsqueda tardó demasiado. Intenta más tarde.'
    except requests.exceptions.RequestException:
        error_message = 'Error de conexión. Verifica tu conexión a internet.'
    except Exception as e:
        error_message = 'Ocurrió un error inesperado.'
    
    context = {
        'anime': anime,
        'error_message': error_message,
        'user': request.user
    }
    
    return render(request, 'anime_detail.html', context)

def success_view(request):
    """Vista de éxito después del registro"""
    return render(request, 'registration/success.html')