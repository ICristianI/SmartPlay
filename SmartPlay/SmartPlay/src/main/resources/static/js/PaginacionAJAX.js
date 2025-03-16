function cargarPagina(button, tipo) {
    let pagina = button.getAttribute("data-page");
    let size = tipo === "cuadernos" ? document.getElementById("pageSizeCuadernos").value :
                                       document.getElementById("pageSize").value;
    const urlParams = new URLSearchParams(window.location.search);
    urlParams.set('page', pagina);
    urlParams.set('size', size);

    let url = tipo === "cuadernos" ? `/f/verFicha?${urlParams.toString()}` : `/cuadernos/ver?${urlParams.toString()}`;

    fetch(url, {
        method: "GET",
        headers: { "X-Requested-With": "XMLHttpRequest" }
    })
    .then(response => response.text())
    .then(html => {
        let parser = new DOMParser();
        let doc = parser.parseFromString(html, 'text/html');

        if (tipo === "cuadernos") {
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
        } else {
            let nuevoContenidoFichas = doc.querySelector("#contenedorFichas");
            let contenedorFichas = document.querySelector("#contenedorFichas");
            if (nuevoContenidoFichas && contenedorFichas) {
                contenedorFichas.innerHTML = nuevoContenidoFichas.innerHTML;
            }

            let nuevaPaginacion = doc.querySelector("#navPaginacion");
            let contenedorPaginacion = document.querySelector("#navPaginacion");
            if (nuevaPaginacion && contenedorPaginacion) {
                contenedorPaginacion.innerHTML = nuevaPaginacion.innerHTML;
            }
        }

        // Actualizar eventos en los nuevos botones de paginación
        setTimeout(actualizarBotonesPaginacion, 100);
    })
    .catch(error => console.error('Error cargando la página:', error));
}
