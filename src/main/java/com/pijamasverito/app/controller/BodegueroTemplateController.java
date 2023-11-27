package com.pijamasverito.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pijamasverito.app.entity.Administrador;
import com.pijamasverito.app.entity.Bodeguero;
import com.pijamasverito.app.exception.NotFoundException;
import com.pijamasverito.app.repository.AdministradorRepository;
import com.pijamasverito.app.repository.BodegueroRepository; 

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/bodegueros") 
public class BodegueroTemplateController { 

    @Autowired
    private BodegueroRepository bodegueroRepository; 

    @Autowired
    private AdministradorRepository administradorRepository;

    @GetMapping("/list/{id}")
    public String BodeguerosListByAdministrador(@ModelAttribute("administrador") Administrador administrador,
            @PathVariable("id") String id, Model model, HttpSession session) {

        model.addAttribute("administrador", administradorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

        administrador = (Administrador) session.getAttribute("administrador");
        session.setAttribute("administrador", administrador);

        model.addAttribute("bodegueros", bodegueroRepository.findAll()); 
        return "bodeguero/bodegueros-list"; 
    }

    @GetMapping("/new/{id}")
    public String BodegueroNewByAdministrador(@ModelAttribute("administrador") Administrador administrador,
            @PathVariable("id") String id, Model model, HttpSession session) {
        model.addAttribute("administrador", administradorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

        administrador = (Administrador) session.getAttribute("administrador");
        session.setAttribute("administrador", administrador);

        model.addAttribute("bodegueros", bodegueroRepository.findAll());
        model.addAttribute("bodeguero", new Bodeguero()); 
        return "bodeguero/bodegueros-form"; 
    }

    @PostMapping("/salvar/{id}")
    public String BodegueroSaveByAdministrador(@ModelAttribute("administrador") Administrador administrador,
            @ModelAttribute("bodeguero") Bodeguero bodeguero, HttpSession session, @PathVariable("id") String id,
            Model model) {
        if (bodeguero.getId().isEmpty()) {
            bodeguero.setId(null);
        }

        if (bodeguero.getEstado().isEmpty()) {
            bodeguero.setEstado("activo");
        }

        bodegueroRepository.save(bodeguero);

        model.addAttribute("administrador", administradorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

        administrador = (Administrador) session.getAttribute("administrador");
        session.setAttribute("administrador", administrador);
        model.addAttribute("bodegueros", bodegueroRepository.findAll());
        return "bodeguero/bodegueros-list"; 
    }

    @GetMapping("/editar/{id}/{id2}")
    public String BodegueroEditByAdministrador(@ModelAttribute("administrador") Administrador administrador,
            @PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
        model.addAttribute("bodeguero",
                bodegueroRepository.findById(id).orElseThrow(() -> new NotFoundException("Bodeguero no encontrado"))); // Cambio de Trabajador a Bodeguero

        model.addAttribute("administrador", administradorRepository.findById(id2)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

        administrador = (Administrador) session.getAttribute("administrador");
        session.setAttribute("administrador", administrador);
        return "bodeguero/bodegueros-form"; 
    }

    @GetMapping("/delete/{id}/{id2}")
    public String BodegueroDeleteByAdministrador(@ModelAttribute("administrador") Administrador administrador,
            @PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
        bodegueroRepository.deleteById(id);

        model.addAttribute("administrador", administradorRepository.findById(id2)
                .orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

        administrador = (Administrador) session.getAttribute("administrador");
        session.setAttribute("administrador", administrador);
        model.addAttribute("bodegueros", bodegueroRepository.findAll()); 
        return "bodeguero/bodegueros-list"; 
    }

    // métodos bodeguero perfil ------------------------------------------------

    @PostMapping("/modificarPerfil")
    public String BodegueroSavePerfilByAdministrador(@ModelAttribute("bodeguero") Bodeguero bodeguero,
            RedirectAttributes redirectAttributes, HttpSession session) {
        if (bodeguero.getId().isEmpty()) {
            bodeguero.setId(null);
        }

        if (bodeguero.getEstado().isEmpty()) {
            bodeguero.setEstado("activo");
        }

        bodegueroRepository.save(bodeguero);

        redirectAttributes.addAttribute("successMessage", "Por su seguridad se ha finalizado la sesión.");
        return "redirect:/login/";
    }

    @GetMapping("/perfil/{id}")
    public String PerfilBodeguero(@ModelAttribute("bodeguero") Bodeguero bodeguero, @PathVariable("id") String id,
            HttpSession session, Model model) {

        model.addAttribute("bodeguero",
                bodegueroRepository.findById(id).orElseThrow(() -> new NotFoundException("Bodeguero no encontrado")));

        bodeguero = (Bodeguero) session.getAttribute("bodeguero");

        session.setAttribute("bodeguero", bodeguero);

        return "perfiles/perfil-bodeguero";
    }

    @GetMapping("/perfilHome/{id}")
    public String PerfilBodegueroHome(@ModelAttribute("bodeguero") Bodeguero bodeguero, @PathVariable("id") String id,
            HttpSession session, Model model) {

        model.addAttribute("bodeguero",
                bodegueroRepository.findById(id).orElseThrow(() -> new NotFoundException("Bodeguero no encontrado")));

        bodeguero = (Bodeguero) session.getAttribute("bodeguero");

        session.setAttribute("bodeguero", bodeguero);

        return "homes/home-bodeguero";
    }

    @GetMapping("/modificar/{id}")
    public String BodegueroEditPerfilByAdministrador(@ModelAttribute("bodeguero") Bodeguero bodeguero,
            @PathVariable("id") String id, HttpSession session, Model model) {
        model.addAttribute("bodeguero",
                bodegueroRepository.findById(id).orElseThrow(() -> new NotFoundException("Bodeguero no encontrado")));

        bodeguero = (Bodeguero) session.getAttribute("bodeguero");

        session.setAttribute("bodeguero", bodeguero);

        return "perfiles/perfil-bodeguero-editable";
    }

}
