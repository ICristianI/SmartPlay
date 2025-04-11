document.addEventListener("DOMContentLoaded", () => {
    const respuestasRaw = document.getElementById("respuestasUsuarioRaw");
    if (!respuestasRaw) return;
  
    const respuestas = JSON.parse(respuestasRaw.textContent);
    const elementos = document.querySelectorAll(".elemento-editable");
  
    respuestas.forEach(resp => {
      const el = elementos[resp.indice];
      if (!el) return;
  
      switch (resp.tipo) {
        case "evaluado":
          const input = el.querySelector("textarea");
          if (input) input.value = resp.valor;
          break;
  
        case "seleccion":
          const opciones = el.querySelectorAll("input[type='radio']");
          opciones.forEach((radio, i) => {
            const label = radio.nextElementSibling;
            if (label?.textContent.trim() === resp.valor) {
              radio.checked = true;
            }
          });
          break;
  
        case "checkbox":
          const checkbox = el.querySelector("div.checkbox-cuadro");
          if (checkbox) {
            checkbox.classList.toggle("seleccionado", resp.valor === true);
            checkbox.dataset.usuarioSeleccionado = resp.valor;
          }
          break;
  
        case "desplegable":
          const select = el.querySelector("select");
          if (select) {
            Array.from(select.options).forEach(opt => {
              if (opt.textContent === resp.valor) opt.selected = true;
            });
          }
          break;
  
        case "join":
          el.dataset.joinSeleccionado = resp.valor; // si usas lógica de unión más adelante
          break;
      }
    });
  });
  