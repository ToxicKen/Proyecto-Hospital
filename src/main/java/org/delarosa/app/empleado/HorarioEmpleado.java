package org.delarosa.app.empleado;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.delarosa.app.Dia;

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

    @ManyToOne
    @JoinColumn(name = "IdEmpleado",nullable = false)
    private Empleado empleado;

    @Column(nullable = false)
    private LocalTime hrsInicio;

    @Column(nullable = false)
    private LocalTime hrsFin;

    @Enumerated(EnumType.STRING)
    private Dia dia;

    
}

