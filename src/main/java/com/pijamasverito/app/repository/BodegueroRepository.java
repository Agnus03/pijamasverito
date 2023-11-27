package com.pijamasverito.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.pijamasverito.app.entity.Bodeguero;

public interface BodegueroRepository extends MongoRepository<Bodeguero, String>{

	Bodeguero findByCorreoAndContrasena(String correo, String contrasena);
	
}
