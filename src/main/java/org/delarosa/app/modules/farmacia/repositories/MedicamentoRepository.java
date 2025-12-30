package org.delarosa.app.modules.farmacia.repositories;

import org.delarosa.app.modules.farmacia.entities.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Integer> {

    Optional<Medicamento> findByNombre(String nombre);
}
