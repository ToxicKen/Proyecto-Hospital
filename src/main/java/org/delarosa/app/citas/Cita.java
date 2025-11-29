package org.delarosa.app.citas;

import jakarta.persistence.*;
import lombok.*;
import org.delarosa.app.empleado.doctor.Doctor;
import org.delarosa.app.paciente.Paciente;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer folioCita;

    @OneToOne
    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente")
    private Paciente paciente;

    @OneToOne
    @JoinColumn(name = "idDoctor", referencedColumnName = "idDoctor")
    private Doctor doctor;

    private LocalDateTime fechaCita;

    private LocalDateTime fechaSolicitud;

    @Enumerated(EnumType.STRING)
    private EstatusCita estatus;


}
