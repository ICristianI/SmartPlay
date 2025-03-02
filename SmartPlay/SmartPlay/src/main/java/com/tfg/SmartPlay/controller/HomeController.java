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

    // lleva a pagina de decision de registro o inicio de sesion

    @GetMapping("/regin")
    public String regin(Model model) {
        return "RegistrarIniciarSesion/RegistrarOIniciar";
    }

    // lleva a pagina de registro

    @GetMapping("/signup")
    public String signup(Model model) {
        return "RegistrarIniciarSesion/Registrar";
    }

    // lleva a pagina de inicio de sesion
    @GetMapping("/login")
    public String signin(Model model) {
        return "RegistrarIniciarSesion/IniciarSesion";
    }

    // lleva a pagina de configuracion
    @GetMapping("/config")
    public String config(Model model) {
        return "Configuracion/configuration";
    }

    // lleva a pagina de seleccion de fichas o juegos
    @GetMapping("/fames")
    public String fames(Model model) {
        return "Fichas/FichasJuegos";
    }

    // lleva a pagina de juegos
    @GetMapping("/juegos")
    public String games(Model model) {
        return "Fichas/Juegos";
    }

    // lleva a pagina de fichas
    @GetMapping("/fichas")
    public String fichas(Model model) {
        return "Fichas/Fichas";
    }

    // lleva a pagina de crear fichas
    @GetMapping("/crearFichas")
    public String crearfichas(Model model) {
        return "Fichas/crearFichas";
    }

    

}
