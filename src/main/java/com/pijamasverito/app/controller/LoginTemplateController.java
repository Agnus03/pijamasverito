package com.pijamasverito.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.pijamasverito.app.entity.*;
import com.pijamasverito.app.repository.AdministradorRepository;
import com.pijamasverito.app.repository.EmpleadoRepository; 
import com.pijamasverito.app.repository.BodegueroRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginTemplateController {

    @GetMapping("/")
    public String HomeTemplate(Model model) {
        return "login";

    }

    @GetMapping("/registro")
    public String RegistroTemplate(Model model) {
        model.addAttribute("empleado", new Empleado()); 
        return "registro";

    }

    @Autowired
    private EmpleadoRepository empleadoRepository; 

    @Autowired
    private BodegueroRepository bodegueroRepository; 

    @Autowired
    private AdministradorRepository administradorRepository;

    @PostMapping("/ingresar")
    public String login(@RequestParam("correo") String correo, @RequestParam("contrasena") String contrasena,
            Model model, HttpSession session) {
    	
    	List<Administrador> administradorList = administradorRepository.findAll();
        System.out.println(administradorList.get(0).getCorreo());
        Administrador administrador = administradorRepository.findByCorreoAndContrasena(correo, contrasena);
        if (administrador != null) {
            // Inicio de sesión exitoso, redirigir a la página de inicio
            System.out.println("Correo: " + administrador.getCorreo() + " Password:" + administrador.getContrasena());
            Administrador admin = (Administrador) session.getAttribute("administrador");
            session.setAttribute("administrador", admin);
            model.addAttribute("administrador", administrador);
            return "homes/home-administrador"; // Nombre de la página de inicio (por ejemplo, "inicio.html")

        }

        List<Empleado> empleadoList = empleadoRepository.findAll(); 
        System.out.println(empleadoList.get(0).getCorreo());
        Empleado empleado = empleadoRepository.findByCorreoAndContrasena(correo, contrasena); 
        if (empleado != null) {
            // Inicio de sesión exitoso, redirigir a la página de inicio
            System.out.println("Correo: " + empleado.getCorreo() + " Password:" + empleado.getContrasena());
            Empleado emp = (Empleado) session.getAttribute("empleado"); 
            session.setAttribute("empleado", emp); 
            model.addAttribute("empleado", empleado); 
            if (empleado.getEstado().equals("bloqueado")) {
                model.addAttribute("authenticationFailed", true);
                model.addAttribute("errorMessage", "Su cuenta está bloqueada");
                return "/login";
            } else {
                return "homes/home-empleado"; 

            }

        }
        List<Bodeguero> bodegueroList = bodegueroRepository.findAll(); 
        System.out.println(bodegueroList.get(0).getCorreo());
        Bodeguero bodeguero = bodegueroRepository.findByCorreoAndContrasena(correo, contrasena); 
        if (bodeguero != null) {
            // Inicio de sesión exitoso, redirigir a la página de inicio
            System.out.println("Correo: " + bodeguero.getCorreo() + " Password:" + bodeguero.getContrasena());
            Bodeguero bod = (Bodeguero) session.getAttribute("bodeguero"); 
            session.setAttribute("bodeguero", bod); 
            model.addAttribute("bodeguero", bodeguero); 

            if (bodeguero.getEstado().equals("bloqueado")) {
                model.addAttribute("authenticationFailed", true);
                model.addAttribute("errorMessage", "Su cuenta está bloqueada");
                return "/login";
            } else {
                return "homes/home-bodeguero"; // Cambio de home-trabajador a home-bodeguero

            }

        }

        

        else if (empleado == null || bodeguero == null || administrador == null) {
            // Inicio de sesión fallido, mostrar mensaje de error en la página de inicio
            model.addAttribute("authenticationFailed", true);
            model.addAttribute("errorMessage", "Usuario o contraseña incorrectos");
            return "/login";
        }
        return "/login";
    }

}
