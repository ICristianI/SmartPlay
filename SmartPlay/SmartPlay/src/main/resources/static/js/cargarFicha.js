document.addEventListener("DOMContentLoaded", () => {
    const datosRaw = document.getElementById("elementosSuperpuestosRaw");
    if (!datosRaw) return;
  
    const elementos = JSON.parse(datosRaw.textContent);
  
    elementos.forEach(elemento => {
      switch (elemento.tipo) {
        case "evaluado":
          restaurarTextoEvaluado(elemento);
          break;
        case "decorativo":
          restaurarTextoDecorativo(elemento);
          break;
        case "seleccion":
          restaurarSeleccionUnica(elemento);
          break;
        case "checkbox":
          restaurarCheckbox(elemento);
          break;
        case "join":
          restaurarJoin(elemento);
          break;
        case "desplegable":
          restaurarDesplegable(elemento);
          break;
      }
    });
  });

  function restaurarTextoEvaluado(elemento) {
    const contenedor = document.getElementById("contenedorFicha");
    const wrapper = document.createElement("div");
    wrapper.dataset.tipo = "evaluado";
    wrapper.className = "elemento-editable";
    wrapper.style.position = "absolute";
    wrapper.style.left = `${elemento.x}px`;
    wrapper.style.top = `${elemento.y}px`;
    wrapper.style.zIndex = 10;
  
    const input = document.createElement("textarea");
    input.className = "editable-div";
    input.value = elemento.texto || "";
    input.readOnly = true;
    input.style.width = `${elemento.width}px`;
    input.style.height = `${elemento.height}px`;
    if (elemento.colorFondo) input.style.backgroundColor = elemento.colorFondo;
    if (elemento.colorTexto) input.style.color = elemento.colorTexto;

  
    const botones = crearBotonesEdicion(wrapper, input);
    wrapper.appendChild(botones);
    wrapper.appendChild(input);
    contenedor.appendChild(wrapper);
  
    habilitarArrastreSoloSiActivo(wrapper);
    habilitarHoverBotones(wrapper);
  }

  
  function restaurarTextoDecorativo(elemento) {
    const contenedor = document.getElementById("contenedorFicha");
    const wrapper = document.createElement("div");
    wrapper.dataset.tipo = "decorativo";
    wrapper.className = "elemento-editable";
    wrapper.style.position = "absolute";
    wrapper.style.left = `${elemento.x}px`;
    wrapper.style.top = `${elemento.y}px`;
    wrapper.style.zIndex = 10;
  
    const input = document.createElement("textarea");
    input.className = "editable-div";
    input.value = elemento.texto || "";
    input.readOnly = true;
    input.style.width = `${elemento.width}px`;
    input.style.height = `${elemento.height}px`;
  
    const botones = crearBotonesEdicion(wrapper, input);
    wrapper.appendChild(botones);
    wrapper.appendChild(input);
    contenedor.appendChild(wrapper);
  
    habilitarArrastreSoloSiActivo(wrapper);
    habilitarHoverBotones(wrapper);
  }

  
  function restaurarSeleccionUnica(elemento) {
    const contenedor = document.getElementById("contenedorFicha");
  
    const wrapper = document.createElement("div");
    wrapper.dataset.tipo = "seleccion";
    wrapper.className = "elemento-editable";
    wrapper.style.position = "absolute";
    wrapper.style.left = `${elemento.x}px`;
    wrapper.style.top = `${elemento.y}px`;
    wrapper.style.zIndex = 10;
  
    const opcionesDiv = document.createElement("div");
    opcionesDiv.className = "seleccion-unica editable-div";
    opcionesDiv.style.resize = "both";
    opcionesDiv.style.overflow = "auto";
    opcionesDiv.style.minWidth = "150px";
    opcionesDiv.style.minHeight = "60px";
    if (elemento.colorFondo) opcionesDiv.style.backgroundColor = elemento.colorFondo;
    if (elemento.colorTexto) opcionesDiv.style.color = elemento.colorTexto;
    

  
    renderizarOpciones(opcionesDiv, elemento.opciones);
  
    const botones = crearBotonesSeleccion(wrapper, opcionesDiv, elemento.opciones);
    wrapper.appendChild(botones);
    wrapper.appendChild(opcionesDiv);
    contenedor.appendChild(wrapper);
  
    habilitarArrastreSoloSiActivo(wrapper);
    habilitarHoverBotones(wrapper);
  }

  
  function restaurarCheckbox(elemento) {
    const contenedor = document.getElementById("contenedorFicha");
  
    const wrapper = document.createElement("div");
    wrapper.dataset.tipo = "checkbox";
    wrapper.dataset.estado = elemento.estado;
    wrapper.className = "elemento-editable";
    wrapper.style.position = "absolute";
    wrapper.style.left = `${elemento.x}px`;
    wrapper.style.top = `${elemento.y}px`;
    wrapper.style.zIndex = 10;
  
    const checkbox = document.createElement("div");
    checkbox.className = `checkbox-cuadro ${elemento.estado}`;
    checkbox.dataset.estado = elemento.estado;
  
    const botones = crearBotonesCheckbox(wrapper, checkbox);
    wrapper.appendChild(botones);
    wrapper.appendChild(checkbox);
    contenedor.appendChild(wrapper);
  
    habilitarArrastreSoloSiActivo(wrapper);
    habilitarHoverBotones(wrapper);
  }

  
  function restaurarJoin(elemento) {
    const contenedor = document.getElementById("contenedorFicha");
  
    const wrapper = document.createElement("div");
    wrapper.dataset.tipo = "join";
    wrapper.dataset.joinId = elemento.joinId || "";
    wrapper.className = "elemento-editable";
    wrapper.style.position = "absolute";
    wrapper.style.left = `${elemento.x}px`;
    wrapper.style.top = `${elemento.y}px`;
    wrapper.style.zIndex = 10;
  
    const cuadro = document.createElement("div");
    cuadro.className = "editable-div";
    cuadro.textContent = "Join: " + (elemento.joinId || "sin ID");
    cuadro.style.textAlign = "center";
  
    const botones = crearBotonesJoin(wrapper);
    wrapper.appendChild(botones);
    wrapper.appendChild(cuadro);
    contenedor.appendChild(wrapper);
  
    habilitarArrastreSoloSiActivo(wrapper);
    habilitarHoverBotones(wrapper);
  }

  function restaurarDesplegable(elemento) {
    const contenedor = document.getElementById("contenedorFicha");
  
    const wrapper = document.createElement("div");
    wrapper.dataset.tipo = "desplegable";
    wrapper.className = "elemento-editable";
    wrapper.style.position = "absolute";
    wrapper.style.left = `${elemento.x}px`;
    wrapper.style.top = `${elemento.y}px`;
    wrapper.style.zIndex = 10;
  
    const opcionesDiv = document.createElement("div");
    opcionesDiv.className = "seleccion-unica editable-div";
    opcionesDiv.style.resize = "both";
    opcionesDiv.style.overflow = "auto";
    opcionesDiv.style.minWidth = "150px";
    opcionesDiv.style.minHeight = "60px";
    if (elemento.colorFondo) opcionesDiv.style.backgroundColor = elemento.colorFondo;
    if (elemento.colorTexto) opcionesDiv.style.color = elemento.colorTexto;

  
    renderizarOpcionesDesplegable(opcionesDiv, elemento.opciones);
  
    const botones = crearBotonesDesplegable(wrapper, opcionesDiv, elemento.opciones);
    wrapper.appendChild(botones);
    wrapper.appendChild(opcionesDiv);
    contenedor.appendChild(wrapper);
  
    habilitarArrastreSoloSiActivo(wrapper);
    habilitarHoverBotones(wrapper);
  }

  function crearBotonesDesplegable(wrapper, opcionesDiv, opciones) {
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
  
    return botones;
  }

  function crearBotonesEdicion(wrapper, input) {
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
    btnEditar.onclick = () => abrirModalEdicion(input);
  
    const btnEliminar = document.createElement("button");
    btnEliminar.className = "btn btn-sm btn-danger";
    btnEliminar.innerHTML = "🗑️";
    btnEliminar.onclick = () => wrapper.remove();
  
    botones.appendChild(btnMover);
    botones.appendChild(btnEditar);
    botones.appendChild(btnEliminar);
  
    return botones;
  }

  
  function crearBotonesSeleccion(wrapper, opcionesDiv, opciones) {
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
  
    return botones;
  }

  
  function crearBotonesCheckbox(wrapper, checkbox) {
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
  
    return botones;
  }

  function crearBotonesJoin(wrapper) {
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
  
    return botones;
  }
  
  
  
  