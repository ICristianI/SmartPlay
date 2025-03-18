document.addEventListener("DOMContentLoaded", function () {
    const palabra = juego.palabra.toUpperCase();
    let intentosRestantes = parseInt(juego.maxIntentos);
    let letrasAdivinadas = new Set();

    inicializarJuego();

    function inicializarJuego() {
        document.getElementById("intentosRestantes").textContent = intentosRestantes;
        actualizarPalabra();
        crearTeclado();
    }

    function actualizarPalabra() {
        const contenedor = document.getElementById("palabraAdivinada");
        contenedor.innerHTML = "";
        palabra.split("").forEach(letra => {
            const span = document.createElement("span");
            span.textContent = letrasAdivinadas.has(letra) || letra === " " ? letra : "_";
            span.classList.add("mx-2", "fs-2", "fw-bold", "border-bottom", "border-3", "px-2");
            contenedor.appendChild(span);
        });

        if (![...palabra].some(letra => !letrasAdivinadas.has(letra) && letra !== " ")) {
            mostrarMensaje("Ganaste!", "success");
        }
    }

    function crearTeclado() {
        const teclado = document.getElementById("teclado");
        teclado.innerHTML = "";
        
        // Letras organizadas en dos filas
        const letrasFila1 = "QWERTYUIOP".split("");
        const letrasFila2 = "ASDFGHJKL".split("");
        const letrasFila3 = "ZXCVBNM".split("");

        const fila1 = document.createElement("div");
        const fila2 = document.createElement("div");
        const fila3 = document.createElement("div");

        fila1.classList.add("d-flex", "justify-content-center", "mb-2");
        fila2.classList.add("d-flex", "justify-content-center", "mb-2");
        fila3.classList.add("d-flex", "justify-content-center");

        [letrasFila1, letrasFila2, letrasFila3].forEach((fila, index) => {
            fila.forEach(letra => {
                const boton = document.createElement("button");
                boton.textContent = letra;
                boton.classList.add("btn", "btn-outline-dark", "m-1", "px-3", "py-2");
                boton.onclick = () => intentarLetra(letra, boton);
                
                if (index === 0) fila1.appendChild(boton);
                else if (index === 1) fila2.appendChild(boton);
                else fila3.appendChild(boton);
            });
        });

        teclado.appendChild(fila1);
        teclado.appendChild(fila2);
        teclado.appendChild(fila3);
    }

    function intentarLetra(letra, boton) {
        boton.disabled = true;
        if (palabra.includes(letra)) {
            letrasAdivinadas.add(letra);
            actualizarPalabra();
        } else {
            intentosRestantes--;
            document.getElementById("intentosRestantes").textContent = intentosRestantes;
        }

        if (intentosRestantes === 0) {
            mostrarMensaje("Perdiste! La palabra era: " + palabra, "danger");
            document.querySelectorAll("#teclado button").forEach(btn => btn.disabled = true);
        }
    }

    function mostrarMensaje(mensaje, tipo) {
        const mensajeDiv = document.getElementById("mensaje");
        mensajeDiv.innerHTML = `<div class="alert alert-${tipo} mt-3">${mensaje}</div>`;
    }

    function reiniciarJuego() {
        letrasAdivinadas.clear();
        intentosRestantes = parseInt(juego.maxIntentos);
        document.getElementById("mensaje").innerHTML = "";
        inicializarJuego();
    }

    function descargarPDF() {
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();

        // Agregar título
        doc.setFontSize(20);
        doc.text(juego.nombre, 10, 20);

        // Agregar descripción y detalles
        doc.setFontSize(14);
        doc.text(`Descripción: ${juego.descripcion}`, 10, 40);
        doc.text(`Intentos máximos: ${juego.maxIntentos}`, 10, 50);
        doc.text(`Palabra: ${"_ ".repeat(juego.palabra.length)}`, 10, 60);

        const img = new Image();
        img.src = "/images/generalImages/imagenAhorcado.png";
        img.onload = function () {
            doc.addImage(img, "PNG", 10, 70, 60, 60);
            doc.save(`${juego.nombre}.pdf`);
        };
    }

    window.reiniciarJuego = reiniciarJuego;
    window.descargarPDF = descargarPDF;

    function descargarPDF() {
        const { jsPDF } = window.jspdf;
        const elementoJuego = document.querySelector(".container");
    
        // Ocultar los botones de "Reiniciar", "Descargar PDF" y "Volver"
        document.querySelectorAll(".btn-primary, .btn-success, .btn-secondary").forEach(el => el.style.display = "none");
    
        // Ocultar solo el mensaje de victoria/derrota, pero dejar el título
        const mensajeDiv = document.getElementById("mensaje");
        const mensajeOriginal = mensajeDiv.innerHTML;
        mensajeDiv.innerHTML = ""; // Borra el mensaje temporalmente
    
        const imagenJuego = document.getElementById("imagenJuego");
        const clasesOriginales = imagenJuego.className;
        imagenJuego.classList.remove("navbar-custom");
    
        // Cambiar las líneas de la palabra a negro temporalmente
        document.querySelectorAll("#palabraAdivinada span").forEach(span => {
            span.classList.add("border-dark");
        });
    
        html2canvas(elementoJuego, { scale: 2 }).then(canvas => {
            const imgData = canvas.toDataURL("image/png");
            const pdf = new jsPDF("p", "mm", "a4");
            const imgWidth = 210;
            const imgHeight = (canvas.height * imgWidth) / canvas.width;
    
            pdf.setFontSize(20);
            pdf.text(juego.nombre, 10, 20); // Asegura que el título se mantenga
    
            pdf.addImage(imgData, "PNG", 0, 30, imgWidth, imgHeight);
            pdf.save(`${juego.nombre}.pdf`);
    
            // Restaurar solo los botones ocultos
            document.querySelectorAll(".btn-primary, .btn-success, .btn-secondary").forEach(el => el.style.display = "");
            imagenJuego.classList.add("navbar-custom");
    
            // Restaurar el mensaje original
            mensajeDiv.innerHTML = mensajeOriginal;
    
            // Quitar la clase de borde negro para mantener el diseño original
            document.querySelectorAll("#palabraAdivinada span").forEach(span => {
                span.classList.remove("border-dark");
            });
        });
    }
    
    
});


