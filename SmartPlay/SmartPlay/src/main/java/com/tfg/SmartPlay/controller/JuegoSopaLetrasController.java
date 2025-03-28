package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.JuegoSopaLetras;
import com.tfg.SmartPlay.service.JuegoService;
import com.tfg.SmartPlay.service.JuegoSopaLetrasService;
import com.tfg.SmartPlay.service.UserComponent;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/sopaletras")
public class JuegoSopaLetrasController {


    @Autowired
    private JuegoService juegoService;
    
    @Autowired
    private JuegoSopaLetrasService juegoSopaLetrasService;

    @Autowired
    private UserComponent userComponent;

    @GetMapping("/listar")
    public String listarJuegos(Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page) {

        int size = 6;

        Page<JuegoSopaLetras> juegosPage = juegoSopaLetrasService
                .obtenerJuegosPaginadosPorUsuario(userDetails.getUsername(), page, size);

        boolean pages = juegosPage.getTotalPages() > 0;

        model.addAttribute("pages", pages);
        model.addAttribute("juegos", juegosPage.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", juegosPage.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < juegosPage.getTotalPages() - 1);
        model.addAttribute("prevPage", page > 0 ? page - 1 : 0);
        model.addAttribute("nextPage", page < juegosPage.getTotalPages() - 1 ? page + 1 : page);

        return "/Juegos/Sopa/verJuegosSopaLetras";
    }

    @GetMapping("/jugar")
    public String jugarSopaLetras(Model model, HttpSession session) {
        Long juegoId = (Long) session.getAttribute("juegoId");
        Optional<JuegoSopaLetras> juegoOpt = juegoSopaLetrasService.obtenerJuego(juegoId, userComponent.getUser().get().getEmail());

        if (juegoOpt.isPresent()) {
            JuegoSopaLetras juego = juegoOpt.get();
            model.addAttribute("juego", juego);
            return "Juegos/Sopa/JugarSopa";
        } else {
            model.addAttribute("error", "El juego no existe.");
            return "redirect:/sopaletras/listar";
        }
    }

    @GetMapping("/ver")
    public String verJuego(HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            @RequestParam(name = "pageCuadernos", defaultValue = "0") int pageCuadernos,
            RedirectAttributes redirectAttributes) {

        Long juegoId = (Long) session.getAttribute("juegoId");

        if (juegoId == null) {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado.");
            return "redirect:/sopaletras/listar";
        }

        Optional<JuegoSopaLetras> juego = juegoSopaLetrasService.obtenerJuego(juegoId, userDetails.getUsername());

        if (juego.isPresent()) {
            model.addAttribute("juego", juego.get());

            int size = 3;
            Page<Cuaderno> cuadernosPage = juegoSopaLetrasService.obtenerCuadernosConJuegoPaginados(juego.get(), pageCuadernos, size);

            boolean pages = cuadernosPage.getTotalPages() > 0;

            model.addAttribute("pages", pages);
            model.addAttribute("cuadernos", cuadernosPage.getContent());
            model.addAttribute("currentPageCuadernos", pageCuadernos + 1);
            model.addAttribute("totalPagesCuadernos", cuadernosPage.getTotalPages());
            model.addAttribute("hasPrevCuadernos", pageCuadernos > 0);
            model.addAttribute("hasNextCuadernos", pageCuadernos < cuadernosPage.getTotalPages() - 1);
            model.addAttribute("prevPageCuadernos", pageCuadernos > 0 ? pageCuadernos - 1 : 0);
            model.addAttribute("nextPageCuadernos", pageCuadernos < cuadernosPage.getTotalPages() - 1 ? pageCuadernos + 1 : pageCuadernos);

            return "Juegos/Sopa/verJuegoSopaLetras";
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para ver este juego.");
            return "redirect:/sopaletras/listar";
        }
    }

    @PostMapping("/editar")
    public String editarJuego(@RequestParam("juegoId") Long juegoId,
            @ModelAttribute JuegoSopaLetras juegoEditado,
            @AuthenticationPrincipal UserDetails userDetails,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            juegoSopaLetrasService.editarJuego(juegoId, juegoEditado, userDetails.getUsername());

            session.setAttribute("juegoId", juegoId);
            redirectAttributes.addFlashAttribute("mensaje", "Juego editado exitosamente.");

            return "redirect:/sopaletras/ver";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/sopaletras/listar";
        }
    }

    @GetMapping("/crear")
    public String crearJuego(Model model) {
        model.addAttribute("juego", new JuegoSopaLetras());
        return "Juegos/Sopa/crearJuegosSopaLetras";
    }

    @PostMapping("/guardar")
    public String guardarJuego(@ModelAttribute JuegoSopaLetras juego,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        juegoSopaLetrasService.guardarJuego(juego, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("mensaje", "Juego guardado correctamente.");
        return "redirect:/sopaletras/listar";
    }

    @PostMapping("/eliminar")
    public String eliminarJuego(@RequestParam("juegoId") Long juegoId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        Optional<JuegoSopaLetras> juego = juegoSopaLetrasService.obtenerJuego(juegoId, userDetails.getUsername());

        if (juego.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado.");
            return "redirect:/sopaletras/listar";
        }

        try {
            juegoService.eliminarJuego(juegoId, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("mensaje", "Juego eliminado exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/sopaletras/listar";
    }

}
