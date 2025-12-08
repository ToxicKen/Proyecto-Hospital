package org.delarosa.app.modules.clinico.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Receta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int folioReceta;

    @OneToOne
    @JoinColumn(name = "folioCita")
    private Cita cita;

    private String diagnostico;

    private String observaciones;


    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MedicamentosReceta> medicamentos = new ArrayList<>();

}
