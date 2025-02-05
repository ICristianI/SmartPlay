package com.tfg.SmartPlay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FichasController {

    @GetMapping("/ahorcado")
    public String redirigirAhorcado(Model model) {
        return "Juegos/Ahorcado";
    }

}
