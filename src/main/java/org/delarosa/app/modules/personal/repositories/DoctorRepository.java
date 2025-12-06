package org.delarosa.app.modules.personal.repositories;

import org.delarosa.app.modules.personal.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findAllByEspecialidadIdEspecialidad(Integer idEspecialidad);
}
