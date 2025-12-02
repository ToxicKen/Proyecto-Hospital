package org.delarosa.app.paciente;

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

    @Column(unique = true, nullable = false)
    private String nombre;

}
