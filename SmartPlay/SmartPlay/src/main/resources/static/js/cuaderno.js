document.addEventListener("DOMContentLoaded", function () {
    const listaFichas = document.getElementById("listaFichas");
    const inputFichas = document.getElementById("fichasSeleccionadas");
    const fichasSeleccionadas = new Set();
  
    // Manejar selección de fichas en el modal
    document.querySelectorAll(".ficha-item").forEach((ficha) => {
      ficha.addEventListener("click", function () {
        const id = ficha.getAttribute("data-id");
  
        if (fichasSeleccionadas.has(id)) {
          fichasSeleccionadas.delete(id);
          ficha.classList.remove("ficha-seleccionada");
        } else {
          fichasSeleccionadas.add(id);
          ficha.classList.add("ficha-seleccionada");
        }
  
        actualizarLista();
      });
    });
  
    // Función para actualizar la lista de fichas seleccionadas
    function actualizarLista() {
      listaFichas.innerHTML = "";
      fichasSeleccionadas.forEach((id) => {
        const ficha = document.querySelector(`.ficha-item[data-id='${id}']`);
        const nombre = ficha.getAttribute("data-nombre");
  
        const li = document.createElement("li");
        li.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-center");
        li.innerHTML = `
          ${nombre}
          <span class="eliminar-ficha" data-id="${id}">&times;</span>
        `;
  
        listaFichas.appendChild(li);
      });
  
      // Actualizar input oculto con los IDs seleccionados
      inputFichas.value = Array.from(fichasSeleccionadas).join(",");
  
      // Agregar eventos a los botones de eliminación
      document.querySelectorAll(".eliminar-ficha").forEach((btn) => {
        btn.addEventListener("click", function () {
          const id = btn.getAttribute("data-id");
          fichasSeleccionadas.delete(id);
  
          // Quitar la clase de selección del modal
          const ficha = document.querySelector(`.ficha-item[data-id='${id}']`);
          if (ficha) ficha.classList.remove("ficha-seleccionada");
  
          actualizarLista();
        });
      });
    }
  });
  