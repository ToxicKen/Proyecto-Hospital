package org.delarosa.app.modules.personal.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEspecialidad;

    @Column(nullable = false,unique = true)
    private String nombre;

    @Column(precision =  19, scale = 2,nullable = false)
    private BigDecimal costo;
}
