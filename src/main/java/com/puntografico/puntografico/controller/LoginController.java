package com.puntografico.puntografico.controller;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@SuppressWarnings("ALL")
@Controller
public class LoginController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String ingresar(@RequestParam String username, @RequestParam String password, Model model, HttpSession session) {
        boolean esEmpleadoValido = empleadoService.validarEmpleado(username, password);

        if (esEmpleadoValido) {
            Empleado empleado = empleadoService.traerEmpleadoPorUsername(username);
            session.setAttribute("empleadoLogueado", empleado);
            return "redirect:/home";
        } else {
            model.addAttribute("error", true);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
