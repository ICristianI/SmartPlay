package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.service.CuadernoService;
import com.tfg.SmartPlay.service.FichaService;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private CuadernoService cuadernoService;

    @GetMapping("/listarFichas")
    public String listarFichas(Model model, @AuthenticationPrincipal UserDetails userDetails, HttpSession session) {
        List<Ficha> fichas = fichaService.listarFichas(userDetails.getUsername());
        model.addAttribute("fichas", fichas);
        session.removeAttribute("fichaId");
        return "/Fichas/verFichas";
    }

    @PostMapping("/seleccionarFicha")
    public String seleccionarFicha(@RequestParam("fichaId") Long fichaId,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());

        if (ficha.isPresent()) {
            session.setAttribute("fichaId", fichaId);
            return "redirect:/f/verFicha"; // Ahora redirige al GET para obtener los datos.
        } else {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada o sin permiso.");
            return "redirect:/f/listarFichas";
        }
    }

    @GetMapping("/verFicha")
    public String verFichaDesdeSesion(HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            RedirectAttributes redirectAttributes) {
        Long fichaId = (Long) session.getAttribute("fichaId");

        if (fichaId == null) {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada.");
            return "redirect:/f/listarFichas";
        }

        Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());

        if (ficha.isPresent()) {
            model.addAttribute("ficha", ficha.get());

            // Obtener cuadernos que contienen esta ficha
            List<Cuaderno> cuadernos = cuadernoService.obtenerCuadernosConFicha(ficha.get());
            model.addAttribute("cuadernos", cuadernos);

            return "Fichas/verFicha";
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para ver esta ficha.");
            return "redirect:/f/listarFichas";
        }
    }

    @PostMapping("/editar")
    public String editarFicha(@RequestParam("fichaId") Long fichaId,
            @ModelAttribute Ficha fichaEditada,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            fichaService.editarFicha(fichaId, fichaEditada, userDetails.getUsername());

            session.setAttribute("fichaId", fichaId);
            redirectAttributes.addFlashAttribute("mensaje", "Ficha editada exitosamente.");

            return "redirect:/f/verFicha";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/f/listarFichas";
        }
    }

    @GetMapping("/ficha/image/{id}")
    public ResponseEntity<Object> downloadFichaImage(@PathVariable Long id) {
        return fichaService.obtenerImagenFicha(id);
    }

    @PostMapping("/guardar")
    public String guardarFicha(@ModelAttribute Ficha ficha, @RequestParam("imagenFondo") MultipartFile imagenFondo,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) throws Exception {
        fichaService.guardarFicha(ficha, imagenFondo, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("mensaje", "Ficha guardada correctamente.");
        return "redirect:/f/listarFichas";
    }

    @PostMapping("/eliminar")
    public String eliminarFicha(@AuthenticationPrincipal UserDetails userDetails,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Ficha ficha = (Ficha) session.getAttribute("fichaSeleccionada");

        if (ficha == null) {
            redirectAttributes.addFlashAttribute("error", "No hay ficha seleccionada.");
            return "redirect:/f/listarFichas";
        }

        try {
            fichaService.eliminarFicha(ficha.getId(), userDetails.getUsername());
            session.removeAttribute("fichaSeleccionada"); // Quitamos de la sesión
            redirectAttributes.addFlashAttribute("mensaje", "Ficha eliminada exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/f/listarFichas";
    }
}
