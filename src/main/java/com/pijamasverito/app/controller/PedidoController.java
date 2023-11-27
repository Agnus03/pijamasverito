package com.pijamasverito.app.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pijamasverito.app.entity.Pedido; // Cambio en la importación
import com.pijamasverito.app.exception.NotFoundException;
import com.pijamasverito.app.repository.PedidoRepository; // Cambio en la importación

@RestController
@RequestMapping(value = "/api/pedidos")
public class PedidoController { 

    @Autowired
    private PedidoRepository pedidoRepository; 

    @GetMapping("/")
    public List<Pedido> getAllPedido() { 
        return pedidoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pedido getPedidoById(@PathVariable String id) { 
        return pedidoRepository.findById(id).orElseThrow(() -> new NotFoundException("Pedido no encontrado"));
    }

    @PostMapping("/")
    public Pedido savePedido(@RequestBody Map<String, Object> body) {
        ObjectMapper mapper = new ObjectMapper();
        Pedido pedido = mapper.convertValue(body, Pedido.class); 
        return pedidoRepository.save(pedido);
    }

    @PutMapping("/{id}")
    public Pedido updatePedido(@PathVariable String id, @RequestBody Map<String, Object> body) { 
        ObjectMapper mapper = new ObjectMapper();
        Pedido pedido = mapper.convertValue(body, Pedido.class); 
        pedido.setId(id);
        return pedidoRepository.save(pedido);
    }

    @DeleteMapping("/{id}")
    public Pedido deletePedido(@PathVariable String id) { 
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido no encontrado"));
        pedidoRepository.deleteById(id);
        return pedido;
    }
}
