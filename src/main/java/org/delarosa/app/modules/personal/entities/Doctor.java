package org.delarosa.app.modules.personal.entities;

import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "idDoctor", referencedColumnName = "idEmpleado")
    private Empleado empleado;

    @Column(nullable = false, unique = true)
    private String cedulaProfesional;

    @ManyToOne
    @JoinColumn(name = "idEspecialidad", referencedColumnName = "idEspecialidad",nullable = false)
    private Especialidad especialidad;

    @ManyToOne
    @JoinColumn(name = "idConsultorio", referencedColumnName = "idConsultorio",nullable = false)
    private Consultorio consultorio;

}

