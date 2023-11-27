package com.pijamasverito.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pijamasverito.app.entity.Bodeguero; // Cambio de Trabajador a Bodeguero
import com.pijamasverito.app.exception.NotFoundException;
import com.pijamasverito.app.repository.BodegueroRepository; // Cambio de TrabajadorRepository a BodegueroRepository

@RestController
@RequestMapping(value = "/api/bodegueros") // Cambio de /trabajadores a /bodegueros
public class BodegueroController { // Cambio de TrabajadorController a BodegueroController

    @Autowired
    private BodegueroRepository bodegueroRepository; // Cambio de TrabajadorRepository a BodegueroRepository

    @GetMapping("/")
    public List<Bodeguero> getAllBodegueros() { // Cambio de getAllEmpleados a getAllBodegueros
        return bodegueroRepository.findAll(); // Cambio de trabajadorRepository a bodegueroRepository
    }

    @GetMapping("/{id}")
    public Bodeguero getBodegueroById(@PathVariable String id) { // Cambio de getTrabajadorById a getBodegueroById
        return bodegueroRepository.findById(id).orElseThrow(() -> new NotFoundException("Bodeguero no encontrado")); // Cambio de trabajadorRepository a bodegueroRepository
    }

    @PostMapping("/")
    public Bodeguero saveBodeguero(@RequestBody Map<String, Object> body) { // Cambio de saveTrabajador a saveBodeguero
        ObjectMapper mapper = new ObjectMapper();
        Bodeguero bodeguero = mapper.convertValue(body, Bodeguero.class); // Cambio de Trabajador a Bodeguero
        return bodegueroRepository.save(bodeguero); // Cambio de trabajadorRepository a bodegueroRepository
    }

    @PutMapping("/{id}")
    public Bodeguero updateBodeguero(@PathVariable String id, @RequestBody Map<String, Object> body) { // Cambio de updateTrabajador a updateBodeguero
        ObjectMapper mapper = new ObjectMapper();
        Bodeguero bodeguero = mapper.convertValue(body, Bodeguero.class); // Cambio de Trabajador a Bodeguero
        bodeguero.setId(id);
        return bodegueroRepository.save(bodeguero); // Cambio de trabajadorRepository a bodegueroRepository
    }

    @DeleteMapping("/{id}")
    public Bodeguero deleteBodeguero(@PathVariable String id) { // Cambio de deleteTrabajador a deleteBodeguero
        Bodeguero bodeguero = bodegueroRepository.findById(id).orElseThrow(() -> new NotFoundException("Bodeguero no encontrado")); // Cambio de trabajadorRepository a bodegueroRepository
        bodegueroRepository.deleteById(id); // Cambio de trabajadorRepository a bodegueroRepository
        return bodeguero;
    }
}
