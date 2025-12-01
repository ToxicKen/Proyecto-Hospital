package org.delarosa.app.citas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenPagoRepository extends JpaRepository<OrdenPago,Integer> {
}
