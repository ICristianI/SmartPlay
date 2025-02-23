package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.ImagenEntity;
import com.tfg.SmartPlay.repository.ImagenRepository;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
@RequestMapping("/imagen")
public class ImagenController {

    private final ImagenRepository imagenRepository;

    public ImagenController(ImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    @GetMapping("/subir")
    public String mostrarFormulario() {
        return "subirImagen";
    }

    @PostMapping("/guardar")
    public String newPost(Model model, @ModelAttribute ImagenEntity imagenEntity, @RequestParam("archivo") MultipartFile archivo) throws Exception {
        // Aquí ya tienes todos los parámetros mapeados automáticamente
        save(imagenEntity, archivo); // Guardar la imagen y otros datos
        imagenRepository.save(imagenEntity); // Guardar todo el objeto


        model.addAttribute("mensaje", "Imagen subida correctamente.");
        return "saved_post";
    }

    public void save(ImagenEntity imagenEntity, MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            imagenEntity.setImagen(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
        }
    }
}
