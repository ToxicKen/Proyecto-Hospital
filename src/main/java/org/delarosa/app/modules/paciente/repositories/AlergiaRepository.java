package org.delarosa.app.modules.paciente.repositories;

import org.delarosa.app.modules.paciente.entities.Alergia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlergiaRepository extends JpaRepository<Alergia, Integer> {
    Optional<Alergia> findByNombre(String nombre);
}
