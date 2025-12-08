package org.delarosa.app.modules.personal.repositories;

import org.delarosa.app.modules.general.enums.Dia;
import org.delarosa.app.modules.personal.entities.HorarioEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioEmpleadoRepository extends JpaRepository<HorarioEmpleado,Integer> {
    List<HorarioEmpleado> findByEmpleadoIdEmpleado(Integer idEmpleado);

    Optional<HorarioEmpleado> findByEmpleadoIdEmpleadoAndDiaSemana(Integer idEmpleado, Dia dia);
}
