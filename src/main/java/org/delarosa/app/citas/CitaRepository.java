package org.delarosa.app.citas;

import org.delarosa.app.empleado.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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
}
