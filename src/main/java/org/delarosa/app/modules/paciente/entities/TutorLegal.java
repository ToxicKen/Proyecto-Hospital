package org.delarosa.app.modules.paciente.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TutorLegal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTutorLegal;

    private String nombre;

    private String apellidoP;

    private String apellidoM;

    @Column(unique = true)
    private String curp;
//
//    @OneToMany
//    @JoinColumn(name = "idPaciente", referencedColumnName = "idPaciente")
//    private List<Paciente> tutelados;

}