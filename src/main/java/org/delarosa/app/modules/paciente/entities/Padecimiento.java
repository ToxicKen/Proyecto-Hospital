package org.delarosa.app.modules.paciente.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Padecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPadecimiento;

    @Column(unique = true, nullable = false)
    private String nombre;

}
