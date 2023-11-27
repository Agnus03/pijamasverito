package com.pijamasverito.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.pijamasverito.app.entity.Empleado;
public interface EmpleadoRepository extends MongoRepository<Empleado, String> {

	Empleado findByCorreoAndContrasena(String correo, String contrasena);

}
