package org.delarosa.app.modules.personal.repositories;

import org.delarosa.app.modules.personal.entities.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Integer> {

}
