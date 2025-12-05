package org.delarosa.app.modules.personal.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delarosa.app.modules.general.enums.Dia;

import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HorarioEmpleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHorarioEmpleado;

    @Column(nullable = false)
    private LocalTime hrsInicio;

    @Column(nullable = false)
    private LocalTime hrsFin;

    @Enumerated(EnumType.STRING)
    private Dia dia;

    @ManyToOne
    @JoinColumn(name = "IdEmpleado",nullable = false)
    private Empleado empleado;

}

