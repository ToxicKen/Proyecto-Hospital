package org.delarosa.app.modules.clinico.repositories;

import org.delarosa.app.modules.clinico.entities.Receta;
import org.delarosa.app.modules.paciente.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta,Integer>
{
    List<Receta> findByCitaDoctorIdDoctor(Integer idDoctor);
}
