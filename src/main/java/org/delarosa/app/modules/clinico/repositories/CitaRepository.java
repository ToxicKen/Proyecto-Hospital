package org.delarosa.app.modules.clinico.repositories;

import org.delarosa.app.modules.clinico.entities.Cita;
import org.delarosa.app.modules.personal.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    Optional<Cita> findByDoctorAndFechaSolicitud(Doctor doctor, LocalDateTime fecha);

    @Query("""
       SELECT c
       FROM Cita c
       WHERE c.paciente.idPaciente = :paciente
         AND c.doctor.idDoctor = :doctor
         AND (c.estatus = 'AGENDADA_PENDIENTE_DE_PAGO'
              OR c.estatus = 'PAGADA_PENDIENTE_POR_ATENDER')
       """)
    Optional<Cita> obtenerCitasPendientesDeDoctorPaciente(
            Integer paciente,
            Integer doctor
    );

    List<Cita> findAllByDoctorIdDoctorAndFechaCitaBetween(Integer idDoctor,
                                                        LocalDateTime inicio,
                                                        LocalDateTime fin);

    @Procedure(name = "SP_Filtrar_Citas")
    List<Object[]> filtrarCitas(
            @Param("FechaInicio") LocalDate fechaInicio,
            @Param("FechaFin") LocalDate fechaFin,
            @Param("Estatus") String estatus,
            @Param("NombrePaciente") String nombrePaciente,
            @Param("IdPaciente") Integer idPaciente,
            @Param("IdDoctor") Integer idDoctor
    );

}
