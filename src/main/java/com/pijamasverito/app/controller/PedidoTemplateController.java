package com.pijamasverito.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.pijamasverito.app.entity.*;
import com.pijamasverito.app.exception.NotFoundException;
import com.pijamasverito.app.repository.AdministradorRepository;
import com.pijamasverito.app.repository.EmpleadoRepository;
import com.pijamasverito.app.repository.PedidoRepository;
import com.pijamasverito.app.repository.BodegueroRepository;
import com.pijamasverito.app.repository.InventarioRepository;
import com.pijamasverito.app.utilities.*;

import jakarta.servlet.http.HttpSession;

@Controller 
@RequestMapping("/pedidos")
public class PedidoTemplateController {

	@Autowired
	private PedidoRepository pedidoRepository;

	@Autowired
	private EmpleadoRepository empleadoRepository;

	@Autowired
	private BodegueroRepository bodegueroRepository;

	@Autowired
	private AdministradorRepository administradorRepository;

	@Autowired
	private InventarioRepository inventarioRepository;
	
	@Autowired
	private ListarPedidoPdf listarPrestamoPdf;

	// metodos para administrador

	@GetMapping("/list/{id}")
	public String PedidoListByAdministrador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);

		model.addAttribute("pedidos", pedidoRepository.findAll());
		return "prestamos/prestamos-list-administrador";
	}

	@GetMapping("/new/{id}")
	public String PedidoNewByAdministrador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session, Model modelCliente, Model modelVehiculo,
			Model modelTrabajador) {
		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);

		List<Empleado> empleados = empleadoRepository.findAll();
		List<Bodeguero> bodegueros = bodegueroRepository.findAll();
		List<Inventario> inventarios = inventarioRepository.findByEstado("Disponible");

		modelCliente.addAttribute("empleados", empleados);
		modelTrabajador.addAttribute("bodegueros", bodegueros);
		modelVehiculo.addAttribute("inventarios", inventarios);

		model.addAttribute("pedido", new Pedido());
		return "prestamos/prestamos-form-administrador";
	}

	@PostMapping("/salvar/{id}")
	public String PedidorSaveByAdministrador(@ModelAttribute("administrador") Administrador administrador,
			@ModelAttribute("prestamo") Pedido pedido, HttpSession session, @PathVariable("id") String id,
			Model model, Model alerta, RedirectAttributes redirectAttributes) {

		Inventario inventario = inventarioRepository.findById(pedido.getInventario().getId())
				.orElseThrow(() -> new NotFoundException("Inventario no encontrado"));
		Empleado empleado = empleadoRepository.findById(pedido.getEmpleado().getId())
				.orElseThrow(() -> new NotFoundException("Empleado no encontrado"));
		List<Pedido> pedidos = pedidoRepository.findByEmpleadoAndEstado(empleado, "Activo");

		if (pedido.getId().isEmpty()) {
			pedido.setId(null);
		}

		if (pedido.getEstado().isEmpty()) {
			pedido.setEstado("Activo");
		}

		// Verificar la variable de sesión para determinar si se está editando
		Boolean edicionEnCurso = (Boolean) session.getAttribute("edicionEnCurso");
		if (edicionEnCurso != null && edicionEnCurso) {
			inventarioRepository.save(inventario);
			pedidoRepository.save(pedido);
			// Eliminar la variable de sesión después de procesar
			session.removeAttribute("edicionEnCurso");
			model.addAttribute("administrador", administradorRepository.findById(id)
					.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
			administrador = (Administrador) session.getAttribute("administrador");
			session.setAttribute("administrador", administrador);
			model.addAttribute("pedidos", pedidoRepository.findAll());
			return "prestamos/prestamos-list-administrador";
		}

		if (!pedidos.isEmpty()) {
			alerta.addAttribute("alert", "El empleado tiene pedidos activos");
			model.addAttribute("administrador", administradorRepository.findById(id)
					.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
			administrador = (Administrador) session.getAttribute("administrador");
			session.setAttribute("administrador", administrador);
			model.addAttribute("pedidos", pedidoRepository.findAll());
			return "prestamos/prestamos-list-administrador";
		}

		inventarioRepository.save(inventario);
		pedidoRepository.save(pedido);
		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("pedidos", pedidoRepository.findAll());
		return "prestamos/prestamos-list-administrador";
	}

	@GetMapping("/editar/{id}/{id2}")
	public String PedidoEditByAdministrador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session,
			Model modelEmpleado, Model modelInventario, Model modelBodeguero) {
		model.addAttribute("administrador", administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		List<Empleado> empleados = empleadoRepository.findAll();
		List<Inventario> inventarios = inventarioRepository.findAll();
		List<Bodeguero> bodegueros = bodegueroRepository.findAll();

		modelBodeguero.addAttribute("bodegueros", bodegueros);
		modelEmpleado.addAttribute("empleados", empleados);
		modelEmpleado.addAttribute("inventarios", inventarios);

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		session.setAttribute("edicionEnCurso", true);
		model.addAttribute("pedido",
				pedidoRepository.findById(id).orElseThrow(() -> new NotFoundException("Pedido no encontrado")));
		return "prestamos/prestamos-form-administrador";
	}

	@GetMapping("/eliminar/{id}/{id2}")
	public String BodegueroDeleteByBodeguero(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
		bodegueroRepository.deleteById(id);
		model.addAttribute("administrador", administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("bodegueros", bodegueroRepository.findAll());

		Pedido pedido = pedidoRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Pedido no encontrado"));
		Inventario inventario = inventarioRepository.findById(pedido.getInventario().getId())
				.orElseThrow(() -> new NotFoundException("Inventario no encontrado"));

		if (inventario != null) {
			inventario.setEstado("Disponible");
			inventarioRepository.save(inventario);
		}

		inventarioRepository.deleteById(id);

		return "prestamos/prestamos-list-administrador";
	}

	// metodos para administrador

	@GetMapping("/listBodeguero/{id}")
	public String PedidoListByBodeguero(@ModelAttribute("bodeguero") Bodeguero bodeguero,
			@PathVariable("id") String id, Model model, HttpSession session) {

		model.addAttribute("bodeguero",
				bodegueroRepository.findById(id).orElseThrow(() -> new NotFoundException("Bodeguero no encontrado")));

		bodeguero = (Bodeguero) session.getAttribute("bodeguero");
		session.setAttribute("bodeguero", bodeguero);

		model.addAttribute("pedido", pedidoRepository.findAll());
		return "prestamos/prestamos-list-trabajador";
	}

	@GetMapping("/newBodeguero/{id}")
	public String PedidoNewByBodeguero(@ModelAttribute("bodeguero") Bodeguero bodeguero,
			@PathVariable("id") String id, Model model, HttpSession session, Model modelEmpleado, Model modelInventario,
			Model modelBodeguero) {
		model.addAttribute("trabajador",
				bodegueroRepository.findById(id).orElseThrow(() -> new NotFoundException("Bodeguero no encontrado")));
		bodeguero = (Bodeguero) session.getAttribute("bodeguero");
		session.setAttribute("bodeguero", bodeguero);

		List<Empleado> empleados = empleadoRepository.findAll();
		List<Inventario> inventarios = inventarioRepository.findByEstado("Disponible");

		modelEmpleado.addAttribute("empleados", empleados);
		modelInventario.addAttribute("inventarios", inventarios);

		model.addAttribute("pedido", new Pedido());
		return "prestamos/prestamos-form-trabajador";
	}

	@PostMapping("/salvarBodeguero/{id}")
	public String PedidorSaveByBodeguero(@ModelAttribute("bodeguero") Bodeguero bodeguero,
			@ModelAttribute("pedido") Pedido pedido, HttpSession session, @PathVariable("id") String id,
			Model model, Model alerta, RedirectAttributes redirectAttributes) {

		Inventario inventario = inventarioRepository.findById(pedido.getInventario().getId())
				.orElseThrow(() -> new NotFoundException("Inventario no encontrado"));
		Empleado empleado = empleadoRepository.findById(pedido.getEmpleado().getId())
				.orElseThrow(() -> new NotFoundException("Empleado no encontrado"));
		List<Pedido> pedidos = pedidoRepository.findByEmpleadoAndEstado(empleado, "Activo");

		if (pedido.getEstado().isEmpty()) {
			pedido.setEstado("Activo");
		}

		if (inventario.getEstado().equalsIgnoreCase("Disponible")) {
			inventario.setEstado("Oculto");
		}

		if (pedido.getEstado().equalsIgnoreCase("Finalizado")) {
			inventario.setEstado("Disponible");
		} else {
			inventario.setEstado("Oculto");
		}


		if (!pedidos.isEmpty()) {
			alerta.addAttribute("alert", "El Empleado tiene alquileres activos");
			model.addAttribute("bodeguero", bodegueroRepository.findById(id)
					.orElseThrow(() -> new NotFoundException("bodeguero no encontrado")));
			bodeguero = (Bodeguero) session.getAttribute("bodeguero");
			session.setAttribute("bodeguero", bodeguero);
			model.addAttribute("pedido", pedidoRepository.findAll());
			return "prestamos/prestamos-list-trabajador";
		}

		inventarioRepository.save(inventario);
		pedidoRepository.save(pedido);
		model.addAttribute("bodeguero",
				bodegueroRepository.findById(id).orElseThrow(() -> new NotFoundException("Bodeguero no encontrado")));
		bodeguero = (Bodeguero) session.getAttribute("bodeguero");
		session.setAttribute("bodeguero", bodeguero);

		model.addAttribute("pedido", pedidoRepository.findAll());
		return "prestamos/prestamos-list-trabajador";
	}

	@GetMapping("/editarBodeguero/{id}/{id2}")
	public String PedidoEditByBodeguero(@ModelAttribute("bodeguero") Bodeguero bodeguero,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session,
			Model modelEmpleado, Model modelInventario, Model modelBodeguero) {
		model.addAttribute("bodeguero", bodegueroRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Bodeguero no encontrado")));

		List<Empleado> empleados = empleadoRepository.findAll();
		List<Inventario> inventarios = inventarioRepository.findAll();

		modelEmpleado.addAttribute("empleados", empleados);
		modelInventario.addAttribute("inventarios", inventarios);

		bodeguero = (Bodeguero) session.getAttribute("bodeguero");
		session.setAttribute("bodeguero", bodeguero);
		session.setAttribute("edicionEnCurso", true);

		model.addAttribute("pedido",
				pedidoRepository.findById(id).orElseThrow(() -> new NotFoundException("Pedido no encontrado")));
		return "prestamos/prestamos-form-trabajador";
	}

	// metodos para cliente

	@GetMapping("/listEmpleado/{id}")
	public String PedidoListByEmpleado(@ModelAttribute("empleado") Empleado empleado, @PathVariable("id") String id,
			Model model, HttpSession session) {

		model.addAttribute("empleado",
				empleadoRepository.findById(id).orElseThrow(() -> new NotFoundException("Empleado no encontrado")));

		empleado = (Empleado) session.getAttribute("empleado");
		session.setAttribute("empleado", empleado);

		model.addAttribute("pedido", pedidoRepository.findAll());
		return "prestamos/prestamos-list-cliente";
	}
	
	@GetMapping("/pdf/{id}")
	public ModelAndView generarPdf(@ModelAttribute("empleado") Empleado empleado, @PathVariable("id") String id,
			Model model, HttpSession session) {

		model.addAttribute("empleado",
				empleadoRepository.findById(id).orElseThrow(() -> new NotFoundException("empleado no encontrado")));
		ArrayList<Pedido> listadoPrestamos = (ArrayList<Pedido>) pedidoRepository.findAll();
		empleado = (Empleado) session.getAttribute("empleado");
		session.setAttribute("empleado", empleado);

		Map<String, Object> model1 = new HashMap<>();
		model1.put("pedidos", listadoPrestamos);

		return new ModelAndView(listarPrestamoPdf, model1);
	}


}
