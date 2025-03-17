function cargarPagina(button, tipo) {
    let pagina = button.getAttribute("data-page");
    let size;

    if (tipo === "fichas") {
        size = document.getElementById("pageSizeFichas")?.value || "3";
    } else if (tipo === "juegos") {
        size = document.getElementById("pageSizeJuegos")?.value || "3";
    }

    let urlParams = new URLSearchParams(window.location.search);
    if (tipo === "fichas") {
        urlParams.set("pageFichas", pagina);
    } else if (tipo === "juegos") {
        urlParams.set("pageJuegos", pagina);
    }
    urlParams.set("size", size);

    let url = `/cuadernos/ver?${urlParams.toString()}`;

    fetch(url, {
        method: "GET",
        headers: { "X-Requested-With": "XMLHttpRequest" }
    })
    .then(response => response.text())
    .then(html => {
        let parser = new DOMParser();
        let doc = parser.parseFromString(html, "text/html");

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
        } else if (tipo === "juegos") {
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

        // Actualizar eventos en los nuevos botones de paginación
        setTimeout(actualizarBotonesPaginacion, 100);
    })
    .catch(error => console.error("Error cargando la página:", error));
}
