package org.delarosa.app.modules.clinico.repositories;

import org.delarosa.app.modules.clinico.entities.OrdenPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenPagoRepository extends JpaRepository<OrdenPago,Integer> {
}
