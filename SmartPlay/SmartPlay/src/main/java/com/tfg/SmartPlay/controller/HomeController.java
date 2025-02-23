package com.tfg.SmartPlay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.tfg.SmartPlay.entity.User;

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
    
    //lleva a pagina de configuracion
    @GetMapping("/config")
    public String config(Model model) {
        return "Configuracion/configuration";    
    }

    @GetMapping("/fames")
    public String fames(Model model) {
        return "Fichas/FichasJuegos";    
    }

    @GetMapping("/ImagenPrueba")
    public String IM(Model model) {
        return "ImagenPrueba";    
    }
    
    @GetMapping("/juegos")
    public String games(Model model) {
        return "Fichas/Juegos";    
    }

    
    @GetMapping("/fichas")
    public String fichas(Model model) {
        return "Fichas/Fichas";    
    }

    @GetMapping("/crearFichas")
    public String crearfichas(Model model) {
        return "Fichas/crearFichas";    
    }

}
