// Función para cambiar el idioma
function changeLanguage(language) {
    // Guardar el idioma en localStorage
    localStorage.setItem('language', language);
    
    // Llamar a la función para actualizar el contenido
    updateLanguage();
}
  
// Función para cargar las traducciones desde el archivo JSON
async function loadTranslations() {
    const language = localStorage.getItem('language') || 'es'; // Por defecto es español
    const response = await fetch('/json/translation.json');
    const translations = await response.json();
    
    return translations[language];
}
  
// Función para actualizar el contenido según el idioma
async function updateLanguage() {
    const translations = await loadTranslations();
    
    // Cambiar el texto de las etiquetas según las traducciones
    document.getElementById('config-title').textContent = translations['config-title'];
    document.getElementById('config-subtitle').textContent = translations['config-subtitle'];
    document.getElementById('language').textContent = translations['language'];
    document.getElementById('language-spanish').textContent = translations['language-spanish'];
    document.getElementById('language-english').textContent = translations['language-english'];
}

// Función para aplicar el color en el navbar
function applyGradient(gradientType) {
    if (gradientType === 'bluepurple') {
        document.documentElement.style.setProperty('--bg-navbar', 'linear-gradient(to right, #5729e1, #9622d5)');
        document.documentElement.style.setProperty('--bg-dropdown-hover', 'linear-gradient(to right, #4b1cba, #7f1d9f)');
        document.documentElement.style.setProperty('--bg-dropdown-item-hover', 'linear-gradient(to right, #4b1cba, #7f1d9f)');
    } else if (gradientType === 'redyellow') {
        document.documentElement.style.setProperty('--bg-navbar', 'linear-gradient(to right, #f0381f, #f8b500)');
        document.documentElement.style.setProperty('--bg-dropdown-hover', 'linear-gradient(to right, #c02e18, #c69300)');
        document.documentElement.style.setProperty('--bg-dropdown-item-hover', 'linear-gradient(to right, #c02e18, #c69300)');
    } else if (gradientType === 'greenTurquoise') {
        document.documentElement.style.setProperty('--bg-navbar', 'linear-gradient(to right, #20c997, #007bff)');
        document.documentElement.style.setProperty('--bg-dropdown-hover', 'linear-gradient(to right, #199874, #0056b3)');
        document.documentElement.style.setProperty('--bg-dropdown-item-hover', 'linear-gradient(to right, #199874, #0056b3)');
    }
}

// Cargar el color guardado desde localStorage si existe
document.addEventListener("DOMContentLoaded", function() {
    const savedGradient = localStorage.getItem("selectedGradient");
    if (savedGradient) {
        // Aplicar el color guardado
        applyGradient(savedGradient);
    }
    
    // Añadir el evento a los botones
    document.querySelectorAll('.btn').forEach(button => {
        button.addEventListener('click', function() {
            const gradientType = this.getAttribute('data-gradient');
            // Aplicar el nuevo color
            applyGradient(gradientType);
            // Guardar el color seleccionado en localStorage
            localStorage.setItem("selectedGradient", gradientType);
        });
    });
});
  
// Ejecutar al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    updateLanguage(); // Actualizar idioma
});
