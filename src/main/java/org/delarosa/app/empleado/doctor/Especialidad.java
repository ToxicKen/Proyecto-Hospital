package org.delarosa.app.empleado.doctor;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEspecialidad;

    @Column(nullable = false,unique = true)
    private String nombre;

    @Column(precision =  19, scale = 2)
    private BigDecimal costo;
}
