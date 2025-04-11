window.obtenerRespuestasUsuario = function () {
    const respuestas = [];
  
    document.querySelectorAll(".elemento-editable").forEach((el, i) => {
      const tipo = el.dataset.tipo;
  
      if (tipo === "evaluado") {
        const input = el.querySelector("input");
        respuestas.push({ tipo, indice: i, valor: input?.value || "" });
  
      } else if (tipo === "seleccion") {
        const checked = el.querySelector("input[type='radio']:checked");
        respuestas.push({ tipo, indice: i, valor: checked?.nextElementSibling?.textContent || "" });
  
      } else if (tipo === "checkbox") {
        const valor = el.querySelector(".checkbox-cuadro")?.classList.contains("seleccionado") || false;
        respuestas.push({ tipo, indice: i, valor });
  
      } else if (tipo === "desplegable") {
        const select = el.querySelector("select");
        respuestas.push({ tipo, indice: i, valor: select?.value || "" });
  
      } else if (tipo === "join") {
        const joinId = el.dataset.joinSeleccionado || "";
        respuestas.push({ tipo, indice: i, valor: joinId });
      }
    });
  
    return respuestas;
  };
  