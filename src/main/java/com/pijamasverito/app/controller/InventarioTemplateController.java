package com.pijamasverito.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.pijamasverito.app.entity.Administrador;
import com.pijamasverito.app.entity.Empleado;
import com.pijamasverito.app.entity.Inventario;
import com.pijamasverito.app.exception.NotFoundException;
import com.pijamasverito.app.repository.AdministradorRepository;
import com.pijamasverito.app.repository.InventarioRepository;
import com.pijamasverito.app.repository.EmpleadoRepository;

import jakarta.servlet.http.HttpSession;

@Controller // Asegúrate de agregar la anotación @Controller
@RequestMapping("/inventarios")
public class InventarioTemplateController {

	@Autowired
	private InventarioRepository inventarioRepository;
	
	@Autowired
	private EmpleadoRepository empleadoRepository;

	@Autowired
	private AdministradorRepository administradorRepository;

	@GetMapping("/new/{id}")
	public String inventarioNew(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {
		model.addAttribute("inventario", new Inventario());
		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		return "inventarios/inventarios-form";
	}

	@GetMapping("/list/{id}")
	public String InventarioListByBodeguero(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);

		model.addAttribute("inventarios", inventarioRepository.findAll());
		return "inventarios/inventarios-list";
	}
	
	@GetMapping("/listInventarioEmpleado/{id}")
	public String InventarioBodeguero(@ModelAttribute("empleado") Empleado empleado,
			@PathVariable("id") String id, Model model, HttpSession session) {

		model.addAttribute("empleado", empleadoRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("empleado no encontrado")));

		empleado = (Empleado) session.getAttribute("empleado");
		session.setAttribute("empleado", empleado);

		model.addAttribute("inventarios", inventarioRepository.findAll());
		return "inventarios/inventarios-list-empleado";
	}

	@PostMapping("/salvar/{id}")
	public String InventariosSave(@ModelAttribute("administrador") Administrador administrador,
			@ModelAttribute("vehiculo") Inventario inventario, HttpSession session, @PathVariable("id") String id,
			Model model) {
		if (inventario.getId().isEmpty()) {
			inventario.setId(null);
		}

		if (inventario.getEstado().isEmpty()) {
			inventario.setEstado("Disponible");
		}

		inventarioRepository.save(inventario);

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("inventarios", inventarioRepository.findAll());
		return "inventarios/inventarios-list";
	}

	@PostMapping("/save")
	public String InventarioSaveByTrabajador(@ModelAttribute("inventario") Inventario inventario,
			@RequestParam("file") MultipartFile imagen) throws IOException {
		if (inventario.getId().isEmpty()) {
			inventario.setId(null);
		}

		if (inventario.getEstado().isEmpty()) {
			inventario.setEstado("Disponible");
		}

		inventarioRepository.save(inventario);
		return "redirect:/inventarios/";
	}

	@GetMapping("/editar/{id}/{id2}")
	public String InventarioEdit(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
		model.addAttribute("inventario",
				inventarioRepository.findById(id).orElseThrow(() -> new NotFoundException("inventario no encontrado")));

		model.addAttribute("administrador", administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		return "inventarios/inventarios-form";
	}

	@GetMapping("/delete/{id}/{id2}")
	public String EmpleadoDeleteByBodeguero(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
		inventarioRepository.deleteById(id);
		model.addAttribute("administrador", administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("inventarios", inventarioRepository.findAll());
		return "inventarios/inventarios-list";
	}

}
