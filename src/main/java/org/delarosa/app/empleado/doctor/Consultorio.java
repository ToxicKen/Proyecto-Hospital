package org.delarosa.app.doctor;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Consultorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idConsultorio;

    @Column(nullable = false,unique = true)
    private Integer numero;
}
