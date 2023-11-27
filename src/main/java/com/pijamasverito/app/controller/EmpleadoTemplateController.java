package com.pijamasverito.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pijamasverito.app.entity.Administrador;
import com.pijamasverito.app.entity.Empleado; // Cambio de Cliente a Empleado
import com.pijamasverito.app.exception.NotFoundException;
import com.pijamasverito.app.repository.AdministradorRepository;
import com.pijamasverito.app.repository.EmpleadoRepository; // Cambio de ClienteRepository a EmpleadoRepository

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/empleados") // Cambio de /clientes a /empleados
public class EmpleadoTemplateController { // Cambio de ClienteTemplateController a EmpleadoTemplateController

    @Autowired
    private EmpleadoRepository empleadoRepository; // Cambio de ClienteRepository a EmpleadoRepository

    @Autowired
    private AdministradorRepository administradorRepository;

    @GetMapping("/new/{id}")
    public String EmpleadoNewByTrabajador(@ModelAttribute("administrador") Administrador administrador,
            @PathVariable("id") String id, Model model, HttpSession session) {
        model.addAttribute("empleado", new Empleado()); // Cambio de Cliente a Empleado
        model.addAttribute("administrador", administradorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

        administrador = (Administrador) session.getAttribute("administrador");
        session.setAttribute("administrador", administrador);
        return "empleado/empleados-form"; // Cambio de cliente/clientes-form a empleado/empleados-form
    }

    @GetMapping("/list/{id}")
    public String EmpleadosListByTrabajador(@ModelAttribute("administrador") Administrador administrador,
            @PathVariable("id") String id, Model model, HttpSession session) {

        model.addAttribute("administrador", administradorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

        administrador = (Administrador) session.getAttribute("administrador");
        session.setAttribute("administrador", administrador);

        model.addAttribute("empleados", empleadoRepository.findAll()); // Cambio de clientes a empleados
        return "empleado/empleados-list"; // Cambio de cliente/clientes-list a empleado/empleados-list
    }

    @PostMapping("/salvar/{id}")
    public String EmpleadoSaveByAdministrador(@ModelAttribute("administrador") Administrador administrador,
            @ModelAttribute("empleado") Empleado empleado, HttpSession session, @PathVariable("id") String id,
            Model model) {
        if (empleado.getId().isEmpty()) {
            empleado.setId(null);
        }

        if (empleado.getEstado().isEmpty()) {
            empleado.setEstado("activo");
        }

        empleadoRepository.save(empleado);

        model.addAttribute("administrador", administradorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

        administrador = (Administrador) session.getAttribute("administrador");
        session.setAttribute("administrador", administrador);
        model.addAttribute("empleados", empleadoRepository.findAll()); // Cambio de clientes a empleados
        return "empleado/empleados-list"; // Cambio de cliente/clientes-list a empleado/empleados-list
    }

    @GetMapping("/editar/{id}/{id2}")
    public String EmpleadoEditByAdministrador(@ModelAttribute("administrador") Administrador administrador,
            @PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
        model.addAttribute("empleado",
                empleadoRepository.findById(id).orElseThrow(() -> new NotFoundException("Empleado no encontrado"))); // Cambio de Cliente a Empleado

        model.addAttribute("administrador", administradorRepository.findById(id2)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

        administrador = (Administrador) session.getAttribute("administrador");
        session.setAttribute("administrador", administrador);
        return "empleado/empleados-form"; // Cambio de cliente/clientes-form a empleado/empleados-form
    }

    @GetMapping("/delete/{id}/{id2}")
    public String EmpleadoDeleteByTrabajador(@ModelAttribute("administrador") Administrador administrador,
            @PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
        empleadoRepository.deleteById(id);
        model.addAttribute("administrador", administradorRepository.findById(id2)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

        administrador = (Administrador) session.getAttribute("administrador");
        session.setAttribute("administrador", administrador);
        model.addAttribute("empleados", empleadoRepository.findAll()); // Cambio de clientes a empleados
        return "empleado/empleados-list"; // Cambio de cliente/clientes-list a empleado/empleados-list
    }

    // métodos empleado perfil ------------------------------------------------

    @PostMapping("/modificarPerfil")
    public String EmpleadoSavePerfil(@ModelAttribute("empleado") Empleado empleado, HttpSession session, Model model,
            RedirectAttributes redirectAttributes) {
        if (empleado.getId().isEmpty()) {
            empleado.setId(null);
        }

        if (empleado.getEstado().isEmpty()) {
            empleado.setEstado("activo");
        }

        empleadoRepository.save(empleado);

        redirectAttributes.addAttribute("successMessage", "Por su seguridad se ha finalizado la sesión.");
        return "redirect:/login/";

    }

    @GetMapping("/perfil/{id}")
    public String PerfilEmpleado(@ModelAttribute("empleado") Empleado empleado, @PathVariable("id") String id,
            HttpSession session, Model model) {

        model.addAttribute("empleado",
                empleadoRepository.findById(id).orElseThrow(() -> new NotFoundException("Empleado no encontrado"))); // Cambio de Cliente a Empleado

        empleado = (Empleado) session.getAttribute("empleado");

        session.setAttribute("empleado", empleado);

        return "perfiles/perfil-empleado";
    }

    @GetMapping("/perfilHome/{id}")
    public String PerfilEmpleadoHome(@ModelAttribute("empleado") Empleado empleado, @PathVariable("id") String id,
            HttpSession session, Model model) {

        model.addAttribute("empleado",
                empleadoRepository.findById(id).orElseThrow(() -> new NotFoundException("Empleado no encontrado"))); // Cambio de Cliente a Empleado

        empleado = (Empleado) session.getAttribute("empleado");

        session.setAttribute("empleado", empleado);

        return "homes/home-empleado";
    }

    @GetMapping("/modificar/{id}")
    public String EmpleadoEditablePerfilByAdministrador(@ModelAttribute("empleado") Empleado empleado,
            @PathVariable("id") String id, Model model, HttpSession session) {
        model.addAttribute("empleado",
                empleadoRepository.findById(id).orElseThrow(() -> new NotFoundException("Empleado no encontrado"))); // Cambio de Cliente a Empleado

        empleado = (Empleado) session.getAttribute("empleado");

        session.setAttribute("empleado", empleado);

        return "perfiles/perfil-empleado-editable";
    }

    @PostMapping("/guardar")
    public String EmpleadoSave(@ModelAttribute("empleado") Empleado empleado, Model model) {
        if (empleado.getId().isEmpty()) {
            empleado.setId(null);
        }

        if (empleado.getEstado().isEmpty()) {
            empleado.setEstado("activo");
        }

        empleadoRepository.save(empleado);

        return "login";
    }
}
