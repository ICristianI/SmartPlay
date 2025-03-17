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
import org.springframework.data.domain.Page;

import java.util.Optional;

// Controlador de las fichas

@Controller
@RequestMapping("/f")
public class FichaController {

    @Autowired
    private FichaService fichaService;

    @Autowired
    private CuadernoService cuadernoService;

    // Método para listar las fichas de un usuario

    @GetMapping("/listarFichas")
    public String listarFichas(Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page) {

        int size = 6;

        Page<Ficha> fichasPage = fichaService.obtenerFichasPaginadasPorUsuario(userDetails.getUsername(), page, size);

        int totalPages = fichasPage.getTotalPages();
        boolean hasPrev = page > 0;
        boolean hasNext = page < totalPages - 1;
        int prevPage = hasPrev ? page - 1 : 0;
        int nextPage = hasNext ? page + 1 : page;

        model.addAttribute("fichas", fichasPage.getContent());
        model.addAttribute("currentPage", page + 1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);

        return "/Fichas/verFichas";
    }

    // Método para seleccionar una ficha sin revelar id en la URL

    @PostMapping("/seleccionarFicha")
    public String seleccionarFicha(@RequestParam("fichaId") Long fichaId,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());

        if (ficha.isPresent()) {
            session.setAttribute("fichaId", fichaId);
            return "redirect:/f/verFicha";
        } else {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada o sin permiso.");
            return "redirect:/f/listarFichas";
        }
    }

    // Método para ver una ficha que lleva a la template

    @GetMapping("/verFicha")
    public String verFichaDesdeSesion(HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            @RequestParam(defaultValue = "0") int page,
            RedirectAttributes redirectAttributes) {

        Long fichaId = (Long) session.getAttribute("fichaId");

        if (fichaId == null) {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada.");
            return "redirect:/f/listarFichas";
        }

        Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());

        if (ficha.isPresent()) {
            model.addAttribute("ficha", ficha.get());

            int size = 3;
            Page<Cuaderno> cuadernosPage = cuadernoService.obtenerCuadernosConFichaPaginados(ficha.get(), page, size);

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

            return "Fichas/verFicha";
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para ver esta ficha.");
            return "redirect:/f/listarFichas";
        }
    }

    // Método que lleva a la template de crear fichas

    @GetMapping("/crearFichas")
    public String crearfichas(Model model) {
        return "Fichas/crearFichas";
    }

    // Método para guardar una ficha

    @PostMapping("/guardar")
    public String guardarFicha(@ModelAttribute Ficha ficha, @RequestParam("imagenFondo") MultipartFile imagenFondo,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) throws Exception {
        fichaService.guardarFicha(ficha, imagenFondo, userDetails.getUsername());
        redirectAttributes.addFlashAttribute("mensaje", "Ficha guardada correctamente.");
        return "redirect:/f/listarFichas";
    }

    // Método para editar una ficha

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
    
    // Método para eliminar una ficha

    @PostMapping("/eliminar")
    public String eliminarFicha(@RequestParam("fichaId") Long fichaId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        Optional<Ficha> ficha = fichaService.obtenerFicha(fichaId, userDetails.getUsername());

        if (ficha.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Ficha no encontrada.");
            return "redirect:/f/listarFichas";
        }

        try {
            fichaService.eliminarFicha(fichaId, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("mensaje", "Ficha eliminada exitosamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/f/listarFichas";
    }

    // Método para obtener la imagen de una ficha

    @GetMapping("/ficha/image/{id}")
    public ResponseEntity<Object> downloadFichaImage(@PathVariable Long id) {
        return fichaService.obtenerImagenFicha(id);
    }


}
