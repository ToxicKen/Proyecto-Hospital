package org.delarosa.app.modules.paciente.repositories;

import org.delarosa.app.modules.paciente.entities.Alergia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlergiaRepository extends JpaRepository<Alergia, Integer> {

}
