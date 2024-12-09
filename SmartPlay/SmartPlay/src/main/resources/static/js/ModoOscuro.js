document.addEventListener('DOMContentLoaded', function() {
    // Cargar el tema guardado del almacenamiento local
    const savedTheme = localStorage.getItem('theme');
    const toggleButton = document.getElementById('toggle-mode');
  
    if (savedTheme === 'dark') {
      document.body.classList.add('dark-mode');
      toggleButton.innerHTML = '<i class="fas fa-sun fa-2x"></i>';
    } else {
      document.body.classList.remove('dark-mode');
      toggleButton.innerHTML = '<i class="fas fa-moon fa-2x"></i>';
    }
  
    // Evento para alternar el tema
    toggleButton.addEventListener('click', function() {
      
      if (document.body.classList.contains('dark-mode')) {
        document.body.classList.remove('dark-mode');
        toggleButton.innerHTML = '<i class="fas fa-moon fa-2x"></i>';
        localStorage.setItem('theme', 'light');
      } else {
        document.body.classList.add('dark-mode');
        toggleButton.innerHTML = '<i class="fas fa-sun fa-2x"></i>'; 
        localStorage.setItem('theme', 'dark');
      }
    });
  });
  