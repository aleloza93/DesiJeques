package com.desi.jeques.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "home";
    }

    @GetMapping("/publicaciones")
    public String publicaciones() {
        return "modulos/publicaciones";
    }

    @GetMapping("/contratos")
    public String contratos() {
        return "modulos/contratos";
    }

    @GetMapping("/facturas")
    public String facturas() {
        return "modulos/facturas";
    }
}