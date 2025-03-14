package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.service.FichaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/f")
public class FichaController {

    @Autowired
    private FichaService fichaService;

    @GetMapping("/listarFichas")
    public String listarFichas(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Ficha> fichas = fichaService.listarFichas(userDetails.getUsername());
        model.addAttribute("fichas", fichas);
        return "/Fichas/verFichas";
    }

    @PostMapping("/seleccionarFicha")
    public String seleccionarFicha(@RequestParam("fichaId") Long fichaId, Model model, 
                                   @AuthenticationPrincipal UserDetails userDetails, 
                                   RedirectAttributes redirectAttributes) {
        Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());
        if (ficha.isPresent()) {
            model.addAttribute("ficha", ficha.get());
            return "Fichas/verFicha";
        } else {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada o sin permiso.");
            return "redirect:/f/listarFichas";
        }
    }

    @PostMapping("/editar")
    public String editarFicha(@RequestParam("fichaId") Long fichaId, Model model, 
                              @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute Ficha fichaEditada) {
        try {
            fichaService.editarFicha(fichaId, fichaEditada, userDetails.getUsername());
            Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());

            model.addAttribute("mensaje", "Ficha editada exitosamente.");
            model.addAttribute("ficha", ficha.get());
            return "Fichas/verFicha";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/f/listarFichas";
        }
    }

    @GetMapping("/ficha/image/{id}")
    public ResponseEntity<Object> downloadFichaImage(@PathVariable Long id) {
        return fichaService.obtenerImagenFicha(id);
    }

    @PostMapping("/guardar")
    public String guardarFicha(@ModelAttribute Ficha ficha, @RequestParam("imagenFondo") MultipartFile imagenFondo,
                               Model model) throws Exception {
        fichaService.guardarFicha(ficha, imagenFondo);
        model.addAttribute("mensaje", "Ficha guardada correctamente.");
        return "redirect:/f/listarFichas";
    }

    @PostMapping("/eliminar")
    public String eliminarFicha(@RequestParam("fichaId") Long fichaId, @AuthenticationPrincipal UserDetails userDetails, 
                                RedirectAttributes redirectAttributes) {
        try {
            fichaService.eliminarFicha(fichaId, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("mensaje", "Ficha eliminada exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/f/listarFichas";
    }
}
