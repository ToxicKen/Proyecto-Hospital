package org.delarosa.app.modules.personal.repositories;

import org.delarosa.app.modules.personal.entities.Recepcionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecepcionistaRepository extends JpaRepository<Recepcionista, Integer> {
}
