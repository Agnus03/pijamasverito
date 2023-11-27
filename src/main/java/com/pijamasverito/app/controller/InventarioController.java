package com.pijamasverito.app.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pijamasverito.app.entity.Inventario; 
import com.pijamasverito.app.exception.NotFoundException;
import com.pijamasverito.app.repository.InventarioRepository; 

@RestController
@RequestMapping(value = "/api/inventarios")
public class InventarioController { 

    @Autowired
    private InventarioRepository inventarioRepository; 

    @GetMapping("/")
    public List<Inventario> getAllInventarios() { 
        return inventarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Inventario getInventarioById(@PathVariable String id) { 
        return inventarioRepository.findById(id).orElseThrow(() -> new NotFoundException("Inventario no encontrado"));
    }

    @PostMapping("/")
    public Inventario saveInventario(@RequestBody Map<String, Object> body) { 
        ObjectMapper mapper = new ObjectMapper();
        Inventario inventario = mapper.convertValue(body, Inventario.class); 
        return inventarioRepository.save(inventario);
    }

    @PutMapping("/{id}")
    public Inventario updateInventario(@PathVariable String id, @RequestBody Map<String, Object> body) { 
        ObjectMapper mapper = new ObjectMapper();
        Inventario inventario = mapper.convertValue(body, Inventario.class); 
        inventario.setId(id);
        return inventarioRepository.save(inventario);
    }

    @DeleteMapping("/{id}")
    public Inventario deleteInventario(@PathVariable String id) { 
        Inventario inventario = inventarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventario no encontrado"));
        inventarioRepository.deleteById(id);
        return inventario;
    }
}
