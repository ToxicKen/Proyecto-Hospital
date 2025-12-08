package org.delarosa.app.modules.clinico.repositories;

import org.delarosa.app.modules.clinico.dtos.BitacoraResumenDTO;
import org.delarosa.app.modules.clinico.entities.BitacoraHistorialCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BitacoraRepository extends JpaRepository<BitacoraHistorialCita,Integer> {

    @Query("""
    SELECT NEW org.delarosa.app.modules.clinico.dtos.BitacoraResumenDTO(
        b.idHistorial,
        b.fechaCita,
        b.horaCita,
        CONCAT(perDoc.nombre, ' ', perDoc.apellidoP, ' ', perDoc.apellidoM),
        
        /* AHORA: Nombre desde la tabla Especialidad */
        b.especialidad,
        
        CONCAT(perPac.nombre, ' ', perPac.apellidoP, ' ', perPac.apellidoM),
        
        /* AHORA: Número desde la tabla Consultorio */
        CAST(b.consultorio AS string)
    )
    FROM BitacoraHistorialCita b
    
    /* Joins Doctor (ID Doctor = ID Empleado confirmado) */
    JOIN Doctor d ON b.idDoctor = d.idDoctor
    JOIN Empleado e ON d.idDoctor = e.idEmpleado 
    JOIN Persona perDoc ON e.idEmpleado = perDoc.idPersona
    
    /* Joins Paciente (ID Paciente = ID Persona confirmado) */
    JOIN Paciente p ON b.idPaciente = p.idPaciente
    JOIN Persona perPac ON p.idPaciente = perPac.idPersona 
    
    WHERE b.idPaciente = :idPaciente and b.estatusConsulta = 'ATENDIDA'
""")
    List<BitacoraResumenDTO> findResumenByPaciente(@Param("idPaciente") Integer idPaciente);
}
