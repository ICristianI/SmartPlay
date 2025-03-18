package com.tfg.SmartPlay.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tfg.SmartPlay.entity.Juego;
import com.tfg.SmartPlay.entity.JuegoAhorcado;
import com.tfg.SmartPlay.service.JuegoService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/juegos")
public class JuegoController {

    @Autowired
    JuegoService juegoService;


    @PostMapping("/seleccionar")
    public String seleccionarJuego(@RequestParam("juegoId") Long juegoId, @RequestParam("tipo") String tipo,
            HttpSession session,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {
        Optional<Juego> juego = juegoService.obtenerJuegoPorId(juegoId);

        if (juego.isPresent()) {
            session.setAttribute("juegoId", juegoId);
            return "redirect:/" + tipo + "/ver";
        } else {
            redirectAttributes.addFlashAttribute("error", "Juego no encontrado o sin permiso.");
            return "/juegos";
        }
    }
    
}
