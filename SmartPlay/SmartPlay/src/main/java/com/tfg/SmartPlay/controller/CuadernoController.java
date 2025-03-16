package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.entity.Ficha;
import com.tfg.SmartPlay.entity.User;
import com.tfg.SmartPlay.service.CuadernoService;
import com.tfg.SmartPlay.service.FichaService;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cuadernos")
public class CuadernoController {

    @Autowired
    private CuadernoService cuadernoService;

    @Autowired
    private UserComponent userComponent;

    @Autowired
    private FichaService fichaService;

    /**
     * Lista los cuadernos del usuario autenticado.
     */
    @GetMapping
    public String listarCuadernos(Model model,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page) {
    
        int size = 6; // Tamaño de la paginación
    
        // Obtener cuadernos paginados
        Page<Cuaderno> cuadernosPage = cuadernoService.listarCuadernosPaginados(userDetails.getUsername(), page, size);
    
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
    
        return "Cuadernos/verCuadernos";
    }
    

    /**
     * POST: Guarda el cuadernoId en sesión y redirige a GET /ver.
     */
    @PostMapping("/ver")
    public String verCuadernoPost(@RequestParam("cuadernoId") Long cuadernoId, HttpSession session) {
        session.setAttribute("cuadernoId", cuadernoId);
        return "redirect:/cuadernos/ver";
    }

    /**
     * GET: Obtiene el cuaderno desde sesión y lo muestra.
     */
    @GetMapping("/ver")
    public String verCuadernoGet(Model model,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page, // Página actual
            @RequestParam(defaultValue = "3") int size,
            RedirectAttributes redirectAttributes) {

        Long cuadernoId = (Long) session.getAttribute("cuadernoId");

        if (cuadernoId == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el cuaderno.");
            return "redirect:/cuadernos";
        }

        Optional<Cuaderno> cuaderno = cuadernoService.obtenerCuadernoPorIdYUsuario(cuadernoId,
                userDetails.getUsername());

        if (cuaderno.isPresent()) {
            Cuaderno cuadernoObj = cuaderno.get();

            // Obtener fichas no agregadas para el modal
            List<Ficha> fichasNoAgregadas = cuadernoService.obtenerFichasNoAgregadas(cuadernoId,
                    userDetails.getUsername());

            // Obtener fichas paginadas
            Page<Ficha> fichasPage = fichaService.obtenerFichasPaginadas(cuadernoId, page, size);

            int totalPages = fichasPage.getTotalPages();
            boolean hasPrev = page > 0;
            boolean hasNext = page < totalPages - 1;
    
            int prevPage = hasPrev ? page - 1 : 0;
            int nextPage = hasNext ? page + 1 : page;

            model.addAttribute("prevPage", prevPage);
            model.addAttribute("nextPage", nextPage);
            model.addAttribute("hasPrev", hasPrev);
            model.addAttribute("hasNext", hasNext);
            model.addAttribute("currentPage", page+1);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("size", size);

            model.addAttribute("cuaderno", cuadernoObj);
            model.addAttribute("fichasPage", fichasPage.getContent());
            model.addAttribute("totalPages", fichasPage.getTotalPages());
            model.addAttribute("fichasNoAgregadas", fichasNoAgregadas);

            return "Cuadernos/verCuaderno";
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para ver este cuaderno.");
            return "redirect:/cuadernos";
        }
    }

    /**
     * Muestra el formulario para crear un nuevo cuaderno.
     */
    @GetMapping("/crear")
    public String mostrarFormularioCreacion(Model model) {
        User usuario = userComponent.getUser().get();
        model.addAttribute("cuaderno", new Cuaderno());
        model.addAttribute("fichas", fichaService.obtenerFichasPorUsuario(usuario.getEmail()));
        return "Cuadernos/crearCuadernos";
    }

    /**
     * Guarda un nuevo cuaderno.
     */
    @PostMapping("/guardar")
    public String guardarCuaderno(@ModelAttribute Cuaderno cuaderno,
            @RequestParam("fichasSeleccionadas") List<Long> fichasIds,
            RedirectAttributes redirectAttributes) {
        cuadernoService.guardarCuaderno(cuaderno, fichasIds);
        redirectAttributes.addFlashAttribute("mensaje", "Cuaderno guardado correctamente.");
        return "redirect:/cuadernos";
    }

    @GetMapping("/fichasDisponibles")
    @ResponseBody
    public List<Ficha> obtenerFichasDisponibles(@RequestParam("cuadernoId") Long cuadernoId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return cuadernoService.obtenerFichasNoAgregadas(cuadernoId, userDetails.getUsername());
    }

    @PostMapping("/editar")
    public String editarCuaderno(@RequestParam("cuadernoId") Long cuadernoId,
            @RequestParam("nombre") String nuevoNombre,
            @RequestParam(value = "fichasSeleccionadas", required = false) List<Long> fichasIds,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        boolean editado = cuadernoService.editarCuaderno(cuadernoId, nuevoNombre, fichasIds, userDetails.getUsername());

        if (editado) {
            redirectAttributes.addFlashAttribute("mensaje", "Cuaderno editado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permiso para editar este cuaderno.");
        }

        return "redirect:/cuadernos/ver";
    }

    /**
     * Elimina una ficha del cuaderno sin exponer el ID en la URL.
     */
    @PostMapping("/eliminarFicha")
    public String eliminarFichaDeCuaderno(@RequestParam("fichaId") Long fichaId,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        Long cuadernoId = (Long) session.getAttribute("cuadernoId");

        if (cuadernoId == null) {
            redirectAttributes.addFlashAttribute("error", "No se encontró el cuaderno.");
            return "redirect:/cuadernos";
        }

        boolean eliminado = cuadernoService.eliminarFichaDeUsuario(fichaId, cuadernoId, userDetails.getUsername());

        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Ficha eliminada correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar la ficha.");
        }

        return "redirect:/cuadernos/ver";
    }

    /**
     * Elimina un cuaderno sin exponer el ID en la URL.
     */

    @PostMapping("/eliminar")
    public String eliminarCuaderno(@RequestParam("cuadernoId") Long cuadernoId,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        boolean eliminado = cuadernoService.eliminarCuaderno(cuadernoId, userDetails.getUsername());

        if (eliminado) {
            redirectAttributes.addFlashAttribute("mensaje", "Cuaderno eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar este cuaderno.");
        }

        return "redirect:/cuadernos";
    }

}
