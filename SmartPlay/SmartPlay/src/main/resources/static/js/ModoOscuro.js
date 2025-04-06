document.addEventListener("DOMContentLoaded", function () {
  const toggleButton = document.getElementById("toggle-mode");
  const icono = document.getElementById("icono-modo");

  // Inicializar icono según el modo actual
  if (document.documentElement.classList.contains("dark-mode")) {
    icono.innerHTML = '<i class="fas fa-sun fa-2x"></i>';
  } else {
    icono.innerHTML = '<i class="fas fa-moon fa-2x"></i>';
  }

  toggleButton.addEventListener("click", function (e) {
    e.preventDefault(); // evitar salto de href="#"
    const isDark = document.documentElement.classList.toggle("dark-mode");
    icono.innerHTML = isDark
      ? '<i class="fas fa-sun fa-2x"></i>'
      : '<i class="fas fa-moon fa-2x"></i>';
    localStorage.setItem("theme", isDark ? "dark" : "light");
  });
});
