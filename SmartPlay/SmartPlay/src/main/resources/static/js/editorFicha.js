let elementoEditando = null;
let checkboxEditando = null;

document.addEventListener("DOMContentLoaded", () => {
  const selectTamano = document.getElementById("tamanoFicha");
  const contenedor = document.getElementById("contenedorFicha");

  if (selectTamano && contenedor) {
    selectTamano.addEventListener("change", function () {
      contenedor.style.width = `${this.value}px`;
    });
  }
});

window.agregarTexto = function () {
    const contenedor = document.getElementById("contenedorFicha");
  
    const wrapper = document.createElement("div");
    wrapper.dataset.tipo = "evaluado";
    wrapper.className = "elemento-editable";
    wrapper.style.position = "absolute";
    wrapper.style.left = "100px";
    wrapper.style.top = "100px";
    wrapper.style.zIndex = 10;
  
    const input = document.createElement("textarea");
    input.className = "editable-div";
    input.value = "Escribe aquí";
    input.readOnly = true;
  

    const botones = document.createElement("div");
    botones.className = "botones-edicion gap-1";
  
    const btnMover = document.createElement("button");
    btnMover.className = "btn btn-sm btn-info";
    btnMover.innerHTML = "➕";
    btnMover.dataset.mover = "off";
  
    btnMover.onclick = () => {
        if (btnMover.dataset.mover === "off") {
          btnMover.dataset.mover = "on";
          btnMover.innerHTML = "✖️";
          wrapper.classList.add("modo-mover-activo");
        } else {
          btnMover.dataset.mover = "off";
          btnMover.innerHTML = "➕";
          wrapper.classList.remove("modo-mover-activo");
        }
      };
      
  
    const btnEditar = document.createElement("button");
    btnEditar.className = "btn btn-sm btn-warning";
    btnEditar.innerHTML = "✏️";
    btnEditar.onclick = () => abrirModalEdicion(input);
  
    const btnEliminar = document.createElement("button");
    btnEliminar.className = "btn btn-sm btn-danger";
    btnEliminar.innerHTML = "🗑️";
    btnEliminar.onclick = () => wrapper.remove();
  
    botones.appendChild(btnMover);
    botones.appendChild(btnEditar);
    botones.appendChild(btnEliminar);
  
    wrapper.appendChild(botones);
    wrapper.appendChild(input);
    contenedor.appendChild(wrapper);
  
    habilitarArrastreSoloSiActivo(wrapper);
    habilitarHoverBotones(wrapper);
  };

  window.agregarTextoDecorativo = function () {
    const contenedor = document.getElementById("contenedorFicha");
  
    const wrapper = document.createElement("div");
    wrapper.className = "elemento-editable";
    wrapper.dataset.tipo = "decorativo";
    wrapper.style.position = "absolute";
    wrapper.style.left = "100px";
    wrapper.style.top = "100px";
    wrapper.style.zIndex = 10;
  
    const input = document.createElement("textarea");
    input.className = "editable-div";
    input.value = "Texto decorativo";
    input.readOnly = true;
  
    const botones = document.createElement("div");
    botones.className = "botones-edicion gap-1";
  
    const btnMover = document.createElement("button");
    btnMover.className = "btn btn-sm btn-info";
    btnMover.innerHTML = "➕";
    btnMover.dataset.mover = "off";
    btnMover.onclick = () => {
      if (btnMover.dataset.mover === "off") {
        btnMover.dataset.mover = "on";
        btnMover.innerHTML = "✖️";
        wrapper.classList.add("modo-mover-activo");
      } else {
        btnMover.dataset.mover = "off";
        btnMover.innerHTML = "➕";
        wrapper.classList.remove("modo-mover-activo");
      }
    };
  
    const btnEditar = document.createElement("button");
    btnEditar.className = "btn btn-sm btn-warning";
    btnEditar.innerHTML = "✏️";
    btnEditar.onclick = () => abrirModalEdicion(input);
  
    const btnEliminar = document.createElement("button");
    btnEliminar.className = "btn btn-sm btn-danger";
    btnEliminar.innerHTML = "🗑️";
    btnEliminar.onclick = () => wrapper.remove();
  
    botones.appendChild(btnMover);
    botones.appendChild(btnEditar);
    botones.appendChild(btnEliminar);
  
    wrapper.appendChild(botones);
    wrapper.appendChild(input);
    contenedor.appendChild(wrapper);
  
    habilitarArrastreSoloSiActivo(wrapper);
    habilitarHoverBotones(wrapper);
  };

  let seleccionActual = null;

  window.agregarSeleccionUnica = function () {
    const contenedor = document.getElementById("contenedorFicha");
  
    const wrapper = document.createElement("div");

    wrapper.dataset.tipo = "seleccion";
    wrapper.className = "elemento-editable";
    wrapper.style.position = "absolute";
    wrapper.style.left = "100px";
    wrapper.style.top = "100px";
    wrapper.style.zIndex = 10;
  
    const opcionesDiv = document.createElement("div");
    opcionesDiv.className = "seleccion-unica editable-div";
    opcionesDiv.style.resize = "both";
    opcionesDiv.style.overflow = "auto";
    opcionesDiv.style.minWidth = "150px";
    opcionesDiv.style.minHeight = "60px";
  
    const opciones = [
      { texto: "Opción 1", correcta: true },
      { texto: "Opción 2", correcta: false },
    ];
    renderizarOpciones(opcionesDiv, opciones);
  
    const botones = document.createElement("div");
    botones.className = "botones-edicion gap-1";
  
    const btnMover = document.createElement("button");
    btnMover.className = "btn btn-sm btn-info";
    btnMover.innerHTML = "➕";
    btnMover.dataset.mover = "off";
    btnMover.onclick = () => {
      const activo = btnMover.dataset.mover === "on";
      btnMover.dataset.mover = activo ? "off" : "on";
      btnMover.innerHTML = activo ? "➕" : "✖️";
      wrapper.classList.toggle("modo-mover-activo", !activo);
    };
  
    const btnEditar = document.createElement("button");
    btnEditar.className = "btn btn-sm btn-warning";
    btnEditar.innerHTML = "✏️";
    btnEditar.onclick = () => abrirModalSeleccionUnica(opciones, opcionesDiv);
  
    const btnEliminar = document.createElement("button");
    btnEliminar.className = "btn btn-sm btn-danger";
    btnEliminar.innerHTML = "🗑️";
    btnEliminar.onclick = () => wrapper.remove();
  
    botones.appendChild(btnMover);
    botones.appendChild(btnEditar);
    botones.appendChild(btnEliminar);
  
    wrapper.appendChild(botones);
    wrapper.appendChild(opcionesDiv);
    contenedor.appendChild(wrapper);
  
    habilitarArrastreSoloSiActivo(wrapper);
    habilitarHoverBotones(wrapper);
  };
  
  function renderizarOpciones(contenedor, opciones) {
    contenedor.innerHTML = "";
    opciones.forEach((opcion, index) => {
      const div = document.createElement("div");
      const input = document.createElement("input");
      input.type = "radio";
      input.disabled = true;
      input.checked = opcion.correcta;
      input.name = `grupo_${Date.now()}`;
  
      const label = document.createElement("label");
      label.textContent = opcion.texto;
      label.style.marginLeft = "0.5rem";
  
      div.appendChild(input);
      div.appendChild(label);
      contenedor.appendChild(div);
    });
  }
  
  function abrirModalSeleccionUnica(opciones, contenedorOpciones) {
    seleccionActual = { opciones, contenedorOpciones };
    const lista = document.getElementById("listaOpciones");
    lista.innerHTML = "";
  
    opciones.forEach((op, i) => {
      const item = document.createElement("div");
      item.className = "d-flex align-items-center mb-2 gap-2";
  
      const input = document.createElement("input");
      input.type = "text";
      input.value = op.texto;
      input.className = "form-control";
      input.oninput = (e) => (op.texto = e.target.value);
  
      const checkbox = document.createElement("input");
      checkbox.type = "radio";
      checkbox.name = "opCorrecta";
      checkbox.checked = op.correcta;
      checkbox.onclick = () => {
        opciones.forEach((o, j) => (o.correcta = j === i));
      };
  
      const btnEliminar = document.createElement("button");
      btnEliminar.className = "btn btn-danger btn-sm";
      btnEliminar.textContent = "✖";
      btnEliminar.onclick = () => {
        opciones.splice(i, 1);
        abrirModalSeleccionUnica(opciones, contenedorOpciones);
      };

      item.appendChild(input);
      item.appendChild(checkbox);
      item.appendChild(btnEliminar);
      lista.appendChild(item);
    });
  
    const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById("modalSeleccionUnica"));
    const fondo = contenedorOpciones.style.backgroundColor || "";
    const esTransparente = fondo === "transparent" || fondo === "" || fondo === "rgba(0, 0, 0, 0)";
    document.getElementById("colorFondoSeleccionUnica").value = rgbToHex(fondo || "#ffffff");
    document.getElementById("sinFondoSeleccionUnica").checked = esTransparente;

    document.getElementById("colorTextoSeleccionUnica").value = rgbToHex(contenedorOpciones.style.color || "#000000");

    modal.show();
    
  }
  
  window.guardarSeleccionUnicaEditada = function () {
    if (seleccionActual) {
      const fondo = document.getElementById("colorFondoSeleccionUnica").value;
      const texto = document.getElementById("colorTextoSeleccionUnica").value;
      const sinFondo = document.getElementById("sinFondoSeleccionUnica").checked;

      const contenedor = seleccionActual.contenedorOpciones;
      contenedor.style.backgroundColor = sinFondo ? "transparent" : fondo;
      contenedor.style.color = texto;

      renderizarOpciones(seleccionActual.contenedorOpciones, seleccionActual.opciones);
      bootstrap.Modal.getInstance(document.getElementById("modalSeleccionUnica")).hide();
    }
  };
  
  window.agregarOpcion = function () {
    if (seleccionActual) {
      seleccionActual.opciones.push({ texto: "Nueva opción", correcta: false });
      abrirModalSeleccionUnica(seleccionActual.opciones, seleccionActual.contenedorOpciones);
    }
  };

  window.agregarOpcionSeleccion = function () {
    const contenedor = document.getElementById("opcionesSeleccionUnica");
  
    const fila = document.createElement("div");
    fila.className = "d-flex align-items-center mb-2 gap-2";
  
    const input = document.createElement("input");
    input.type = "text";
    input.className = "form-control";
    input.placeholder = "Escribe una opción";
  
    const check = document.createElement("input");
    check.type = "radio";
    check.name = "opcionCorrecta";
  
    const btnEliminar = document.createElement("button");
    btnEliminar.className = "btn btn-sm btn-danger";
    btnEliminar.innerText = "✖";
    btnEliminar.onclick = () => fila.remove();
  
    fila.appendChild(input);
    fila.appendChild(check);
    fila.appendChild(btnEliminar);
  
    contenedor.appendChild(fila);
  };
  
  
  function abrirModalEdicion(input) {
    elementoEditando = input;
    document.getElementById("textoEditable").value = input.value;
  
    const fondo = getComputedStyle(input).backgroundColor;
    const esTransparente = fondo === "transparent" || fondo === "rgba(0, 0, 0, 0)";
    document.getElementById("colorFondoTextoEditable").value = rgbToHex(fondo);
    document.getElementById("sinFondoTextoEditable").checked = esTransparente;
  
    const texto = getComputedStyle(input).color;
    document.getElementById("colorTextoEditable").value = rgbToHex(texto);
  
    const modal = new bootstrap.Modal(document.getElementById("modalEditarTexto"));
    modal.show();
  }
  
  

  window.guardarTextoEditado = function () {
    if (elementoEditando) {
      const nuevoTexto = document.getElementById("textoEditable").value;
      const colorFondo = document.getElementById("colorFondoTextoEditable").value;
      const colorTexto = document.getElementById("colorTextoEditable").value;
      const sinFondo = document.getElementById("sinFondoTextoEditable").checked;
      
      
      elementoEditando.value = nuevoTexto.substring(0, 60);
      elementoEditando.style.backgroundColor = sinFondo ? "transparent" : colorFondo;
      elementoEditando.style.color = colorTexto;
  
      bootstrap.Modal.getInstance(document.getElementById("modalEditarTexto")).hide();
    }
  };
  


window.agregarCheckbox = function () {
    const contenedor = document.getElementById("contenedorFicha");
  
    const wrapper = document.createElement("div");
    wrapper.dataset.tipo = "checkbox";
    wrapper.dataset.estado = "neutral"; // <- NUEVO
    wrapper.className = "elemento-editable";
    wrapper.style.position = "absolute";
    wrapper.style.left = "100px";
    wrapper.style.top = "100px";
    wrapper.style.zIndex = 10;
  
    const checkbox = document.createElement("div");
    checkbox.className = "checkbox-cuadro neutral";
    checkbox.dataset.estado = "neutral";
  
    const botones = document.createElement("div");
    botones.className = "botones-edicion gap-1";
  
    const btnMover = document.createElement("button");
    btnMover.className = "btn btn-sm btn-info";
    btnMover.innerHTML = "➕";
    btnMover.dataset.mover = "off";
    btnMover.onclick = () => {
      const activo = btnMover.dataset.mover === "on";
      btnMover.dataset.mover = activo ? "off" : "on";
      btnMover.innerHTML = activo ? "➕" : "✖️";
      wrapper.classList.toggle("modo-mover-activo", !activo);
    };
  
    const btnEditar = document.createElement("button");
    btnEditar.className = "btn btn-sm btn-warning";
    btnEditar.innerHTML = "✏️";
    btnEditar.onclick = () => abrirModalCheckbox(checkbox);
  
    const btnEliminar = document.createElement("button");
    btnEliminar.className = "btn btn-sm btn-danger";
    btnEliminar.innerHTML = "🗑️";
    btnEliminar.onclick = () => wrapper.remove();
  
    botones.appendChild(btnMover);
    botones.appendChild(btnEditar);
    botones.appendChild(btnEliminar);
  
    wrapper.appendChild(botones);
    wrapper.appendChild(checkbox);
    contenedor.appendChild(wrapper);
    wrapper.dataset.estado = "neutral";

  
    habilitarArrastreSoloSiActivo(wrapper);
    habilitarHoverBotones(wrapper);
  };
  
  
  function abrirModalCheckbox(checkbox) {
  checkboxEditando = checkbox;
  const estado = checkbox.dataset.estado || "neutral";
  document.getElementById("checkboxEstado").value = estado;

  const modal = new bootstrap.Modal(document.getElementById("modalCheckbox"));
  modal.show();
}

  
window.guardarCheckbox = function () {
    const estado = document.getElementById("checkboxEstado").value;
  
    if (checkboxEditando) {
      checkboxEditando.dataset.estado = estado;
      checkboxEditando.className = `checkbox-cuadro ${estado}`;
  
      const wrapper = checkboxEditando.closest(".elemento-editable");
      if (wrapper) {
        wrapper.dataset.estado = estado;
      }
  
      checkboxEditando = null;
    }
  
    bootstrap.Modal.getInstance(document.getElementById("modalCheckbox")).hide();
  };
  
  
function renderizarCheckboxes(contenedor, opciones) {
    contenedor.innerHTML = "";
    opciones.forEach(op => {
      const div = document.createElement("div");
  
      const checkbox = document.createElement("input");
      checkbox.type = "checkbox";
      checkbox.disabled = true;
      checkbox.checked = op.correcta;
  
      const label = document.createElement("label");
      label.textContent = op.texto;
      label.style.marginLeft = "0.5rem";
  
      div.appendChild(checkbox);
      div.appendChild(label);
      contenedor.appendChild(div);
    });
  }

  let checkboxActual = null;
  
  function abrirModalCheckboxes(opciones, contenedor) {
    checkboxActual = { opciones, contenedor };
    const editor = document.getElementById("contenedorCheckboxes");
    editor.innerHTML = "";
  
    opciones.forEach((op, i) => {
      const fila = document.createElement("div");
      fila.className = "d-flex align-items-center mb-2 gap-2";
  
      const input = document.createElement("input");
      input.type = "text";
      input.value = op.texto;
      input.className = "form-control";
      input.oninput = e => op.texto = e.target.value;
  
      const check = document.createElement("input");
      check.type = "checkbox";
      check.checked = op.correcta;
      check.onclick = () => op.correcta = check.checked;
  
      const btnEliminar = document.createElement("button");
      btnEliminar.className = "btn btn-danger btn-sm";
      btnEliminar.innerText = "✖";
      btnEliminar.onclick = () => {
        opciones.splice(i, 1);
        abrirModalCheckboxes(opciones, contenedor);
      };
  
      fila.appendChild(input);
      fila.appendChild(check);
      fila.appendChild(btnEliminar);
      editor.appendChild(fila);
    });
  
    const modal = new bootstrap.Modal(document.getElementById("modalEditarCheckboxes"));
    modal.show();
  }
  
  window.guardarCheckboxes = function () {
    if (checkboxActual) {
      renderizarCheckboxes(checkboxActual.contenedor, checkboxActual.opciones);
      bootstrap.Modal.getInstance(document.getElementById("modalEditarCheckboxes")).hide();
    }
  };
  
  window.agregarCheckboxOpcion = function () {
    if (checkboxActual) {
      checkboxActual.opciones.push({ texto: "Nueva opción", correcta: false });
      abrirModalCheckboxes(checkboxActual.opciones, checkboxActual.contenedor);
    }
  };

  let joinEditando = null;

window.agregarJoin = function () {
  const contenedor = document.getElementById("contenedorFicha");

  const wrapper = document.createElement("div");
  wrapper.className = "elemento-editable";
  wrapper.dataset.tipo = "join";
  wrapper.style.position = "absolute";
  wrapper.style.left = "100px";
  wrapper.style.top = "100px";
  wrapper.style.zIndex = 10;
  wrapper.dataset.tipo = "join";
  wrapper.dataset.joinId = "";

  const cuadro = document.createElement("div");
  cuadro.className = "editable-div";
  cuadro.textContent = "Join";
  cuadro.style.textAlign = "center";

  const botones = document.createElement("div");
  botones.className = "botones-edicion gap-1";

  const btnMover = document.createElement("button");
  btnMover.className = "btn btn-sm btn-info";
  btnMover.innerHTML = "➕";
  btnMover.dataset.mover = "off";
  btnMover.onclick = () => {
    const activo = btnMover.dataset.mover === "on";
    btnMover.dataset.mover = activo ? "off" : "on";
    btnMover.innerHTML = activo ? "➕" : "✖️";
    wrapper.classList.toggle("modo-mover-activo", !activo);
  };

  const btnEditar = document.createElement("button");
  btnEditar.className = "btn btn-sm btn-warning";
  btnEditar.innerHTML = "✏️";
  btnEditar.onclick = () => abrirModalJoin(wrapper);

  const btnEliminar = document.createElement("button");
  btnEliminar.className = "btn btn-sm btn-danger";
  btnEliminar.innerHTML = "🗑️";
  btnEliminar.onclick = () => wrapper.remove();

  botones.appendChild(btnMover);
  botones.appendChild(btnEditar);
  botones.appendChild(btnEliminar);

  wrapper.appendChild(botones);
  wrapper.appendChild(cuadro);
  contenedor.appendChild(wrapper);

  habilitarArrastreSoloSiActivo(wrapper);
  habilitarHoverBotones(wrapper);
};


function abrirModalJoin(wrapper) {
  joinEditando = wrapper;
  document.getElementById("joinIdEditable").value = wrapper.dataset.joinId || "";
  const modal = new bootstrap.Modal(document.getElementById("modalJoin"));
  modal.show();
}

window.guardarJoin = function () {
  if (joinEditando) {
    const nuevoId = document.getElementById("joinIdEditable").value.trim();
    joinEditando.dataset.joinId = nuevoId;
    joinEditando.querySelector(".editable-div").textContent = "Join: " + (nuevoId || "sin ID");
    bootstrap.Modal.getInstance(document.getElementById("modalJoin")).hide();
  }
};


window.agregarDesplegable = function () {
  const contenedor = document.getElementById("contenedorFicha");

  const wrapper = document.createElement("div");
  wrapper.dataset.tipo = "desplegable";
  wrapper.className = "elemento-editable";
  wrapper.style.position = "absolute";
  wrapper.style.left = "100px";
  wrapper.style.top = "100px";
  wrapper.style.zIndex = 10;

  const opcionesDiv = document.createElement("div");
  opcionesDiv.className = "editable-div";
  opcionesDiv.style.display = "flex";
  opcionesDiv.style.alignItems = "center";
  opcionesDiv.style.justifyContent = "center";
  opcionesDiv.style.minWidth = "150px";
  opcionesDiv.style.minHeight = "50px";
  opcionesDiv.style.resize = "both";
  opcionesDiv.style.overflow = "hidden";
  

  const opciones = [
    { texto: "Opción A", correcta: true },
    { texto: "Opción B", correcta: false },
  ];
  renderizarOpcionesDesplegable(opcionesDiv, opciones);

  const botones = document.createElement("div");
  botones.className = "botones-edicion gap-1";

  const btnMover = document.createElement("button");
  btnMover.className = "btn btn-sm btn-info";
  btnMover.innerHTML = "➕";
  btnMover.dataset.mover = "off";
  btnMover.onclick = () => {
    const activo = btnMover.dataset.mover === "on";
    btnMover.dataset.mover = activo ? "off" : "on";
    btnMover.innerHTML = activo ? "➕" : "✖️";
    wrapper.classList.toggle("modo-mover-activo", !activo);
  };

  const btnEditar = document.createElement("button");
  btnEditar.className = "btn btn-sm btn-warning";
  btnEditar.innerHTML = "✏️";
  btnEditar.onclick = () => abrirModalDesplegable(opciones, opcionesDiv);

  const btnEliminar = document.createElement("button");
  btnEliminar.className = "btn btn-sm btn-danger";
  btnEliminar.innerHTML = "🗑️";
  btnEliminar.onclick = () => wrapper.remove();

  botones.appendChild(btnMover);
  botones.appendChild(btnEditar);
  botones.appendChild(btnEliminar);

  wrapper.appendChild(botones);
  wrapper.appendChild(opcionesDiv);
  contenedor.appendChild(wrapper);

  habilitarArrastreSoloSiActivo(wrapper);
  habilitarHoverBotones(wrapper);
};

function renderizarOpcionesDesplegable(contenedor, opciones) {
  contenedor.innerHTML = "";
  const select = document.createElement("select");
    select.style.width = "100%";
    select.style.height = "100%";
    select.disabled = true;

  opciones.forEach(op => {
    const option = document.createElement("option");
    option.textContent = op.texto;
    option.selected = op.correcta;
    select.appendChild(option);
  });

  contenedor.appendChild(select);
}


function abrirModalDesplegable(opciones, contenedorOpciones) {
  desplegableActual = { opciones, contenedorOpciones };
  const lista = document.getElementById("listaOpcionesDesplegable");
  lista.innerHTML = "";

  opciones.forEach((op, i) => {
    const item = document.createElement("div");
    item.className = "d-flex align-items-center mb-2 gap-2";

    const input = document.createElement("input");
    input.type = "text";
    input.value = op.texto;
    input.className = "form-control";
    input.oninput = (e) => (op.texto = e.target.value);

    const checkbox = document.createElement("input");
    checkbox.type = "radio";
    checkbox.name = "opCorrectaDesplegable";
    checkbox.checked = op.correcta;
    checkbox.onclick = () => {
      opciones.forEach((o, j) => (o.correcta = j === i));
    };

    const btnEliminar = document.createElement("button");
    btnEliminar.className = "btn btn-danger btn-sm";
    btnEliminar.textContent = "✖";
    btnEliminar.onclick = () => {
      opciones.splice(i, 1);
      abrirModalDesplegable(opciones, contenedorOpciones);
    };

    item.appendChild(input);
    item.appendChild(checkbox);
    item.appendChild(btnEliminar);
    lista.appendChild(item);
  });

  const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById("modalDesplegable"));
  const fondo = contenedorOpciones.style.backgroundColor || "";
  const esTransparente = fondo === "transparent" || fondo === "" || fondo === "rgba(0, 0, 0, 0)";
  document.getElementById("colorFondoDesplegable").value = rgbToHex(fondo || "#ffffff");
  document.getElementById("sinFondoDesplegable").checked = esTransparente;

  document.getElementById("colorTextoDesplegable").value = rgbToHex(contenedorOpciones.style.color || "#000000");

  modal.show();
}

window.guardarDesplegableEditado = function () {
  if (desplegableActual) {
    const fondo = document.getElementById("colorFondoDesplegable").value;
    const texto = document.getElementById("colorTextoDesplegable").value;
    const sinFondo = document.getElementById("sinFondoDesplegable").checked;

    const contenedor = desplegableActual.contenedorOpciones;
    contenedor.style.backgroundColor = sinFondo ? "transparent" : fondo;
    contenedor.style.color = texto;

    renderizarOpcionesDesplegable(contenedor, desplegableActual.opciones);
    bootstrap.Modal.getInstance(document.getElementById("modalDesplegable")).hide();
  }
};



window.agregarOpcionDesplegable = function () {
  if (desplegableActual) {
    desplegableActual.opciones.push({ texto: "Nueva opción", correcta: false });
    abrirModalDesplegable(desplegableActual.opciones, desplegableActual.contenedorOpciones);
  }
};

function habilitarArrastreSoloSiActivo(wrapper) {
    let offsetX = 0, offsetY = 0, isDragging = false;
  
    wrapper.addEventListener("mousedown", (e) => {
      if (!wrapper.classList.contains("modo-mover-activo")) return;
      if (e.target.tagName === "BUTTON") return;
  
      isDragging = true;
      offsetX = e.clientX - wrapper.getBoundingClientRect().left;
      offsetY = e.clientY - wrapper.getBoundingClientRect().top;
      wrapper.style.transition = "none";
      document.body.style.userSelect = "none";
    });
  
    document.addEventListener("mousemove", (e) => {
      if (!isDragging) return;
  
      const contenedor = document.getElementById("contenedorFicha");
      const contenedorRect = contenedor.getBoundingClientRect();
  
      const maxX = contenedor.offsetWidth - wrapper.offsetWidth;
      const maxY = contenedor.offsetHeight - wrapper.offsetHeight;
      
      const x = Math.max(0, Math.min(e.clientX - contenedorRect.left - offsetX, maxX));
      const y = Math.max(0, Math.min(e.clientY - contenedorRect.top - offsetY, maxY));
      
  
      wrapper.style.left = `${x}px`;
      wrapper.style.top = `${y}px`;
    });
  
    document.addEventListener("mouseup", () => {
      isDragging = false;
      document.body.style.userSelect = "";
    });
  }
  

function habilitarHoverBotones(wrapper) {
  let hideTimeout;

  wrapper.addEventListener("mouseenter", () => {
    clearTimeout(hideTimeout);
    wrapper.classList.add("mostrar-botones");
  });

  wrapper.addEventListener("mouseleave", () => {
    hideTimeout = setTimeout(() => {
      wrapper.classList.remove("mostrar-botones");
    }, 200);
  });
}

document.querySelector("form").addEventListener("submit", function (e) {
    const elementos = [];
    const contenedor = document.getElementById("contenedorFicha").getBoundingClientRect();
  
    document.querySelectorAll(".elemento-editable").forEach((el) => {
      const tipo = el.dataset.tipo || "evaluado";
      const rect = el.getBoundingClientRect();
      const offsetLeft = rect.left - contenedor.left;
      const offsetTop = rect.top - contenedor.top;
  
      const base = {
        tipo,
        x: offsetLeft,
        y: offsetTop,
        width: el.offsetWidth,
        height: el.offsetHeight,
      };
  
      if (tipo === "evaluado" || tipo === "decorativo") {
        const textarea = el.querySelector("textarea");
        base.texto = textarea?.value || "";
        base.colorFondo = textarea?.style.backgroundColor || "";
        base.colorTexto = textarea?.style.color || "";
      }
      
      
  
      if (tipo === "seleccion") {
        const opciones = [];
        const inputs = el.querySelectorAll("input[type=radio]");
        const labels = el.querySelectorAll("label");
        inputs.forEach((input, i) => {
          opciones.push({
            texto: labels[i]?.textContent || "",
            correcta: input.checked,
          });
        });
        base.opciones = opciones;
      }
      
      if (tipo === "checkbox") {
        base.estado = el.dataset.estado || "neutral";
      }
  
      if (tipo === "join") {
        base.joinId = el.dataset.joinId || "";
      }

      if (tipo === "desplegable") {
        const opciones = [];
        const select = el.querySelector("select");
        if (select) {
          [...select.options].forEach((opt) => {
            opciones.push({
              texto: opt.textContent,
              correcta: opt.selected
            });
          });
        }
        base.opciones = opciones;
      }

      if (tipo === "seleccion" || tipo === "desplegable") {
        const contenedorOpciones = el.querySelector(".editable-div");
        base.colorFondo = contenedorOpciones?.style.backgroundColor || "";
        base.colorTexto = contenedorOpciones?.style.color || "";
      }
      
  
      elementos.push(base);
    });
  
    document.getElementById("elementosSuperpuestos").value = JSON.stringify(elementos);
  });

  function rgbToHex(rgb) {
    if (!rgb || !rgb.startsWith("rgb")) return rgb;
  
    const result = rgb.match(/\d+/g);
    if (!result || result.length < 3) return "#000000";
    return (
      "#" +
      result
        .slice(0, 3)
        .map(x => parseInt(x).toString(16).padStart(2, "0"))
        .join("")
    );
  }
  
  
