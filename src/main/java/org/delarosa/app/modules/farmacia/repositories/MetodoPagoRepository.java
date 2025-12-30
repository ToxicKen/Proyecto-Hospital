package org.delarosa.app.modules.farmacia.repositories;

import org.delarosa.app.modules.general.entities.MetodoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer> {
}
