document.addEventListener('DOMContentLoaded', () => {
    const fileInput = document.getElementById('imageInput');
    if (!fileInput) {
        console.error("No se encontró el input con ID 'imageInput'.");
        return;
    }

    fileInput.addEventListener('change', (event) => {
        const file = event.target.files[0];
        if (file && file.size > 1048576) { 
            alert("El archivo es demasiado grande. Por favor, selecciona uno menor a 1 MB.");
            event.preventDefault(); 
        } else {
            fileInput.form.submit();
        }
    });
});