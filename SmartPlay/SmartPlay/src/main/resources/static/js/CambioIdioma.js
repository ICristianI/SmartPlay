// Función para cambiar el idioma
function changeLanguage(language) {
  localStorage.setItem("language", language);
  updateLanguage();
}

// Función para cargar las traducciones desde el archivo JSON
async function loadTranslations() {
  const language = localStorage.getItem("language") || "es"; // Por defecto es español
  const response = await fetch("/json/translation.json");
  const translations = await response.json();

  return translations[language];
}

// Función para actualizar el contenido según el idioma
async function updateLanguage() {
  const translations = await loadTranslations();

  const elements = [
    { id: "config-title", key: "config-title" },
    { id: "config-menu", key: "config-menu" },
    { id: "config-subtitle", key: "config-subtitle" },
    { id: "language", key: "language" },
    { id: "language-spanish", key: "language-spanish" },
    { id: "language-english", key: "language-english" },
    { id: "files", key: "files" },
    { id: "files-menu", key: "files-menu" },
    { id: "start", key: "start" },
    { id: "start-menu", key: "start-menu" },
    { id: "statistics", key: "statistics" },
    { id: "statistics-menu", key: "statistics-menu" },
    { id: "register", key: "register" },
    { id: "register-menu", key: "register-menu" },
    { id: "register-page", key: "register-page" },
    { id: "login", key: "login" },
    { id: "login-menu", key: "login-menu" },
    { id: "login-page", key: "login-page" },
    { id: "log-out", key: "log-out" },
    { id: "all-rights", key: "all-rights" },
    { id: "email", key: "email" },
    { id: "password", key: "password" },
    { id: "no-account", key: "no-account" },
    { id: "register-here", key: "register-here" },
    { id: "name", key: "name" },
    { id: "age", key: "age" },
    { id: "role", key: "role" },
    { id: "select-role", key: "select-role" },
    { id: "student", key: "student" },
    { id: "teacher", key: "teacher" },
    { id: "yes-account", key: "yes-account" },
    { id: "login2", key: "login2" },
  ];

  elements.forEach(({ id, key }) => {
    const element = document.getElementById(id);
    if (element) {
      element.textContent = translations[key];
    }
  });
}

// Función para aplicar el color en el navbar
function applyGradient(gradientType) {
  if (gradientType === "bluepurple") {
    document.documentElement.style.setProperty(
      "--bg-navbar",
      "linear-gradient(to right, #5729e1, #9622d5)"
    );
    document.documentElement.style.setProperty(
      "--bg-dropdown-hover",
      "linear-gradient(to right, #4b1cba, #7f1d9f)"
    );
    document.documentElement.style.setProperty(
      "--bg-dropdown-item-hover",
      "linear-gradient(to right, #4b1cba, #7f1d9f)"
    );
  } else if (gradientType === "redyellow") {
    document.documentElement.style.setProperty(
      "--bg-navbar",
      "linear-gradient(to right, #f0381f, #f8b500)"
    );
    document.documentElement.style.setProperty(
      "--bg-dropdown-hover",
      "linear-gradient(to right, #c02e18, #c69300)"
    );
    document.documentElement.style.setProperty(
      "--bg-dropdown-item-hover",
      "linear-gradient(to right, #c02e18, #c69300)"
    );
  } else if (gradientType === "greenTurquoise") {
    document.documentElement.style.setProperty(
      "--bg-navbar",
      "linear-gradient(to right, #20c997, #007bff)"
    );
    document.documentElement.style.setProperty(
      "--bg-dropdown-hover",
      "linear-gradient(to right, #199874, #0056b3)"
    );
    document.documentElement.style.setProperty(
      "--bg-dropdown-item-hover",
      "linear-gradient(to right, #199874, #0056b3)"
    );
  }
}

// Cargar el color guardado desde localStorage si existe
document.addEventListener("DOMContentLoaded", function () {
  const savedGradient = localStorage.getItem("selectedGradient");
  if (savedGradient) {
    applyGradient(savedGradient);
  }

  // Añadir el evento a los botones
  document.querySelectorAll(".btn").forEach((button) => {
    button.addEventListener("click", function () {
      const gradientType = this.getAttribute("data-gradient");
      applyGradient(gradientType);
      localStorage.setItem("selectedGradient", gradientType);
    });
  });
});

// Ejecutar al cargar la página
document.addEventListener("DOMContentLoaded", () => {
  updateLanguage();
});
