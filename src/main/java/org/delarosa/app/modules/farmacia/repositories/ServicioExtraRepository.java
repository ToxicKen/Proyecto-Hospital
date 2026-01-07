package org.delarosa.app.modules.farmacia.repositories;

import org.delarosa.app.modules.farmacia.entities.ServicioExtra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServicioExtraRepository extends JpaRepository<ServicioExtra,Integer> {
    List<ServicioExtra> findByNombreContainingIgnoreCase(String nombre);
}
