// Crucigrama clásico editable estilo tradicional

const crucigramaContainer = document.getElementById("crucigrama");
const listaPistas = document.getElementById("listaPistas");
const mensaje = document.getElementById("mensajeCrucigrama");

const pistas = juego.pistas.split(',');
const respuestas = juego.respuestas.split(',').map(r => r.toUpperCase().trim());
const gridSize = 15;
let grid = Array.from({ length: gridSize }, () => Array(gridSize).fill(null));
let usadas = [];

function colocarPalabraHorizontal(palabra, row, col) {
    if (col + palabra.length > gridSize) return false;
    for (let i = 0; i < palabra.length; i++) {
        if (grid[row][col + i] && grid[row][col + i] !== palabra[i]) return false;
    }
    for (let i = 0; i < palabra.length; i++) {
        grid[row][col + i] = palabra[i];
    }
    usadas.push({ palabra, row, col, horizontal: true });
    return true;
}

function colocarPalabraVertical(palabra, row, col) {
    if (row + palabra.length > gridSize) return false;
    for (let i = 0; i < palabra.length; i++) {
        if (grid[row + i][col] && grid[row + i][col] !== palabra[i]) return false;
    }
    for (let i = 0; i < palabra.length; i++) {
        grid[row + i][col] = palabra[i];
    }
    usadas.push({ palabra, row, col, horizontal: false });
    return true;
}

function colocarPalabras() {
    grid = Array.from({ length: gridSize }, () => Array(gridSize).fill(null));
    usadas = [];

    respuestas.forEach((palabra, index) => {
        let colocada = false;
        let intentos = 0;
        while (!colocada && intentos < 200) {
            const fila = Math.floor(Math.random() * gridSize);
            const col = Math.floor(Math.random() * gridSize);
            const horizontal = Math.random() > 0.5;
            colocada = horizontal
                ? colocarPalabraHorizontal(palabra, fila, col)
                : colocarPalabraVertical(palabra, fila, col);
            intentos++;
        }
    });
}

function renderizarCrucigrama() {
    crucigramaContainer.innerHTML = "";
    crucigramaContainer.style.display = "grid";
    crucigramaContainer.style.gridTemplateColumns = `repeat(${gridSize}, 30px)`;
    crucigramaContainer.style.gap = "2px";

    for (let i = 0; i < gridSize; i++) {
        for (let j = 0; j < gridSize; j++) {
            const cell = document.createElement("div");
            cell.classList.add("cruci-cell");
            cell.style.width = "30px";
            cell.style.height = "30px";
            cell.style.border = "1px solid #999";
            cell.style.display = "flex";
            cell.style.alignItems = "center";
            cell.style.justifyContent = "center";
            cell.style.fontWeight = "bold";
            cell.style.fontFamily = "monospace";
            cell.style.position = "relative";
            cell.style.backgroundColor = grid[i][j] ? "#fff" : "#333";
            cell.textContent = grid[i][j] ? "" : "";

            const usada = usadas.find((u, index) => {
                return u.row === i && u.col === j;
            });

            if (usada) {
                const numero = usadas.indexOf(usada) + 1;
                const numElem = document.createElement("span");
                numElem.textContent = numero;
                numElem.style.position = "absolute";
                numElem.style.top = "1px";
                numElem.style.left = "2px";
                numElem.style.fontSize = "10px";
                numElem.style.color = "#333";
                cell.appendChild(numElem);
            }

            crucigramaContainer.appendChild(cell);
        }
    }
}


function renderizarPistas() {
    listaPistas.innerHTML = "";
    pistas.forEach((pista, i) => {
        const li = document.createElement("li");
        li.classList.add("list-group-item", "d-flex", "justify-content-between", "align-items-center");
        li.style.color = "black";
        
        li.innerHTML = `
            <div><strong>${i + 1}.</strong> ${pista}</div>
            <input type="text" class="form-control form-control-sm w-50 respuesta-input" data-index="${i}" placeholder="Tu respuesta">
        `;
        listaPistas.appendChild(li);
    });

    restringirSoloLetras();
}

window.reiniciarCrucigrama = function () {
    colocarPalabras();
    renderizarCrucigrama();
    renderizarPistas();
};

window.descargarCrucigramaPDF = function () {
    const { jsPDF } = window.jspdf;
    const elemento = document.querySelector(".container");
    document.querySelectorAll(".btn").forEach(btn => btn.style.display = "none");

    html2canvas(elemento).then(canvas => {
        const imgData = canvas.toDataURL("image/png");
        const pdf = new jsPDF();
        const imgProps = pdf.getImageProperties(imgData);
        const pdfWidth = pdf.internal.pageSize.getWidth();
        const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;
        pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
        pdf.save(`${juego.nombre}_crucigrama.pdf`);
        document.querySelectorAll(".btn").forEach(btn => btn.style.display = "");
    });
};

function verificarRespuestas() {
    const inputs = document.querySelectorAll(".respuesta-input");
    let respuestasCorrectas = 0;

    inputs.forEach(input => {
        const index = parseInt(input.dataset.index);
        const respuesta = input.value.trim().toUpperCase();
        const palabraCorrecta = respuestas[index];

        if (respuesta === palabraCorrecta) {
            input.classList.add("border-success");
            input.classList.remove("border-danger");
            input.disabled = true;
            respuestasCorrectas++;

            // Mostrar la palabra en el crucigrama
            const { row, col, horizontal } = usadas.find(p => p.palabra === palabraCorrecta);
            for (let i = 0; i < palabraCorrecta.length; i++) {
                const pos = horizontal
                    ? row * gridSize + (col + i)
                    : (row + i) * gridSize + col;

                const cell = crucigramaContainer.children[pos];
                cell.textContent = palabraCorrecta[i];
                cell.style.color = "black";

            }
        } else {
            input.classList.remove("border-success");
            if (respuesta.length > 0) input.classList.add("border-danger");
        }
    });


    if (respuestasCorrectas === respuestas.length) {
        mensaje.innerHTML = `<span class="text-success fw-bold">🎉 ¡Has completado el crucigrama!</span>`;
        efectoVictoriaMulticolor();
    }
}


function efectoVictoriaMulticolor() {
    const celdas = crucigramaContainer.querySelectorAll(".cruci-cell");
    const colores = ["#FF595E", "#FFCA3A", "#8AC926", "#1982C4", "#6A4C93"];
    let index = 0;

    const intervalo = setInterval(() => {
        celdas.forEach((cell, i) => {
            const colorIndex = (index + i) % colores.length;
            if (cell.textContent.trim()) {
                cell.style.backgroundColor = colores[colorIndex];
            }
        });
        index++;
    }, 200);

    // Detener el efecto y restaurar color original después de 4 segundos
    setTimeout(() => {
        clearInterval(intervalo);
        celdas.forEach((cell) => {
            if (cell.textContent.trim()) {
                cell.style.backgroundColor = "#fff";
            }
        });
    }, 4000);
}

function restringirSoloLetras() {
    document.querySelectorAll(".respuesta-input").forEach(input => {
      input.addEventListener("input", function () {
        this.value = this.value.replace(/[^a-zA-Z]/g, "").toUpperCase();
      });
    });
  }
  

reiniciarCrucigrama();