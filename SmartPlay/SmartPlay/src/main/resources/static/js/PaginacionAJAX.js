function cargarPagina(button, tipo) {
  let pagina = button.getAttribute("data-page");
  let size;

  // Obtener el tamaño de página adecuado
  if (tipo === "fichas") {
    size = document.getElementById("pageSizeFichas")?.value || "3";
  } else if (tipo === "juegos") {
    size = document.getElementById("pageSizeJuegos")?.value || "3";
  } else if (tipo === "cuadernos") {
    size = document.getElementById("pageSizeCuadernos")?.value || "3";
  } else if (tipo === "cuadernos2") {
    size = document.getElementById("pageSizeCuadernos")?.value || "3";
  }

  let urlParams = new URLSearchParams(window.location.search);
  if (tipo === "fichas") {
    urlParams.set("pageFichas", pagina);
  } else if (tipo === "juegos") {
    urlParams.set("pageJuegos", pagina);
  } else if (tipo === "cuadernos") {
    urlParams.set("pageCuadernos", pagina);
  } else if (tipo === "cuadernos2") {
    urlParams.set("pageCuadernos", pagina);
  }
  urlParams.set("size", size);

  let url;
  if (tipo === "cuadernos") {
      url = `/f/verFicha?${urlParams.toString()}`;
  } else if (tipo === "cuadernos2") {
      url = `/ahorcado/ver?${urlParams.toString()}`;
  } else if (tipo === "fichas") {
      url = `/cuadernos/ver?${urlParams.toString()}`;
  } else if (tipo === "juegos") {
      url = `/cuadernos/ver?${urlParams.toString()}`;
  }

  fetch(url, {
    method: "GET",
    headers: { "X-Requested-With": "XMLHttpRequest" },
  })
    .then((response) => response.text())
    .then((html) => {
      let parser = new DOMParser();
      let doc = parser.parseFromString(html, "text/html");

      // Actualizar fichas
      if (tipo === "fichas") {
        let nuevoContenidoFichas = doc.querySelector("#contenedorFichas");
        let contenedorFichas = document.querySelector("#contenedorFichas");
        if (nuevoContenidoFichas && contenedorFichas) {
          contenedorFichas.innerHTML = nuevoContenidoFichas.innerHTML;
        }

        let nuevaPaginacionFichas = doc.querySelector("#navPaginacionFichas");
        let contenedorPaginacionFichas = document.querySelector("#navPaginacionFichas");
        if (nuevaPaginacionFichas && contenedorPaginacionFichas) {
          contenedorPaginacionFichas.innerHTML = nuevaPaginacionFichas.innerHTML;
        }
      } 
      
      // Actualizar juegos
      else if (tipo === "juegos") {
        let nuevoContenidoJuegos = doc.querySelector("#contenedorJuegos");
        let contenedorJuegos = document.querySelector("#contenedorJuegos");
        if (nuevoContenidoJuegos && contenedorJuegos) {
          contenedorJuegos.innerHTML = nuevoContenidoJuegos.innerHTML;
        }

        let nuevaPaginacionJuegos = doc.querySelector("#navPaginacionJuegos");
        let contenedorPaginacionJuegos = document.querySelector("#navPaginacionJuegos");
        if (nuevaPaginacionJuegos && contenedorPaginacionJuegos) {
          contenedorPaginacionJuegos.innerHTML = nuevaPaginacionJuegos.innerHTML;
        }
      } 
      
      // Actualizar cuadernos
      else if (tipo === "cuadernos" || tipo === "cuadernos2") {
        let nuevoContenidoCuadernos = doc.querySelector("#contenedorCuadernos");
        let contenedorCuadernos = document.querySelector("#contenedorCuadernos");
        if (nuevoContenidoCuadernos && contenedorCuadernos) {
          contenedorCuadernos.innerHTML = nuevoContenidoCuadernos.innerHTML;
        }

        let nuevaPaginacionCuadernos = doc.querySelector("#navPaginacionCuadernos");
        let contenedorPaginacionCuadernos = document.querySelector("#navPaginacionCuadernos");
        if (nuevaPaginacionCuadernos && contenedorPaginacionCuadernos) {
          contenedorPaginacionCuadernos.innerHTML = nuevaPaginacionCuadernos.innerHTML;
        }
      }

      // Volver a asignar eventos a los nuevos botones de paginación
      actualizarBotonesPaginacion();
    })
    .catch((error) => console.error("Error cargando la página:", error));
}

//  Función para volver a asignar eventos a los botones de paginación
function actualizarBotonesPaginacion() {
  document.querySelectorAll(".pagination .page-link").forEach((button) => {
    button.onclick = function () {
      let tipo = button.closest("nav").id.replace("navPaginacion", "").toLowerCase();
      cargarPagina(this, tipo);
    };
  });
}

// Ejecutar al cargar la página
document.addEventListener("DOMContentLoaded", actualizarBotonesPaginacion);
