package org.delarosa.app.empleado.doctor;

import jakarta.persistence.*;
import lombok.*;
import org.delarosa.app.empleado.Empleado;
import org.delarosa.app.empleado.HorarioEmpleado;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor {
    @Id
    private Integer idDoctor;

    @OneToOne
    @MapsId
    @JoinColumn(name = "idDoctor",referencedColumnName = "idEmpleado")
    private Empleado empleado;

    @Column(nullable = false,unique = true)
    private String cedulaProfesional;

    @ManyToOne
    @JoinColumn(name = "idEspecialidad",referencedColumnName = "idEspecialidad")
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name = "idConsultorio",referencedColumnName = "idConsultorio")
    private Consultorio consultorio;

  

}

