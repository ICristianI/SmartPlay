package com.tfg.SmartPlay.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



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
    public String signin(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "RegistrarIniciarSesion/IniciarSesion";
    }

    @GetMapping("/loginerror")
    public String loginerror() {

        return "redirect:/login?error=" + URLEncoder.encode("Contraseña Incorrecta", StandardCharsets.UTF_8);
    }

    @GetMapping("/verify")
    public String getMethodName(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "message", required = false) String message, Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "RegistrarIniciarSesion/Verificar";
    }

    // lleva a pagina de configuracion
    @GetMapping("/config")
    public String config(Model model) {
        return "Configuracion/configuration";
    }

    // lleva a pagina de seleccion de fichas o juegos
    @GetMapping("/creacion")
    public String creacion(Model model) {
        return "Creacion";
    }

    // lleva a pagina de fichas
    @GetMapping("/fichas")
    public String fichas(Model model) {
        return "Fichas/Fichas";
    }

    // lleva a pagina de crear fichas
    @GetMapping("/verFichas")
    public String verFichas(Model model) {
        return "Fichas/verFichas";
    }

    @GetMapping("/Cuadernos")
    public String Cuadernos(Model model) {
        return "Cuadernos/Cuadernos";
    }

    @GetMapping("/crearCuadernos")
    public String crearCuadernos(Model model) {
        return "Cuadernos/crearCuadernos";
    }

    @GetMapping("/juegos")
    public String juegos(Model model) {
        return "Juegos/Juegos";
    }

    @GetMapping("/ahorcado")
    public String ahorcado(Model model) {
        return "Juegos/Ahorcado/Ahorcado";
    }

    
    @GetMapping("/sopa")
    public String sopa(Model model) {
        return "Juegos/Sopa/Sopa";
    }

    @GetMapping("/crucigrama")
    public String crucigrama(Model model) {
        return "Juegos/Crucigrama/Crucigrama";
    }

    @GetMapping("/grupo")
    public String grupo(Model model) {
        return "Grupos/Grupos";
    }

    @GetMapping("/crearGrupos")
    public String crearGrupos(Model model) {
        return "Grupos/crearGrupos";
    }

    @GetMapping("/verGrupos")
    public String verGrupos(Model model) {
        return "Grupos/verGrupos";
    }


}
