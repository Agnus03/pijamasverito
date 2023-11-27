package com.pijamasverito.app.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pijamasverito.app.entity.*;

public interface PedidoRepository extends MongoRepository<Pedido, String> {
	List<Pedido> findByEmpleadoAndEstado(Empleado empleado, String estado);

	List<Pedido> findByEmpleado(Empleado empleado);

	Pedido Inventario(Inventario inventario);

	@Query("{ 'prestamo.bodeguero' : ?0 }")
	List<Pedido> findByBodeguero(Bodeguero bodeguero);
}
