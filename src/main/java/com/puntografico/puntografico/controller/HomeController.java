package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.Empleado;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        model.addAttribute("empleado", session.getAttribute("empleadoLogueado"));
        return "home";
    }
}
