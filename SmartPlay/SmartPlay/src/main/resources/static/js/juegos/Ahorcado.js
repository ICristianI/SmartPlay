interact('.letter')
    .draggable({
        onstart(event) {
            event.target.style.backgroundColor = 'yellow';
        },
        onmove(event) {
            // Lógica para mover la letra mientras se arrastra
        },
        onend(event) {
            event.target.style.backgroundColor = '';
            const letter = event.target.dataset.letter;
            const wordSlots = document.querySelectorAll('.word-slot');
            
            wordSlots.forEach(slot => {
                if (slot.innerText === "_" && letter === "A") {  // Verifica si es la letra correcta
                    slot.innerText = letter;
                }
            });
        }
    });
