document.addEventListener("DOMContentLoaded", function () {
  const listaPalabras = document.getElementById("listaPalabras");
  const inputPalabras = document.getElementById("palabras");
  const palabraInput = document.getElementById("palabraInput");

  const palabrasSeleccionadas = new Set();

  // Validar input: solo letras y mayúsculas
  palabraInput.addEventListener("input", function () {
    this.value = this.value.replace(/[^a-zA-Z]/g, "").toUpperCase();
  });

  // Agregar palabra a la lista
  window.agregarPalabra = function () {
    const palabra = palabraInput.value.trim();

    if (palabra.length < 2) {
      alert("La palabra debe tener al menos 2 letras.");
      return;
    }
    if (palabra.length > 10) {
      alert("La palabra no puede tener más de 10 letras.");
      return;
    }
    if (palabrasSeleccionadas.size >= 7) {
      alert("Solo puedes agregar hasta 6 palabras.");
      return;
    }
    if (palabrasSeleccionadas.has(palabra)) {
      alert("Esta palabra ya fue agregada.");
      return;
    }

    // Agregar palabra a la lista visual
    palabrasSeleccionadas.add(palabra);
    const li = crearElementoLista(palabra, "eliminar-palabra");
    listaPalabras.appendChild(li);

    palabraInput.value = "";
    actualizarPalabrasSeleccionadas();
  };

  // Función para actualizar el input oculto con las palabras seleccionadas
  function actualizarPalabrasSeleccionadas() {
    inputPalabras.value = JSON.stringify(Array.from(palabrasSeleccionadas));
    agregarEventosEliminar(
      "eliminar-palabra",
      palabrasSeleccionadas,
      actualizarPalabrasSeleccionadas
    );
  }

  // Función para crear elementos de la lista con "×"
  function crearElementoLista(nombre, claseEliminar) {
    const li = document.createElement("li");
    li.classList.add(
      "list-group-item",
      "d-flex",
      "justify-content-between",
      "align-items-center",
      "item-seleccionado"
    );
    li.innerHTML = `
          ${nombre}
          <span class="${claseEliminar} close-icon" data-nombre="${nombre}">&times;</span>
      `;
    return li;
  }

  // Función para eliminar palabras
  function agregarEventosEliminar(clase, setSeleccionados, actualizarLista) {
    document.querySelectorAll(`.${clase}`).forEach((btn) => {
      btn.addEventListener("click", function () {
        const nombre = btn.getAttribute("data-nombre");
        setSeleccionados.delete(nombre);
        btn.parentElement.remove();
        actualizarLista();
      });
    });
  }

  // Guardar palabras en JSON antes de enviar el formulario
  window.guardarPalabras = function (event) {
    if (palabrasSeleccionadas.size < 2) {
      alert("Debes agregar al menos 2 palabras.");
      event.preventDefault();
      return;
    }
    inputPalabras.value = JSON.stringify(Array.from(palabrasSeleccionadas));
  };
});
document.addEventListener("DOMContentLoaded", function () {
  const listaPalabras = document.getElementById("listaPalabras");
  const inputPalabras = document.getElementById("palabras");
  const palabraInput = document.getElementById("palabraInput");

  const palabrasSeleccionadas = new Set();

  // Validar input: solo letras y mayúsculas
  palabraInput.addEventListener("input", function () {
    this.value = this.value.replace(/[^a-zA-Z]/g, "").toUpperCase();
  });

  // Agregar palabra a la lista
  window.agregarPalabra = function () {
    const palabra = palabraInput.value.trim();

    if (palabra.length < 2) {
      alert("La palabra debe tener al menos 2 letras.");
      return;
    }
    if (palabra.length > 10) {
      alert("La palabra no puede tener más de 10 letras.");
      return;
    }
    if (palabrasSeleccionadas.size >= 6) {
      alert("Solo puedes agregar hasta 6 palabras.");
      return;
    }
    if (palabrasSeleccionadas.has(palabra)) {
      alert("Esta palabra ya fue agregada.");
      return;
    }

    palabrasSeleccionadas.add(palabra);
    const li = crearElementoLista(palabra, "eliminar-palabra");
    listaPalabras.appendChild(li);
    palabraInput.value = "";
    actualizarPalabrasSeleccionadas();
  };

  // Función para actualizar el input oculto con las palabras seleccionadas
  function actualizarPalabrasSeleccionadas() {
    inputPalabras.value = JSON.stringify(Array.from(palabrasSeleccionadas));
    agregarEventosEliminar(
      "eliminar-palabra",
      palabrasSeleccionadas,
      actualizarPalabrasSeleccionadas
    );
  }

  // Función para crear elementos de la lista con "×"
  function crearElementoLista(nombre, claseEliminar) {
    const li = document.createElement("li");
    li.classList.add(
      "list-group-item",
      "d-flex",
      "justify-content-between",
      "align-items-center",
      "item-seleccionado"
    );
    li.innerHTML = `
            ${nombre}
            <span class="${claseEliminar} close-icon" data-nombre="${nombre}">&times;</span>
        `;
    return li;
  }

  // Función para eliminar palabras
  function agregarEventosEliminar(clase, setSeleccionados, actualizarLista) {
    document.querySelectorAll(`.${clase}`).forEach((btn) => {
      btn.addEventListener("click", function () {
        const nombre = btn.getAttribute("data-nombre");
        setSeleccionados.delete(nombre);
        btn.parentElement.remove();
        actualizarLista();
      });
    });
  }

  // Guardar palabras en JSON antes de enviar el formulario
  window.guardarPalabras = function (event) {
    if (palabrasSeleccionadas.size < 2) {
      alert("Debes agregar al menos 2 palabras.");
      event.preventDefault();
      return;
    }
    inputPalabras.value = JSON.stringify(Array.from(palabrasSeleccionadas));
  };

  function cargarPalabrasExistentes() {
    if (listaPalabras.id === "listaPalabrasEdit") {
      try {
        let palabrasExistentes = JSON.parse(inputPalabras.value);
        palabrasExistentes.forEach((palabra) => {
          palabrasSeleccionadas.add(palabra);
          listaPalabras.appendChild(
            crearElementoLista(palabra, "eliminar-palabra")
          );
        });
        actualizarPalabrasSeleccionadas();
      } catch (error) {
        console.error("Error al cargar palabras existentes:", error);
      }
    }
  }

  window.guardarPalabrasEditar = function (event) {
    const palabrasSeleccionadas = JSON.parse(document.getElementById("palabras").value || "[]");

    if (palabrasSeleccionadas.length < 2) {
        alert("Debes agregar al menos 2 palabras antes de guardar.");
        event.preventDefault(); // Evita que el formulario se envíe
        return;
    }
};

  // Llamamos a la función al cargar la página
  cargarPalabrasExistentes();
});
