package org.delarosa.app.modules.clinico.repositories;

import org.delarosa.app.modules.clinico.entities.LineaPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface LineaPagoRepository extends JpaRepository<LineaPago,Integer> {
    @Query("SELECT COALESCE(SUM(l.montoPago), 0) FROM LineaPago l WHERE l.ordenPago.idOrdenPago = :idOrden")
    BigDecimal sumarPagosPorOrden(@Param("idOrden") Integer idOrden);
}
