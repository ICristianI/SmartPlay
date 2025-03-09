package com.tfg.SmartPlay.controller;

import com.tfg.SmartPlay.entity.Cuaderno;
import com.tfg.SmartPlay.repository.CuadernoRepository;
import com.tfg.SmartPlay.service.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/c")
public class CuadernoController {

    @Autowired
    private CuadernoRepository cuadernoRepository;

    @Autowired
    private UserComponent userComponent;

    // Listar cuadernos
    @GetMapping
    public String listarCuadernos(Model model) {
        model.addAttribute("cuadernos", cuadernoRepository.findAll());
        return "cuadernos/lista";
    }

    // Mostrar formulario de creación
    @GetMapping("/nuevo")
    public String mostrarFormularioCreacion(Model model) {
        model.addAttribute("cuaderno", new Cuaderno());
        return "cuadernos/formulario";
    }

    // Guardar cuaderno
    @PostMapping("/guardar")
    public String guardarCuaderno(@ModelAttribute Cuaderno cuaderno) {
        cuaderno.setUsuario(userComponent.getUser().orElse(null));
        cuadernoRepository.save(cuaderno);
        return "redirect:/cuadernos";
    }

    // Mostrar formulario de edición
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Optional<Cuaderno> cuaderno = cuadernoRepository.findById(id);
        if (cuaderno.isPresent()) {
            model.addAttribute("cuaderno", cuaderno.get());
            return "cuadernos/formulario";
        }
        return "redirect:/cuadernos";
    }

    // Eliminar cuaderno
    @PostMapping("/eliminar/{id}")
    public String eliminarCuaderno(@PathVariable Long id) {
        cuadernoRepository.deleteById(id);
        return "redirect:/cuadernos";
    }
}
