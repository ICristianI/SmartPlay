package com.tfg.SmartPlay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.entity.JuegoCrucigrama;
import com.tfg.SmartPlay.entity.JuegoSopaLetras;
import com.tfg.SmartPlay.service.JuegoService;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/juegos")
public class JuegoController {

    @Autowired
    JuegoService juegoService;

    @PostMapping("/seleccionar")
    public String seleccionarJuego(@RequestParam("juegoId") Long juegoId,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        Optional<Juego> juego = juegoService.obtenerJuegoPorId(juegoId);

        if (juego.isPresent()) {
            String tipo;

            if (juego.get() instanceof JuegoAhorcado) {
                tipo = "ahorcado";
            } else if (juego.get() instanceof JuegoSopaLetras) {
                tipo = "sopaletras";
            } else if (juego.get() instanceof JuegoCrucigrama) {
                tipo = "crucigrama";
            } else {
                redirectAttributes.addFlashAttribute("error", "Juego no encontrado o sin permiso.");
                return "redirect:/juegos";
            }

            session.setAttribute("juegoId", juegoId);
            return "redirect:/" + tipo + "/ver";
        } else {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado o sin permiso.");
            return "redirect:/juegos";
        }
    }

    @PostMapping("/explorador")
    public String postMethodName(@RequestParam("juegoId") Long juegoId,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        Optional<Juego> juego = juegoService.obtenerJuegoPorId(juegoId);

        if (juego.isPresent()) {
            String tipo;

            if (juego.get() instanceof JuegoAhorcado) {
                tipo = "ahorcado";
            } else if (juego.get() instanceof JuegoSopaLetras) {
                tipo = "sopaletras";
            } else if (juego.get() instanceof JuegoCrucigrama) {
                tipo = "crucigrama";
            } else {
                redirectAttributes.addFlashAttribute("error", "Juego no encontrado o sin permiso.");
                return "redirect:/juegos";
            }

            session.setAttribute("juegoId", juegoId);
            return "redirect:/" + tipo + "/jugar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado o sin permiso.");
            return "redirect:/juegos";
        }
    }

    @GetMapping("/investigar")
    public String verJuegosPublicos(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String buscar,
            @RequestParam(required = false, defaultValue = "fecha") String orden) {
    
        int size = 24;
    
        Page<Juego> juegosPage;
    
        if ("popularidad".equalsIgnoreCase(orden)) {
            juegosPage = juegoService.ordenarPorMeGusta(buscar, page, size);
        } else {
            juegosPage = juegoService.ordenarPorFecha(buscar, page, size);
        }
    
        List<Map<String, Object>> juegosProcesados = juegosPage.getContent().stream().map(juego -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", juego.getId());
            map.put("nombre", juego.getNombre());
            map.put("asignatura", juego.getAsignatura());
            map.put("meGusta", juego.getMeGusta());
            map.put("fechaFormateada", juego.getFechaCreacionFormateada());
            return map;
        }).toList();
    
        model.addAttribute("juegos", juegosProcesados);
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", juegosPage.getTotalPages());
        model.addAttribute("hasPrev", page > 0);
        model.addAttribute("hasNext", page < juegosPage.getTotalPages() - 1);
        model.addAttribute("prevPage", page > 0 ? page - 1 : 0);
        model.addAttribute("nextPage", page < juegosPage.getTotalPages() - 1 ? page + 1 : page);
        model.addAttribute("pages", juegosPage.getTotalPages() > 0);
        model.addAttribute("buscar", buscar != null ? buscar : "");
        model.addAttribute("ordenFecha", "fecha".equalsIgnoreCase(orden));
        model.addAttribute("ordenPopularidad", "popularidad".equalsIgnoreCase(orden));
    
        return "jugar";
    }
    

    @GetMapping("/image/{id}")
    public ResponseEntity<Object> obtenerImagenJuego(@PathVariable Long id) {
        Optional<Juego> juegoOpt = juegoService.obtenerJuegoPorId(id);

        if (juegoOpt.isPresent() && juegoOpt.get().getImagen() != null) {
            try {
                return juegoService.obtenerImagenJuego(juegoOpt.get().getImagen());
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body("Error al recuperar la imagen");
            }
        }
        return ResponseEntity.notFound().build();
    }

}
