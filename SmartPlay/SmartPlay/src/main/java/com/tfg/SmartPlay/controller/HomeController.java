package com.tfg.SmartPlay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";    
    }

    //lleva a pagina de decision de registro o inicio de sesion

    @GetMapping("/regin")
    public String regin(Model model) {
        return "RegistrarIniciarSesion/RegistrarOIniciar";
    }

    //lleva a pagina de registro

    @GetMapping("/signup")
    public String signup(Model model) {
        return "RegistrarIniciarSesion/Registrar";
    }

    //lleva a pagina de inicio de sesion
    @GetMapping("/signin")
    public String signin(Model model) {
        return "RegistrarIniciarSesion/IniciarSesion";
    }

}
