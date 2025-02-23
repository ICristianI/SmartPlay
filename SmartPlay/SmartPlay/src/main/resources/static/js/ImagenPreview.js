function mostrarVistaPrevia(event) {
    const input = event.target;
    const preview = document.getElementById("previewImagen");

    if (input.files && input.files[0]) {
      const reader = new FileReader();

      reader.onload = function (e) {
        preview.src = e.target.result;
        preview.style.display = "block";
      };

      reader.readAsDataURL(input.files[0]);
    } else {
      preview.src = "";
      preview.style.display = "none";
    }
  }

  document.getElementById("imagenFondo").addEventListener("change", mostrarVistaPrevia);
