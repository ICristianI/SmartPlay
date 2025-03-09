package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.repository.FichaRepository;
import com.tfg.SmartPlay.repository.UserRepository;
import com.tfg.SmartPlay.service.ImagenService;
import com.tfg.SmartPlay.service.UserComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/f")
public class FichaController {

    @Autowired
    private FichaRepository fichaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImagenService imagenService; // Inyectar el servicio

    @Autowired
    private UserComponent userComponent;

    @GetMapping("/listarFichas")
    public String listarFichas(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User usuario = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Ficha> fichas = fichaRepository.findByUsuario(usuario);
        model.addAttribute("fichas", fichas);
        return "/Fichas/verFichas";
    }

    @GetMapping("/ver/{id}")
    public String verFicha(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Ficha> fichaOptional = fichaRepository.findById(id);
        if (fichaOptional.isPresent()) {
            model.addAttribute("ficha", fichaOptional.get());
            return "Fichas/verFicha"; // Esta es la vista donde se mostrarán los detalles de la ficha
        } else {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada.");
            return "redirect:/f/listarFichas"; // Redirige si no se encuentra la ficha
        }
    }

    @GetMapping("/ficha/image/{id}")
    public ResponseEntity<Object> downloadFichaImage(@PathVariable Long id) throws SQLException {
        Optional<Ficha> ficha = fichaRepository.findById(id);
        if (ficha.isPresent() && ficha.get().getImagen() != null) {
            return imagenService.getImageResponse(ficha.get().getImagen());
        }
        return ResponseEntity.notFound().build();
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
        ficha.setUsuario(userComponent.getUser().orElse(null));
        fichaRepository.save(ficha);

        model.addAttribute("mensaje", "Ficha guardada correctamente.");
        return "redirect:/f/listarFichas";
    }

    // Editar ficha
    @PostMapping("/editar/{id}")
    public String editarFicha(@PathVariable Long id, @ModelAttribute Ficha fichaEditada,
            Model model, RedirectAttributes redirectAttributes) {

        Optional<Ficha> fichaOptional = fichaRepository.findById(id);
        if (fichaOptional.isPresent()) {
            Ficha ficha = fichaOptional.get();
            // Actualizando los campos de la ficha
            ficha.setNombre(fichaEditada.getNombre());
            ficha.setIdioma(fichaEditada.getIdioma());
            ficha.setAsignatura(fichaEditada.getAsignatura());
            ficha.setContenido(fichaEditada.getContenido());
            ficha.setDescripcion(fichaEditada.getDescripcion());

            // Guardar la ficha editada
            fichaRepository.save(ficha);
            redirectAttributes.addFlashAttribute("mensaje", "Ficha editada correctamente.");
            return "redirect:/f/ver/" + id;
        } else {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada.");
            return "redirect:/f/listarFichas";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarFicha(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Ficha> fichaOptional = fichaRepository.findById(id);
        if (fichaOptional.isPresent()) {
            fichaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensaje", "Ficha eliminada exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada.");
        }
        return "redirect:/f/listarFichas";
    }
}
