package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.service.ImagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Blob;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/f")
public class FichaController {

    @Autowired
    private FichaRepository fichaRepository;

    @Autowired
    private ImagenService imagenService; // Inyectar el servicio

    // Mostrar todas las fichas
    @GetMapping
    public String listarFichas(Model model) {
        List<Ficha> fichas = fichaRepository.findAll();
        model.addAttribute("fichas", fichas);
        return "fichas/listar";
    }

    // Mostrar formulario para crear una nueva ficha
    @GetMapping("/nueva")
    public String mostrarFormularioNuevaFicha(Model model) {
        model.addAttribute("ficha", new Ficha());
        return "fichas/formulario";
    }

    // Guardar ficha con imagen
    @PostMapping("/guardar")
    public String guardarFicha(@ModelAttribute Ficha ficha, @RequestParam("imagenFondo") MultipartFile imagenFondo,
            Model model) throws Exception {
        if (!imagenFondo.isEmpty()) {
            Blob imagen = imagenService.saveImage(imagenFondo);
            if (imagen != null) {
                ficha.setImagen(imagen);
            }
        }
        fichaRepository.save(ficha);

        model.addAttribute("mensaje", "Ficha guardada correctamente.");
        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarFicha(@PathVariable Long id, Model model,
            RedirectAttributes redirectAttributes) {
        Optional<Ficha> fichaOptional = fichaRepository.findById(id);
        if (fichaOptional.isPresent()) {
            model.addAttribute("ficha", fichaOptional.get());
            return "fichas/formulario";
        } else {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada.");
            return "redirect:/f";
        }
    }

    // Eliminar una ficha
    @GetMapping("/eliminar/{id}")
    public String eliminarFicha(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (fichaRepository.existsById(id)) {
            fichaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Ficha eliminada exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada.");
        }
        return "redirect:/f";
    }
}
