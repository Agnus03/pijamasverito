package com.pijamasverito.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pijamasverito.app.entity.Administrador;

public interface AdministradorRepository extends MongoRepository<Administrador, String>{
	
	Administrador findByCorreoAndContrasena(String correo, String contrasena);

}
