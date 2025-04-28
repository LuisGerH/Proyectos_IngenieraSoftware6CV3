document.addEventListener('DOMContentLoaded', function() {
    // Obtener elementos del selector de tema
    const themeOptions = document.querySelectorAll('.theme-option');
    const isUserLoggedIn = document.body.getAttribute('data-user-logged-in') === 'true';
    
    // Token CSRF para peticiones seguras
    const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
    
    // Función para obtener el tema actual del body
    function getCurrentTheme() {
        const bodyClasses = document.body.className.split(' ');
        for (let cls of bodyClasses) {
            if (cls.startsWith('theme-')) {
                return cls.replace('theme-', '');
            }
        }
        return 'system'; // Por defecto
    }
    
    // Actualizar el estado visual del selector según el tema actual
    function updateThemeSelector(theme) {
        themeOptions.forEach(option => {
            const optionTheme = option.getAttribute('data-theme');
            if (optionTheme === theme) {
                option.classList.add('active');
            } else {
                option.classList.remove('active');
            }
        });
    }
    
    // Inicializar el selector con el tema actual
    updateThemeSelector(getCurrentTheme());
    
    // Añadir evento click a cada opción de tema
    themeOptions.forEach(option => {
        option.addEventListener('click', function() {
            const selectedTheme = this.getAttribute('data-theme');
            
            // Actualizar clase en el body
            document.body.className = document.body.className.replace(/theme-\w+/, '');
            document.body.classList.add('theme-' + selectedTheme);
            
            // Actualizar la UI del selector
            updateThemeSelector(selectedTheme);
            
            // Si el usuario está autenticado, guardar su preferencia
            if (isUserLoggedIn) {
                // Enviar petición al servidor para guardar preferencia
                const formData = new FormData();
                formData.append('theme', selectedTheme);
                
                fetch('/cambiar-tema', {
                    method: 'POST',
                    headers: {
                        [csrfHeader]: csrfToken
                    },
                    body: formData,
                    credentials: 'same-origin'
                })
                .then(response => {
                    if (!response.ok) {
                        console.error('Error al guardar preferencia de tema');
                    }
                })
                .catch(error => {
                    console.error('Error en la petición:', error);
                });
            } else {
                // Si no está autenticado, guardar en localStorage
                localStorage.setItem('preferredTheme', selectedTheme);
            }
        });
    });
    
    // Cargar tema desde localStorage si el usuario no está autenticado
    if (!isUserLoggedIn) {
        const savedTheme = localStorage.getItem('preferredTheme');
        if (savedTheme) {
            document.body.className = document.body.className.replace(/theme-\w+/, '');
            document.body.classList.add('theme-' + savedTheme);
            updateThemeSelector(savedTheme);
        }
    }
});