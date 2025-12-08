package org.delarosa.app.modules.paciente.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Alergia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAlergia;

    @Column(unique = true, nullable = false, length = 30)
    private String nombre;

}
