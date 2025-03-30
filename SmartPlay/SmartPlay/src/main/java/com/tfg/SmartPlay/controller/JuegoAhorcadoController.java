package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.service.JuegoAhorcadoService;
import com.tfg.SmartPlay.service.JuegoService;
import com.tfg.SmartPlay.service.UserComponent;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/ahorcado")
public class JuegoAhorcadoController {

    @Autowired
    private JuegoService juegoService;

    @Autowired
    private JuegoAhorcadoService juegoAhorcadoService;

    @Autowired
    private UserComponent userComponent;

    @GetMapping("/listar")
    public String listarJuegos(Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page) {

        int size = 6;

        Page<JuegoAhorcado> juegosPage = juegoAhorcadoService
                .obtenerJuegosPaginadosPorUsuario(userDetails.getUsername(), page, size);

        boolean pages = juegosPage.getTotalPages() > 0;

        List<Map<String, Object>> juegosProcesados = juegosPage.getContent().stream().map(juego -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", juego.getId());
            map.put("nombre", juego.getNombre());
            map.put("asignatura", juego.getAsignatura());
            map.put("maxIntentos", juego.getMaxIntentos());
            map.put("meGusta", juego.getMeGusta());
            map.put("fechaFormateada", juego.getFechaCreacionFormateada());
            return map;
        }).toList();

        model.addAttribute("juegos", juegosProcesados);
        model.addAttribute("pages", pages);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", juegosPage.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < juegosPage.getTotalPages() - 1);
        model.addAttribute("prevPage", page > 0 ? page - 1 : 0);
        model.addAttribute("nextPage", page < juegosPage.getTotalPages() - 1 ? page + 1 : page);

        return "/Juegos/Ahorcado/verJuegosAhorcados";
    }


    @GetMapping("/jugar")
    public String jugarAhorcado(Model model, HttpSession session) {
        Long juegoId = (Long) session.getAttribute("juegoId");
        Optional<JuegoAhorcado> juegoOpt = juegoAhorcadoService.obtenerJuego(juegoId, userComponent.getUser().get().getEmail());
    
        if (juegoOpt.isPresent()) {
            JuegoAhorcado juego = juegoOpt.get();
            model.addAttribute("juego", juego);
            return "Juegos/Ahorcado/JugarAhorcado";
        } else {
            model.addAttribute("error", "El juego no existe.");
            return "redirect:/ahorcado/listar";
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
            return "redirect:/ahorcado/listar";
        }

        Optional<JuegoAhorcado> juego = juegoAhorcadoService.obtenerJuego(juegoId, userDetails.getUsername());

        if (juego.isPresent()) {
            model.addAttribute("juego", juego.get());

            int size = 3;
            Page<Cuaderno> cuadernosPage = juegoAhorcadoService.obtenerCuadernosConJuegoPaginados(juego.get(), pageCuadernos,
                    size);

        boolean pages = cuadernosPage.getTotalPages() > 0;
        int totalPagesCuadernos = cuadernosPage.getTotalPages();
        boolean hasPrevCuadernos = pageCuadernos > 0;
        boolean hasNextCuadernos = pageCuadernos < totalPagesCuadernos - 1;
        int prevPageCuadernos = hasPrevCuadernos ? pageCuadernos - 1 : 0;
        int nextPageCuadernos = hasNextCuadernos ? pageCuadernos + 1 : pageCuadernos;

        model.addAttribute("pages", pages);
        model.addAttribute("cuadernos", cuadernosPage.getContent());
        model.addAttribute("currentPageCuadernos", pageCuadernos + 1);
        model.addAttribute("totalPagesCuadernos", totalPagesCuadernos);
        model.addAttribute("hasPrevCuadernos", hasPrevCuadernos);
        model.addAttribute("hasNextCuadernos", hasNextCuadernos);
        model.addAttribute("prevPageCuadernos", prevPageCuadernos);
        model.addAttribute("nextPageCuadernos", nextPageCuadernos);
        model.addAttribute("fechaFormateada", juego.get().getFechaCreacionFormateada());


            return "Juegos/Ahorcado/verJuegoAhorcado";
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para ver este juego.");
            return "redirect:/ahorcado/listar";
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

            return "redirect:/ahorcado/ver";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/ahorcado/listar";
        }
    }

    @GetMapping("/crear")
    public String crearJuego(Model model) {
        model.addAttribute("juego", new JuegoAhorcado());
        return "Juegos/Ahorcado/crearJuegosAhorcados";
    }

@PostMapping("/guardar")
public String guardarJuego(@ModelAttribute JuegoAhorcado juego,
                           @RequestParam("imagenJuego") MultipartFile imagenJuego,
                           @AuthenticationPrincipal UserDetails userDetails,
                           RedirectAttributes redirectAttributes) {

    juegoAhorcadoService.guardarJuego(juego, userDetails.getUsername(), imagenJuego);
    redirectAttributes.addFlashAttribute("mensaje", "Juego guardado correctamente.");
    return "redirect:/ahorcado/listar";
}


    @PostMapping("/eliminar")
    public String eliminarJuego(@RequestParam("juegoId") Long juegoId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        Optional<JuegoAhorcado> juego = juegoAhorcadoService.obtenerJuego(juegoId, userDetails.getUsername());

        if (juego.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado.");
            return "redirect:/ahorcado/listar";
        }

        try {
            juegoService.eliminarJuego(juegoId, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("mensaje", "Juego eliminado exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/ahorcado/listar";
    }

}
