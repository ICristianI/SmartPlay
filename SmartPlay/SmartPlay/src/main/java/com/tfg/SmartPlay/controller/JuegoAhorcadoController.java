package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.service.JuegoAhorcadoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;

import java.util.Optional;

@Controller
@RequestMapping("/juegos/ahorcado")
public class JuegoAhorcadoController {

    @Autowired
    private JuegoAhorcadoService juegoAhorcadoService;

    @GetMapping("/listar")
    public String listarJuegos(Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page) {

        int size = 6; // Tamaño de la paginación

        Page<JuegoAhorcado> juegosPage = juegoAhorcadoService
                .obtenerJuegosPaginadosPorUsuario(userDetails.getUsername(), page, size);

        model.addAttribute("juegos", juegosPage.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", juegosPage.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < juegosPage.getTotalPages() - 1);
        model.addAttribute("prevPage", page > 0 ? page - 1 : 0);
        model.addAttribute("nextPage", page < juegosPage.getTotalPages() - 1 ? page + 1 : page);

        return "/Juegos/verJuegosAhorcados";
    }

    @PostMapping("/seleccionar")
    public String seleccionarJuego(@RequestParam("juegoId") Long juegoId,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        Optional<JuegoAhorcado> juego = juegoAhorcadoService.obtenerJuego(juegoId, userDetails.getUsername());

        if (juego.isPresent()) {
            session.setAttribute("juegoId", juegoId);
            return "redirect:/juegos/ahorcado/ver";
        } else {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado o sin permiso.");
            return "redirect:/juegos/ahorcado/listar";
        }
    }

    @GetMapping("/ver")
    public String verJuego(HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            @RequestParam(defaultValue = "0") int page, // Página actual
            RedirectAttributes redirectAttributes) {

        Long juegoId = (Long) session.getAttribute("juegoId");

        if (juegoId == null) {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado.");
            return "redirect:/juegos/ahorcado/listar";
        }

        Optional<JuegoAhorcado> juego = juegoAhorcadoService.obtenerJuego(juegoId, userDetails.getUsername());

        if (juego.isPresent()) {
            model.addAttribute("juego", juego.get());

            // Obtener cuadernos paginados que contienen este juego
            int size = 3; // Tamaño de la paginación
            Page<Cuaderno> cuadernosPage = juegoAhorcadoService.obtenerCuadernosConJuegoPaginados(juego.get(), page,
                    size);

            int totalPages = cuadernosPage.getTotalPages();
            boolean hasPrev = page > 0;
            boolean hasNext = page < totalPages - 1;
            int prevPage = hasPrev ? page - 1 : 0;
            int nextPage = hasNext ? page + 1 : page;

            model.addAttribute("cuadernos", cuadernosPage.getContent());
            model.addAttribute("currentPage", page + 1);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("hasPrev", hasPrev);
            model.addAttribute("hasNext", hasNext);
            model.addAttribute("prevPage", prevPage);
            model.addAttribute("nextPage", nextPage);

            return "Juegos/verJuegoAhorcado";
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para ver este juego.");
            return "redirect:/juegos/ahorcado/listar";
        }
    }

    @PostMapping("/editar")
    public String editarJuego(@RequestParam("juegoId") Long juegoId,
            @ModelAttribute JuegoAhorcado juegoEditado,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            juegoAhorcadoService.editarJuego(juegoId, juegoEditado, userDetails.getUsername());

            session.setAttribute("juegoId", juegoId);
            redirectAttributes.addFlashAttribute("mensaje", "Juego editado exitosamente.");

            return "redirect:/juegos/ahorcado/ver";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/juegos/ahorcado/listar";
        }
    }

    @GetMapping("/crear")
    public String crearJuego(Model model) {
        model.addAttribute("juego", new JuegoAhorcado());
        return "Juegos/crearJuegosAhorcados";
    }

    @PostMapping("/guardar")
    public String guardarJuego(@ModelAttribute JuegoAhorcado juego,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        juegoAhorcadoService.guardarJuego(juego, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("mensaje", "Juego guardado correctamente.");
        return "redirect:/juegos/ahorcado/listar";
    }

    @PostMapping("/eliminar")
    public String eliminarJuego(@RequestParam("juegoId") Long juegoId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        Optional<JuegoAhorcado> juego = juegoAhorcadoService.obtenerJuego(juegoId, userDetails.getUsername());

        if (juego.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado.");
            return "redirect:/juegos/ahorcado/listar";
        }

        try {
            juegoAhorcadoService.eliminarJuego(juegoId, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("mensaje", "Juego eliminado exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/juegos/ahorcado/listar";
    }

}
