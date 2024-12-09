document.addEventListener("DOMContentLoaded", function() {
    // Cargar el color guardado desde localStorage si existe
    const savedGradient = localStorage.getItem("selectedGradient");
  
    if (savedGradient) {
      // Aplicar el color guardado al navbar
      applyGradient(savedGradient);
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
  