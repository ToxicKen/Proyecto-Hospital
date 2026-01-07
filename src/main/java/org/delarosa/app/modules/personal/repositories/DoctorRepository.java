package org.delarosa.app.modules.personal.repositories;

import org.delarosa.app.modules.paciente.entities.Paciente;
import org.delarosa.app.modules.personal.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    List<Doctor> findAllByEspecialidadIdEspecialidad(Integer idEspecialidad);

    @Query("SELECT d FROM Doctor d WHERE d.empleado.persona.usuario.correoElectronico = :email")
    Optional<Doctor> buscarPorEmailDeUsuario(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "{call SP_DarDeBajaDoctor(:idDoctor)}", nativeQuery = true)
    void darDeBajaDoctor(@Param("idDoctor") Integer idDoctor);

    Optional<Doctor> findByEmpleadoPersonaNombre(String nombre);

}
