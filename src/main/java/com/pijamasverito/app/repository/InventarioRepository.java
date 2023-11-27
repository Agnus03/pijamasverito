package com.pijamasverito.app.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.pijamasverito.app.entity.Inventario;


public interface InventarioRepository extends MongoRepository<Inventario, String> {
	List<Inventario> findByEstado(String estado);
}
